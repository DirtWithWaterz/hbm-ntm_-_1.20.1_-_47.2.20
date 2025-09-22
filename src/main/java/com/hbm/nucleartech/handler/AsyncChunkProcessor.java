package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.common.util.NonNullSupplier;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AsyncChunkProcessor {
    private static final int QUEUE_CAPACITY = 16; // Maximum number of chunks to queue (original)
    private static final long QUEUE_TIMEOUT_MS = 500; // 1 second timeout for queue operations
    
    // Get the number of concurrent chunks from config, with a minimum of 1
    private static int getMaxConcurrentChunks() {
        return Math.max(1, com.hbm.nucleartech.Config.concurrentChunkThreads);
    }
    
    private static final ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r, "Radiation Chunk Processor");
        t.setDaemon(true);
        t.setUncaughtExceptionHandler((thread, throwable) -> {
            System.err.println("Uncaught exception in " + thread.getName() + ": " + throwable.getMessage());
            throwable.printStackTrace();
        });
        return t;
    };
    
    private static final ExecutorService executorService = new ThreadPoolExecutor(
        getMaxConcurrentChunks(),
        getMaxConcurrentChunks(),
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(QUEUE_CAPACITY),
        threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy() // Fallback to running on calling thread if queue is full
    ) {
        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            // Log thread pool stats periodically
            if (System.currentTimeMillis() % 10000 < 50) { // Log roughly every 10 seconds
                HBM.LOGGER.debug("Chunk processor stats - Active: {}, Queue: {}/{}",
                    getActiveCount(), getQueue().size(), QUEUE_CAPACITY);
            }
        }
    };

    // --- Use net.minecraft.world.level.ChunkPos consistently as the key ---
    private static final Map<net.minecraft.world.level.ChunkPos, ProcessChunkTask> pendingTasks = new ConcurrentHashMap<>();

    private static class ProcessChunkTask implements Runnable {
        private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ProcessChunkTask.class);
        final ServerLevel world;
        final LevelChunk chunk;
        final int sectionY;
        final RadiationSystemChunksNT.RadPocket pocket;
        final LevelChunkSection section;

        ProcessChunkTask(ServerLevel world, LevelChunk chunk, int sectionY, RadiationSystemChunksNT.RadPocket pocket) {
            this.world = world;
            this.chunk = chunk;
            this.sectionY = sectionY;
            this.pocket = pocket;
            this.section = chunk.getSections()[sectionY];
            LOGGER.info("Created task for chunk at ({}, {}) sectionY: {}", chunk.getPos().x, chunk.getPos().z, sectionY);
        }

        @Override
        public void run() {
            try {
                // MUST run on server thread because we access chunk/section/world APIs
                List<BlockPos> blocksToUpdate = new ArrayList<>();

                int chunkX = chunk.getPos().x;
                int chunkZ = chunk.getPos().z;
                int baseY = world.getMinBuildHeight() + (sectionY << 4);

                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            BlockState state = section.getBlockState(x, y, z);
                            if (state.isAir()) continue;

                            if (state.getBlock() != net.minecraft.world.level.block.Blocks.GRASS_BLOCK &&
                                    state.getBlock() != net.minecraft.world.level.block.Blocks.PODZOL) {
                                continue;
                            }

                            int worldX = (chunkX << 4) + x;
                            int worldY = baseY + y;
                            int worldZ = (chunkZ << 4) + z;
                            BlockPos worldPos = new BlockPos(worldX, worldY, worldZ);

                            if (pocket.parent.getPocket(worldPos) != pocket) continue;

                            if (world.random.nextInt(100) <= 60) continue;

                            // collect to apply after scanning (still on server thread)
                            blocksToUpdate.add(worldPos);
                        }
                    }
                }

                if (!blocksToUpdate.isEmpty()) {
                    LOGGER.info("Scheduling {} block updates for chunk at ({}, {})", blocksToUpdate.size(), chunk.getPos().x, chunk.getPos().z);
                    int updated = 0;
                    for (BlockPos pos : blocksToUpdate) {
                        if (world.getBlockState(pos).getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK) {
                            world.setBlock(pos, RegisterBlocks.DEAD_GRASS.get().defaultBlockState(), 2);
                            updated++;
                        }
                    }
                    LOGGER.info("Updated {} grass blocks to dead grass in chunk at ({}, {})", updated, chunk.getPos().x, chunk.getPos().z);
                }
            } catch (Exception e) {
                LOGGER.error("Error processing chunk at ({}, {})", chunk.getPos().x, chunk.getPos().z, e);
            } finally {
                // remove using the same key type we used to put
                pendingTasks.remove(new net.minecraft.world.level.ChunkPos(chunk.getPos().x, chunk.getPos().z));
            }
        }
    }

    public static void queueChunkForProcessing(ServerLevel world, LevelChunk chunk, int sectionY, RadiationSystemChunksNT.RadPocket pocket) {
        if (chunk == null || world == null || pocket == null) return;

        net.minecraft.world.level.ChunkPos chunkPos = new net.minecraft.world.level.ChunkPos(chunk.getPos().x, chunk.getPos().z);

        if (pendingTasks.containsKey(chunkPos)) return;

        ProcessChunkTask task = new ProcessChunkTask(world, chunk, sectionY, pocket);
        pendingTasks.put(chunkPos, task);

        // Schedule the entire task to run on the server/main thread.
        // This is safe for chunk access and block updates.
        try {
            world.getServer().execute(task);
        } catch (Exception e) {
            pendingTasks.remove(chunkPos);
            HBM.LOGGER.error("Failed to schedule chunk processing on server thread for chunk ({}, {})", chunk.getPos().x, chunk.getPos().z, e);
        }
    }

    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(50, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private static class ChunkPos {
        public final int x, z;
        
        public ChunkPos(int x, int z) {
            this.x = x;
            this.z = z;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkPos chunkPos = (ChunkPos) o;
            return x == chunkPos.x && z == chunkPos.z;
        }
        
        @Override
        public int hashCode() {
            return 31 * x + z;
        }
    }
}
