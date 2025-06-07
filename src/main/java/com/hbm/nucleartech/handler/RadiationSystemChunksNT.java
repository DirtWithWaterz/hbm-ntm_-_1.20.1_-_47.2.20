package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * RadiationSystemNT (Forge 1.20.1 port of HBM’s Nuclear Tech 1.12.2 3D radiation system).
 *
 * This class encapsulates:
 *  - IChunkRadiation: Capability interface for per-chunk radiation storage.
 *  - RadPocket: Represents one connected, “open” region in a 16×16×16 subchunk.
 *  - SubChunkRadiationStorage: Manages flood-fill pocket detection inside one subchunk (24 per chunk).
 *  - ChunkRadiationStorage: The actual capability implementation attached to each LevelChunk.
 *  - RadiationEventHandlers: Forge event subscribers to register, attach, and tick radiation.
 *
 * Key differences from 1.12.2:
 *  - World height is –64..319 (384 blocks = 24 subchunks of 16 blocks).
 *  - Uses Minecraft 1.20.1 Forge capability system (AttachCapabilitiesEvent<LevelChunk>).
 *  - Replaces EnumFacing with Direction.
 *  - Uses world.getBlockState(pos) instead of raw Chunk storage.
 */
@Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.MOD)
public class RadiationSystemChunksNT {
    // -----------------------------------
    // CAPABILITY REGISTRATION
    // -----------------------------------

    /** Capability instance token for IChunkRadiation */
    public static final Capability<IChunkRadiation> CHUNK_RAD_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

    @Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModSetup {
        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(IChunkRadiation.class);
        }

        // If any mod-common setup is needed, we could subscribe here:
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            // No-op for now
        }
    }

//    public static Set<RadPocket> allActivePockets = new HashSet<>();

    public static Set<RadPocket> getAllActivePockets() {

        Set<RadPocket> result = new HashSet<>();

        for(LevelChunk chunk : loadedChunks) {

            chunk.getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(cap -> {

                result.addAll(cap.getActivePockets());
            });
        }

        return result;
    }

    public static void setRadiationAt(ServerLevel level, BlockPos blockPos, float radAmount, float maxRadAmount) {

        LevelChunk chunk = level.getChunkAt(blockPos);
        chunk.getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(cap -> {

            cap.setRadiationAt(level, blockPos, radAmount, maxRadAmount);
        });
    }

    public static void addRadiationAt(ServerLevel level, BlockPos blockPos, float radAmount, float maxRadAmount) {

        LevelChunk chunk = level.getChunkAt(blockPos);
        chunk.getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(cap -> {

            cap.addRadiationAt(level, blockPos, radAmount, maxRadAmount);
        });
    }

    public static float getRadiationAt(ServerLevel level, BlockPos blockPos) {

        LevelChunk chunk = level.getChunkAt(blockPos);
        AtomicReference<Float> r = new AtomicReference<>((float) 0);
        chunk.getCapability(CHUNK_RAD_CAPABILITY, null).ifPresent(cap -> {

            r.set(cap.getRadiationAt(level, blockPos));
        });
        return r.get();
    }

    // -----------------------------------
    // IChunkRadiation INTERFACE
    // -----------------------------------

    /**
     * Capability interface for storing 3D radiation per chunk.
     */
    public interface IChunkRadiation {
        /**
         * Rebuilds (flood-fills) all radPockets in each of the 24 subchunks of this chunk.
         * Should be called on chunk load or when blocks change.
         */
        void rebuildAllPockets(ServerLevel world, LevelChunk chunk);

        /**
         * Returns the RadPocket containing the given world BlockPos,
         * or null if that block is sealed/solid (no pocket).
         */
        @Nullable
        RadPocket getPocketAt(ServerLevel world, BlockPos pos);

        void setRadiationAt(ServerLevel world, BlockPos pos, float radAmount, float maxRadAmount);

        /**
         * Adds the specified radiation amount (in RAD units) at world BlockPos.
         * Finds the pocket and increments its radiation, marking it active.
         */
        void addRadiationAt(ServerLevel world, BlockPos pos, float radAmount, float maxRadAmount);

        float getRadiationAt(ServerLevel world, BlockPos pos);

        /**
         * Called each server tick to diffuse and decay radiation in all active pockets.
         */
        void tickSpreadAndDecay(ServerLevel world, LevelChunk chunk);

        /**
         * Optional: Clear all active pockets (e.g. on chunk unload).
         */
        void clearActivePockets();

        Set<RadPocket> getActivePockets();

        CompoundTag serializeNBT();

        void deserializeNBT(CompoundTag nbt);
    }

    // -----------------------------------
    // RadPocket CLASS
    // -----------------------------------

    /**
     * Represents one contiguous “open” region (air or non-rad-resistant) in a 16×16×16 subchunk.
     */
    public static class RadPocket {
        public final SubChunkRadiationStorage parent;
        public final int index;           // 0..(pocketsInThisSubchunk−1)
        public float radiation = 0f;      // Current stored radiation value
        public float accumulatedRads = 0f;// Temporary accumulator for simultaneous spreading

        /** For each of the six directions, holds indices of connected pockets, or −1 if neighbor subchunk not loaded */
        public final List<Integer>[] connectionIndices = new ArrayList[Direction.values().length];

        // Bounding-box of local coordinates (0..15 each). For potential optimizations.
        public int minLocalX = 16, maxLocalX = 0;
        public int minLocalY = 16, maxLocalY = 0;
        public int minLocalZ = 16, maxLocalZ = 0;

        public BlockPos getSubChunkPos() {

            return new BlockPos(parent.parentChunk.chunk.getPos().x, (index * 24) - 64, parent.parentChunk.chunk.getPos().z);
        }

        @SuppressWarnings("unchecked")
        public RadPocket(SubChunkRadiationStorage parent, int idx) {
            this.parent = parent;
            this.index = idx;
            for (int i = 0; i < Direction.values().length; i++) {
                connectionIndices[i] = new ArrayList<>(4);
            }
        }

        /**
         * Returns the full set of RadPockets connected to `start`,
         * across all chunks/subchunks (excluding unloaded neighbors).
         */
        public static Set<RadPocket> floodConnected(RadPocket start, ServerLevel world) {

            if(start.isSealed())
                return null;

            Set<RadPocket> visited = new HashSet<>();
            Deque<RadPocket> queue = new ArrayDeque<>();
            visited.add(start);
            queue.add(start);

            while (!queue.isEmpty()) {
                RadPocket current = queue.removeFirst();
                List<RadPocket> neighbors = current.getNeighborPockets(world);

                for (RadPocket nb : current.getNeighborPockets(world)) {
                    if (visited.add(nb)) {
                        queue.addLast(nb);
                    }
                }
            }
            return visited;
        }

        /**
         * For each direction this pocket has a connection index ≥0,
         * fetch the actual RadPocket in that direction.
         */
        public List<RadPocket> getNeighborPockets(ServerLevel world) {
            List<RadPocket> neighbors = new ArrayList<>();
            // we need the parentChunk & its capability
            ChunkRadiationStorage cap = parent.parentChunk;
            LevelChunk thisChunk = cap.chunk;
            int thisY = parent.yIndex;
            ChunkPos thisChunkPos = thisChunk.getPos();

            for (Direction dir : Direction.values()) {
                List<Integer> connList = connectionIndices[dir.ordinal()];
                if (connList.isEmpty()) continue;

                for (int idx : connList) {
                    if (idx < 0) {
                        // neighbor chunk not loaded yet
                        continue;
                    }

                    RadPocket out = null;
                    // vertical neighbor in same chunk?
                    if (dir == Direction.UP || dir == Direction.DOWN) {
                        int ny = thisY + (dir == Direction.UP ? 1 : -1);
                        if (ny >= 0 && ny < cap.subData.length) {
                            SubChunkRadiationStorage sc = cap.subData[ny];
                            if (sc != null && idx < sc.pockets.length) {
                                out = sc.pockets[idx];
                            }
                        }
                    } else {
                        // horizontal neighbor → same subchunkY but different chunk X/Z
                        int nx = thisChunkPos.x + dir.getStepX();
                        int nz = thisChunkPos.z + dir.getStepZ();
                        // get neighbor chunk
                        if (world.getChunkSource().hasChunk(nx, nz)) {
                            LevelChunk neighborChunk = world.getChunk(nx, nz);
                            neighborChunk.getCapability(CHUNK_RAD_CAPABILITY)
                                    .ifPresent(cap2 -> {
                                        RadPocket p2 = cap2.getPocketAt(world,
                                                // pick any block in this pocket, shifted one block in dir:
                                                // we can reconstruct a sample pos:
                                                thisChunkPos.getWorldPosition()
                                                        .offset(dir.getStepX(), /*same local y:*/ thisY*16, dir.getStepZ())
                                        );
                                        if (p2 != null) {
                                            neighbors.add(p2);
                                        }
                                    });
                            continue;
                        }
                    }

                    if (out != null) {
                        neighbors.add(out);
                    }
                }
            }
            return neighbors;
        }


        /** Expand local bounds to include this local coordinate (lx, ly, lz each 0..15). */
        public void addBlockToBounds(int lx, int ly, int lz) {
            if (lx < minLocalX) minLocalX = lx;
            if (lx > maxLocalX) maxLocalX = lx;
            if (ly < minLocalY) minLocalY = ly;
            if (ly > maxLocalY) maxLocalY = ly;
            if (lz < minLocalZ) minLocalZ = lz;
            if (lz > maxLocalZ) maxLocalZ = lz;
        }

        /** Checks whether this pocket is fully sealed (no adjacent pockets, no unloaded connections). */
        public boolean isSealed() {
            int totalLinks = 0;
            for (List<Integer> list : connectionIndices) {
                totalLinks += list.size();
            }
            return (totalLinks == 0);
        }

        public static boolean isGraphSealed(RadPocket start, ServerLevel world) {
            // If any pocket in the connected graph has a `-1` in its connectionIndices,
            // that means a neighbor chunk wasn’t loaded, so it could leak later.
            Set<RadPocket> graph = floodConnected(start, world);
            for (RadPocket p : graph) {
                // p.isSealed() only checks that p.connectionIndices lists are empty.
                // But we also need to ensure *no* direction had a `-1` entry:
                for (List<Integer> conns : p.connectionIndices) {
                    if (conns.contains(-1)) {
                        return false; // has an unloaded neighbor → not fully sealed
                    }
                    if (!conns.isEmpty()) {
                        return false; // has a live connection to another pocket
                    }
                }
            }
            return true;
        }
    }

    // -----------------------------------
    // SubChunkRadiationStorage CLASS
    // -----------------------------------

    /**
     * Stores all radPocket data for exactly one 16×16×16 vertical slice (“subchunk”) of a chunk.
     * There are 24 such slices per chunk, because world Y = –64..319 (384/16 = 24).
     */
    public static class SubChunkRadiationStorage {
        public final ChunkRadiationStorage parentChunk; // Back-pointer to the chunk capability
        public final int yIndex;                      // 0..23
        public RadPocket[] pockets;                    // All pockets found in this subchunk
        public int[] pocketsByBlock;                   // Length 4096 = 16×16×16; stores pocket index or −1

        public SubChunkRadiationStorage(ChunkRadiationStorage parent, int yIndex) {
            this.parentChunk = parent;
            this.yIndex = yIndex;
            this.pocketsByBlock = new int[16 * 16 * 16];
            Arrays.fill(this.pocketsByBlock, -1);
            this.pockets = new RadPocket[0];
        }

        public boolean blockHasPocket(BlockPos pos) {
            // Ensure the block is within this chunk
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            int thisChunkX = parentChunk.chunk.getPos().x;
            int thisChunkZ = parentChunk.chunk.getPos().z;

            if (chunkX != thisChunkX || chunkZ != thisChunkZ) return false;

            // Convert world Y to local subchunk Y
            int yShift = pos.getY() + 64; // world Y -64..319 → shifted 0..383
            int subchunkIndex = yShift >> 4;
            if (subchunkIndex != this.yIndex) return false;

            // Compute local positions (0–15)
            int localX = pos.getX() & 15;
            int localY = yShift & 15;
            int localZ = pos.getZ() & 15;

            // Compute flat index
            int flatIdx = localX + (localZ << 4) + (localY << 8);
            if (flatIdx < 0 || flatIdx >= pocketsByBlock.length) return false;

            return pocketsByBlock[flatIdx] != -1;
        }

        /**
         * Flood-fills this subchunk to find all connected “open” regions (non-rad-resistant).
         * Populates pockets[] and pocketsByBlock[] accordingly.
         */
        public void buildAllPockets(ServerLevel world) {
            Arrays.fill(pocketsByBlock, -1);
            List<RadPocket> pocketList = new ArrayList<>();
            int nextPocketIdx = 0;

            // Compute the base Y for this subchunk: y = (yIndex*16 − 64) … (yIndex*16 − 64 + 15)
            int baseY = (yIndex << 4) - 64;

            // Loop over all 16×16×16 local positions
            for (int localX = 0; localX < 16; localX++) {
                for (int localZ = 0; localZ < 16; localZ++) {
                    for (int localY = 0; localY < 16; localY++) {
                        int flatIdx = localX + (localZ << 4) + (localY << 8);
                        if (pocketsByBlock[flatIdx] != -1) continue; // Already assigned

                        // Compute global BlockPos
                        int worldX = (parentChunk.chunk.getPos().x << 4) + localX;
                        int worldY = baseY + localY;
                        int worldZ = (parentChunk.chunk.getPos().z << 4) + localZ;
                        BlockPos pos = new BlockPos(worldX, worldY, worldZ);

                        BlockState bs = world.getBlockState(pos);
                        // Skip sealed blocks (rad-resistant)
                        if (bs.getBlock() instanceof IRadResistantBlock
                                && ((IRadResistantBlock) bs.getBlock()).isRadResistant(world, pos)) {
                            continue;
                        }

                        // Start a new RadPocket via flood fill
                        RadPocket pocket = buildPocket(world, pos, localX, localY, localZ, nextPocketIdx);
                        pocketList.add(pocket);
                        nextPocketIdx++;
                    }
                }
            }

            this.pockets = pocketList.toArray(new RadPocket[0]);
        }

        /**
         * Flood-fill algorithm starting from one local “open” block.
         *
         * @param world      – ServerLevel
         * @param startPos   – Global BlockPos inside this subchunk
         * @param startX     – Local X (0..15)
         * @param startY     – Local Y (0..15)
         * @param startZ     – Local Z (0..15)
         * @param pocketIdx  – The assigned index for this pocket
         * @return New RadPocket capturing that entire connected region
         */
        private RadPocket buildPocket(ServerLevel world, BlockPos startPos,
                                      int startX, int startY, int startZ, int pocketIdx) {
            RadPocket pocket = new RadPocket(this, pocketIdx);
            Deque<BlockPos> queue = new ArrayDeque<>(2048);

            // Mark the starting block visited
            int startFlat = startX + (startZ << 4) + (startY << 8);
            pocketsByBlock[startFlat] = pocketIdx;
            pocket.addBlockToBounds(startX, startY, startZ);
            queue.addLast(startPos);

            while (!queue.isEmpty()) {
                BlockPos current = queue.removeFirst();
                int curX = current.getX();
                int curY = current.getY();
                int curZ = current.getZ();

                // Compute local coords in this subchunk
                int yShift = curY + 64; // Shift so that –64 maps to 0
                if (yShift < 0 || yShift >= 384) continue;
                int localY = yShift & 0xF;
                int localX = curX & 0xF;
                int localZ = curZ & 0xF;
                int curFlat = localX + (localZ << 4) + (localY << 8);

                // Check each of the six neighbors
                for (Direction dir : Direction.values()) {
                    BlockPos neighbor = current.relative(dir);
                    int nYShift = neighbor.getY() + 64;
                    if (nYShift < 0 || nYShift >= 384) {
                        // Out of world bounds: treat as sealed boundary
                        continue;
                    }
                    int neighborSubIdx = (nYShift >> 4);
                    int nLocalY = nYShift & 0xF;
                    int nLocalX = neighbor.getX() & 0xF;
                    int nLocalZ = neighbor.getZ() & 0xF;
                    int nFlat = nLocalX + (nLocalZ << 4) + (nLocalY << 8);

                    // Check if neighbor is in the same subchunk:
                    boolean sameChunkX = (neighbor.getX() >> 4) == (curX >> 4);
                    boolean sameChunkZ = (neighbor.getZ() >> 4) == (curZ >> 4);
                    if (neighborSubIdx == this.yIndex && sameChunkX && sameChunkZ) {
                        // Inside same 16×16×16 subchunk
                        if (pocketsByBlock[nFlat] == -1) {
                            // Check if neighbor is open or sealed
                            BlockState nbs = world.getBlockState(neighbor);
                            boolean isResistant = (nbs.getBlock() instanceof IRadResistantBlock
                                    && ((IRadResistantBlock) nbs.getBlock()).isRadResistant(world, neighbor));
                            if (!isResistant) {
                                // Mark and enqueue
                                pocketsByBlock[nFlat] = pocketIdx;
                                pocket.addBlockToBounds(nLocalX, nLocalY, nLocalZ);
                                queue.addLast(neighbor);
                            }
                        }
                        // else already belongs to some pocket—skip
                    } else {
                        // Neighbor is in an adjacent subchunk or adjacent chunk
                        // Determine if that chunk is loaded
                        ServerLevel serverWorld = world;
                        int chunkX = neighbor.getX() >> 4;
                        int chunkZ = neighbor.getZ() >> 4;
                        if (!serverWorld.getChunkSource().hasChunk(chunkX, chunkZ)) {
                            // Chunk not loaded: add −1 sentinel if not already present
                            List<Integer> connList = pocket.connectionIndices[dir.ordinal()];
                            if (!connList.contains(-1)) {
                                connList.add(-1);
                            }
                        } else {
                            // Chunk loaded: fetch neighbor chunk’s capability, then get its pocket
                            LevelChunk neighborChunk = serverWorld.getChunk(chunkX, chunkZ);
                            neighborChunk.getCapability(CHUNK_RAD_CAPABILITY).ifPresent(cap -> {
                                RadPocket outPocket = cap.getPocketAt(serverWorld, neighbor);
                                if (outPocket != null) {
                                    int outIdx = outPocket.index;
                                    List<Integer> connList = pocket.connectionIndices[dir.ordinal()];
                                    if (!connList.contains(outIdx)) {
                                        connList.add(outIdx);
                                    }
                                }
                            });
                        }
                    }
                }
            } // end BFS

            return pocket;
        }
    }

    // -----------------------------------
    // ChunkRadiationStorage (Capability) CLASS
    // -----------------------------------

    /**
     * Capability implementation attached to each LevelChunk. Holds 24 SubChunkRadiationStorage instances (one per vertical slice).
     */
    public static class ChunkRadiationStorage implements ICapabilitySerializable<CompoundTag> {
        public final LevelChunk chunk;
        private final IChunkRadiation instance = new Impl();
        public final SubChunkRadiationStorage[] subData = new SubChunkRadiationStorage[24];

        public ChunkRadiationStorage(LevelChunk chunk) {
            this.chunk = chunk;
        }

        /** The actual implementation of IChunkRadiation */
        private class Impl implements IChunkRadiation {
            private final Set<RadPocket> activePockets = new HashSet<>();

            private void ensureSubchunkExists(int idx) {
                if (subData[idx] == null) {
                    System.out.println("Subchunk " + idx + " does not exist. Creating new subchunk radiation storage object...");
                    subData[idx] = new SubChunkRadiationStorage(ChunkRadiationStorage.this, idx);
                }
//                else {
//
//                    System.out.println("Subchunk exists. Moving on to next step...");
//                }
            }

            @Override
            public void rebuildAllPockets(ServerLevel world, LevelChunk chunk) {
                System.out.println("Entering for loop");
                for (int i = 0; i < 24; i++) {
                    System.err.println("Ensuring subchunk " + i + " exists...");
                    ensureSubchunkExists(i);
                    System.err.println("Building all pockets...");
                    subData[i].buildAllPockets(world);
                }
                chunk.setUnsaved(true);
            }

            @Nullable
            @Override
            public RadPocket getPocketAt(ServerLevel world, BlockPos pos) {
                int yShift = pos.getY() + 64;
                if (yShift < 0 || yShift >= 384) return null;
                int subIdx = (yShift >> 4);
                if (subIdx < 0 || subIdx >= 24) return null;

                ensureSubchunkExists(subIdx);

                int localX = pos.getX() & 0xF;
                int localZ = pos.getZ() & 0xF;
                int localY = yShift & 0xF;
                int flatIdx = localX + (localZ << 4) + (localY << 8);

                int pocketIdx = subData[subIdx].pocketsByBlock[flatIdx];
                if (pocketIdx < 0) return null;
                RadPocket[] pocketsArr = subData[subIdx].pockets;
                if (pocketIdx >= pocketsArr.length) return null;
                return pocketsArr[pocketIdx];
            }

            @Override
            public void setRadiationAt(ServerLevel world, BlockPos pos, float radAmount, float maxRadAmount) {
                RadPocket p = getPocketAt(world, pos);
                if(p != null) {
                    p.radiation = radAmount;
                    p.radiation = Mth.clamp(p.radiation, 0, maxRadAmount);
                    activePockets.add(p);
                    chunk.setUnsaved(true);
                }
            }

            @Override
            public void addRadiationAt(ServerLevel world, BlockPos pos, float radAmount, float maxRadAmount) {
                RadPocket p = getPocketAt(world, pos);
                if (p != null) {
                    p.radiation += radAmount;
                    p.radiation = Mth.clamp(p.radiation, 0, maxRadAmount);
                    activePockets.add(p);
                    chunk.setUnsaved(true);
                }
            }

            @Override
            public float getRadiationAt(ServerLevel world, BlockPos pos) {

                RadPocket p = getPocketAt(world, pos);
                if (p != null)
                    return p.radiation;
                else
                    return -1;
            }

            @Override
            public void tickSpreadAndDecay(ServerLevel world, LevelChunk chunk) {
                // Step 1: Spread radiation from each active pocket to its neighbors
                List<RadPocket> newlyActive = new ArrayList<>();

                for (RadPocket p : activePockets) {
                    if (p.radiation <= 0f) continue;

                    // Compute amount to spread per direction
                    float spreadFactor = 0.005f; // 0.5% spread example
                    float toSpread = p.radiation * spreadFactor;
                    p.radiation *= (1f - spreadFactor);

                    if(p.isSealed())
                        continue;
                    // Distribute to each neighbor based on connectionIndices
                    for (Direction dir : Direction.values()) {
                        List<Integer> connList = p.connectionIndices[dir.ordinal()];
                        if (connList.isEmpty()) continue;

                        // Determine neighbor subchunk index based on direction
                        int neighborSubIdx = p.parent.yIndex + (dir == Direction.UP ? 1 : (dir == Direction.DOWN ? -1 : 0));
                        for (int idx : connList) {
                            if (idx == -1) {
                                // Neighbor subchunk not loaded; skip until it loads
                                continue;
                            }
                            // Reach the neighbor pocket
                            SubChunkRadiationStorage neighborSC = null;
                            // If direction is vertical (UP/DOWN) and within same chunk:
                            if (dir == Direction.UP || dir == Direction.DOWN) {
                                if (neighborSubIdx >= 0 && neighborSubIdx < 24) {
                                    neighborSC = subData[neighborSubIdx];
                                }
                            } else {
                                // Horizontal neighbor might be in different chunk
                                // We reconstruct the neighbor’s world coordinates of a sample block in this pocket,
                                // then ask that chunk’s capability for that pocket.
                                // For simplicity, assume neighbor pocket is in same yIndex but different chunk XY:
                                // We retrieve neighbor chunk’s capability based on calling getPocketAt(...)
                                // Will implement by getting one local block of this pocket, offset by dir.
                                // But for performance, we rely on the fact that connectionIndices stored correct indices.
                                neighborSC = p.parent.parentChunk.subData[p.parent.yIndex];
                            }
                            if (neighborSC != null) {
                                RadPocket neighborPocket = neighborSC.pockets[idx];
                                neighborPocket.accumulatedRads += toSpread / connList.size();
                            }
                        }
                    }
                }

                // Step 2: After distributing, add accumulatedRads to each pocket
                for (int i = 0; i < 24; i++) {
                    if (subData[i] == null) continue;
                    for (RadPocket p : subData[i].pockets) {
                        if (p.accumulatedRads > 0f) {
                            p.radiation += p.accumulatedRads;
                            p.accumulatedRads = 0f;
                            newlyActive.add(p);
                        }
                    }
                }

                // Step 3: Decay and update activePockets set
                Iterator<RadPocket> it = activePockets.iterator();
                while (it.hasNext()) {
                    RadPocket p = it.next();
                    // Example decay factor: lose 0.5% per tick
                    p.radiation *= 0.995f;
                    if (p.radiation <= 0.01f) {
                        it.remove();
                    } else {
                        newlyActive.add(p);
                    }
                }
                activePockets.addAll(newlyActive);

                if (!newlyActive.isEmpty()) {
                    chunk.setUnsaved(true);
                }
            }

            @Override
            public void clearActivePockets() {
                activePockets.clear();
            }

            @Override
            public Set<RadPocket> getActivePockets() {

                return activePockets;
            }

            // -------------------------
            // NBT (De)Serialization
            // -------------------------

            @Override
            public CompoundTag serializeNBT() {
                CompoundTag root = new CompoundTag();
                for (int i = 0; i < 24; i++) {
                    SubChunkRadiationStorage sc = subData[i];
                    if (sc == null) continue;

                    CompoundTag subTag = new CompoundTag();
                    // Save pocketsByBlock:
                    IntArrayTag byBlockArr = new IntArrayTag(sc.pocketsByBlock);
                    subTag.put("byBlock", byBlockArr);

                    // Save each RadPocket
                    ListTag pocketListNBT = new ListTag();
                    for (RadPocket p : sc.pockets) {
                        CompoundTag pTag = new CompoundTag();
                        pTag.putFloat("radiation", p.radiation);
                        // Save connectionIndices for each direction
                        for (Direction d : Direction.values()) {
                            IntArrayTag connArr = new IntArrayTag(
                                    p.connectionIndices[d.ordinal()].stream().mapToInt(Integer::intValue).toArray()
                            );
                            pTag.put("conn_" + d.getSerializedName(), connArr);
                        }
                        // Optionally save bounds if desired
                        pTag.putInt("minX", p.minLocalX);
                        pTag.putInt("maxX", p.maxLocalX);
                        pTag.putInt("minY", p.minLocalY);
                        pTag.putInt("maxY", p.maxLocalY);
                        pTag.putInt("minZ", p.minLocalZ);
                        pTag.putInt("maxZ", p.maxLocalZ);

                        pocketListNBT.add(pTag);
                    }
                    subTag.put("pockets", pocketListNBT);
                    root.put("sub_" + i, subTag);
                }
                return root;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                for (int i = 0; i < 24; i++) {
                    String key = "sub_" + i;
                    if (!nbt.contains(key, Tag.TAG_COMPOUND)) continue;
                    CompoundTag subTag = nbt.getCompound(key);

                    SubChunkRadiationStorage sc = new SubChunkRadiationStorage(ChunkRadiationStorage.this, i);
                    // Load pocketsByBlock
                    IntArrayTag arr = new IntArrayTag(subTag.getIntArray("byBlock"));
                    if (arr.size() == 4096) {
                        sc.pocketsByBlock = arr.getAsIntArray();
                    }

                    // Load RadPockets
                    ListTag pocketListNBT = subTag.getList("pockets", Tag.TAG_COMPOUND);
                    sc.pockets = new RadPocket[pocketListNBT.size()];
                    for (int pi = 0; pi < pocketListNBT.size(); pi++) {
                        CompoundTag pTag = pocketListNBT.getCompound(pi);
                        RadPocket p = new RadPocket(sc, pi);
                        p.radiation = pTag.getFloat("radiation");
                        // Load connectionIndices
                        for (Direction d : Direction.values()) {
                            int[] connArr = pTag.getIntArray("conn_" + d.getSerializedName());
                            for (int val : connArr) {
                                p.connectionIndices[d.ordinal()].add(val);
                            }
                        }
                        // Load bounds if needed
                        p.minLocalX = pTag.getInt("minX");
                        p.maxLocalX = pTag.getInt("maxX");
                        p.minLocalY = pTag.getInt("minY");
                        p.maxLocalY = pTag.getInt("maxY");
                        p.minLocalZ = pTag.getInt("minZ");
                        p.maxLocalZ = pTag.getInt("maxZ");

                        sc.pockets[pi] = p;
                    }
                    subData[i] = sc;
                }
            }
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CHUNK_RAD_CAPABILITY) {
                return LazyOptional.of(() -> instance).cast();
            }
            return LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }

    // -----------------------------------
    // RadiationEventHandlers CLASS
    // -----------------------------------

    private static final Set<LevelChunk> loadedChunks = Collections.synchronizedSet(new HashSet<>());

    @Mod.EventBusSubscriber(modid = "hbm", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class RadiationEventHandlers {

        private static final Set<LevelChunk> chunksWithRebuildRequests = Collections.synchronizedSet(new HashSet<>());

        private static final ResourceLocation RADIATION_CAP_KEY = new ResourceLocation("hbm", "chunk_radiation");

        @SubscribeEvent
        public static void attachChunkRadiation(AttachCapabilitiesEvent<LevelChunk> event) {
            LevelChunk chunk = event.getObject();
            ChunkRadiationStorage storage = new ChunkRadiationStorage(chunk);
            event.addCapability(RADIATION_CAP_KEY, storage);
        }

        private static boolean inViewDistance(ChunkPos pos1, ChunkPos pos2, ServerLevel level) {

            int abs1 = Mth.abs(pos1.x - pos2.x);
            int abs2 = Mth.abs(pos1.z - pos2.z);

            int range = level.getServer().getPlayerList().getViewDistance() + 2;

            boolean bool1 = abs1 <= range;
            boolean bool2 = abs2 <= range;

            boolean result = bool1 && bool2;

            System.err.println("dist from chunk " + pos1 + " to player chunk " + pos2 + " is [" + abs1 + ", " + abs2 + "] within range [" + range + "] returns values of [" + bool1 + ", " + bool2 + "] combined to [" + result + "]");
            return result;
        }

        private static boolean inDistance(ChunkPos pos1, ChunkPos pos2) {

            int dx = pos1.x - pos2.x;
            int dz = pos1.z - pos2.z;

            int range = 6;
            int rangeSq = range * range;

            boolean withinCircle = (dx * dx + dz * dz) <= rangeSq;

            if(withinCircle)
                System.out.println("dist from chunk " + pos1 + " to player chunk " + pos2 + " is [" + Mth.sqrt(dx * dx + dz * dz) + "] within range [" + range + "] returns value of [true]");
            return withinCircle;
        }

        @SubscribeEvent
        public static void onChunkLoad(ChunkEvent.Load event) {
            if (!(event.getLevel() instanceof ServerLevel world)) return;
            LevelChunk chunk = (LevelChunk) event.getChunk();
            System.err.println("Chunk load called for chunk at " + chunk.getPos());
            if(event.getLevel().players().isEmpty())
                chunksWithRebuildRequests.add(chunk);

            for(Player player : event.getLevel().players()) {

                ChunkPos pPos = new ChunkPos(player.getOnPos());

                if(inViewDistance(chunk.getPos(), pPos, world)) {

                    System.err.println("Loaded chunk at " + chunk.getPos() + " is in range. Adding to Rebuild pockets request list...");
                    chunksWithRebuildRequests.add(chunk);
                }
            }

            loadedChunks.add(chunk);
        }

        @SubscribeEvent
        public static void onChunkUnload(ChunkEvent.Unload event) {
            if (!(event.getLevel() instanceof ServerLevel world)) return;
            LevelChunk chunk = (LevelChunk) event.getChunk();
            chunk.getCapability(CHUNK_RAD_CAPABILITY).ifPresent(IChunkRadiation::clearActivePockets);

            loadedChunks.remove(chunk);
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent evt) {
            if (evt.phase != TickEvent.Phase.END) return;

            var server = ServerLifecycleHooks.getCurrentServer();
            if(server == null) return;

            Set<LevelChunk> snapshot1;
            synchronized (loadedChunks) {
                snapshot1 = loadedChunks;
            }

            for (LevelChunk chunk : snapshot1) {
                // Only tick radiation on fully loaded chunks in a ServerLevel
                if (chunk.getLevel() instanceof ServerLevel world) {
                    chunk.getCapability(CHUNK_RAD_CAPABILITY).ifPresent(
                            cap -> cap.tickSpreadAndDecay(world, chunk)
                    );
                }
            }

            Set<LevelChunk> snapshot2;
            synchronized (chunksWithRebuildRequests) {
                snapshot2 = new HashSet<>(chunksWithRebuildRequests);
            }

            Set<LevelChunk> builtChunks = Collections.synchronizedSet(new HashSet<>());

            try{

                for(LevelChunk chunk : snapshot2) {

                    if (chunk.getLevel() instanceof ServerLevel world) {

                        for(Player player : evt.getServer().getPlayerList().getPlayers()) {

                            if(inDistance(chunk.getPos(), new ChunkPos(player.getOnPos()))) {

//                                if(!chunksWithRebuildRequests.contains(chunk))
//                                    break;
                                System.out.println("Chunk at " + chunk.getPos() + " is within rebuild distance.");
                                chunk.getCapability(CHUNK_RAD_CAPABILITY).ifPresent(cap -> {
                                    System.err.println("Attempting to rebuild pockets now...");
                                    cap.rebuildAllPockets(world, chunk);
                                });
                                builtChunks.add(chunk);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ignored) {

                System.out.println("Concurrent modification exception!");
            }
            chunksWithRebuildRequests.removeAll(builtChunks);
        }

        @SubscribeEvent
        public static void onBlockBreak(BlockEvent.BreakEvent event) {
            if (!(event.getLevel() instanceof ServerLevel world)) return;
            BlockPos pos = event.getPos();
            LevelChunk chunk = (LevelChunk) world.getChunk(pos);
            chunksWithRebuildRequests.add(chunk);
        }

        @SubscribeEvent
        public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
            if (!(event.getLevel() instanceof ServerLevel world)) return;
            BlockPos pos = event.getPos();
            LevelChunk chunk = (LevelChunk) world.getChunk(pos);
            chunksWithRebuildRequests.add(chunk);
        }
    }
}
