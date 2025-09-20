package com.hbm.nucleartech.handler;

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
    private static final int MAX_CONCURRENT_CHUNKS = 4; // Process up to 4 chunks in parallel (original)
    private static final int QUEUE_CAPACITY = 16; // Maximum number of chunks to queue (original)
    private static final long QUEUE_TIMEOUT_MS = 1000; // 1 second timeout for queue operations
    
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
        MAX_CONCURRENT_CHUNKS,
        MAX_CONCURRENT_CHUNKS,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(QUEUE_CAPACITY),
        threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy() // Fallback to running on calling thread if queue is full
    );
    
    private static final BlockingQueue<ProcessChunkTask> taskQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    private static final Map<ChunkPos, ProcessChunkTask> pendingTasks = new ConcurrentHashMap<>();
    
    private static class ProcessChunkTask implements Callable<Void> {
        private static final org.apache.logging.log4j.Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ProcessChunkTask.class);
        private final ServerLevel world;
        private final LevelChunk chunk;
        private final int sectionY;
        private final RadiationSystemChunksNT.RadPocket pocket;
        private final BlockPos sectionPos;
        private final LevelChunkSection section;
        private int processed = 0;
        
        public ProcessChunkTask(ServerLevel world, LevelChunk chunk, int sectionY, RadiationSystemChunksNT.RadPocket pocket) {
            this.world = world;
            this.chunk = chunk;
            this.sectionY = sectionY;
            this.pocket = pocket;
            this.sectionPos = new BlockPos(
                chunk.getPos().x << 4,
                sectionY << 4,
                chunk.getPos().z << 4
            );
            this.section = chunk.getSections()[sectionY];
//            LOGGER.info("Created task for chunk at ({}, {}) with sectionY: {}, section minY: {}",
//                chunk.getPos().x, chunk.getPos().z, sectionY, sectionY << 4);
        }
        
        @Override
        public Void call() {
            try {
                List<BlockPos> blocksToUpdate = new ArrayList<>();
                
                // Check all blocks in the chunk section
                for (int y = 0; y < 16; y++) {
                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                        BlockState state = section.getBlockState(x, y, z);
                        if (state.isAir()) continue;

                        // Calculate world position directly from chunk and section coordinates
                        int chunkX = chunk.getPos().x;
                        int chunkZ = chunk.getPos().z;
                        int worldX = (chunkX << 4) + x;
                        // Fix Y-coordinate calculation - sectionY is already the chunk's section index (0-15 for the chunk's height)
                        // and y is the local Y within that section (0-15)
                        int worldY = world.getMinBuildHeight() + (sectionY << 4) + y;
                        int worldZ = (chunkZ << 4) + z;
                        BlockPos worldPos = new BlockPos(worldX, worldY, worldZ);
                        
                        if (state.getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK || state.getBlock() ==  net.minecraft.world.level.block.Blocks.PODZOL) {
//                            LOGGER.info("Processing grass block - Chunk: ({},{}), SectionY: {}, Local: ({},{},{}), World: {}",
//                                chunkX, chunkZ, sectionY, x, y, z, worldPos);
//                            LOGGER.info("World min height: {}, Section base Y: {}",
//                                world.getMinBuildHeight(), world.getMinBuildHeight() + (sectionY << 4));
                        }

                        // Check if this is a grass block or podzol with air above it (surface check)
                        if (state.getBlock() != net.minecraft.world.level.block.Blocks.GRASS_BLOCK &&
                            state.getBlock() != net.minecraft.world.level.block.Blocks.PODZOL) {
                            continue;
                        }

//                        // Verify the block above is air (surface check)
//                        if (!world.isEmptyBlock(worldPos.above())) {
////                            LOGGER.trace("Skipping non-surface grass at {}", worldPos);
//                            continue;
//                        }

                        if (pocket.parent.getPocket(worldPos) != pocket) {
//                            LOGGER.trace("Skipping block not in pocket at {}", worldPos);
                            continue;
                        }

                        if (world.random.nextInt(100) <= 60) {
//                            LOGGER.trace("Random skip at {}", worldPos);
                            continue;
                        }

//                        LOGGER.info("Queueing grass block for replacement at {}", worldPos);
                        blocksToUpdate.add(worldPos);
                        processed++;
                        
                        // Replace the grass block with dead grass
                        world.setBlock(worldPos, RegisterBlocks.DEAD_GRASS.get().defaultBlockState(), 3);

                        // Update the block to trigger any necessary updates
                        world.sendBlockUpdated(worldPos, world.getBlockState(worldPos), RegisterBlocks.DEAD_GRASS.get().defaultBlockState(), 3);
                    }
                }
            }
                // Apply changes on the main thread
                if (!blocksToUpdate.isEmpty()) {
//                    LOGGER.info("Scheduling {} block updates for chunk at ({}, {})",
//                        blocksToUpdate.size(), chunk.getPos().x, chunk.getPos().z);
                    world.getServer().execute(() -> {
                        int updated = 0;
                        for (BlockPos pos : blocksToUpdate) {
                            if (world.getBlockState(pos).getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK) {
                                world.setBlock(pos, RegisterBlocks.DEAD_GRASS.get().defaultBlockState(), 18);
                                updated++;
                            } else {
//                                LOGGER.debug("Block at {} is no longer grass, skipping", pos);
                            }
                        }
//                        LOGGER.info("Updated {} grass blocks to dead grass in chunk at ({}, {})",
//                            updated, chunk.getPos().x, chunk.getPos().z);
                    });
                }
                
                return null;
            } catch (Exception e) {
                LOGGER.error("Error processing chunk at ({}, {})", chunk.getPos().x, chunk.getPos().z, e);
                throw new RuntimeException("Failed to process chunk at (" + chunk.getPos().x + ", " + chunk.getPos().z + ")", e);
            } finally {
                // Clean up the pending task
                pendingTasks.remove(new net.minecraft.world.level.ChunkPos(chunk.getPos().x, chunk.getPos().z));
            }
        }
    }
    
    public static void queueChunkForProcessing(ServerLevel world, LevelChunk chunk, int sectionY, RadiationSystemChunksNT.RadPocket pocket) {
        if (chunk == null || world == null || pocket == null) {
            return;
        }
        
        ChunkPos chunkPos = new ChunkPos(chunk.getPos().x, chunk.getPos().z);
        
        // Skip if this chunk is already queued for processing
        if (pendingTasks.containsKey(chunkPos)) {
            return;
        }
        
        ProcessChunkTask task = new ProcessChunkTask(world, chunk, sectionY, pocket);
        pendingTasks.put(chunkPos, task);
        
        try {
            // Try to add to queue with timeout
            if (taskQueue.offer(task, QUEUE_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                executorService.submit(() -> {
                    try {
                        ProcessChunkTask currentTask = taskQueue.take();
                        try {
                            currentTask.call();
                        } finally {
                            // Ensure we always clean up, even if there's an exception
                            pendingTasks.remove(new ChunkPos(currentTask.chunk.getPos().x, currentTask.chunk.getPos().z));
                        }
                    } catch (Exception e) {
                        System.err.println("Error in chunk processing task: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            } else {
                // If we couldn't add to queue, clean up
                pendingTasks.remove(chunkPos);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pendingTasks.remove(chunkPos);
        } catch (Exception e) {
            System.err.println("Unexpected error queueing chunk for processing: " + e.getMessage());
            e.printStackTrace();
            pendingTasks.remove(chunkPos);
        }
    }
    
    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
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
