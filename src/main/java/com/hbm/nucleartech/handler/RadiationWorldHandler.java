package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RadiationWorldHandler {

    public static void handleWorldDestruction(Level level) {
        if (!(level instanceof ServerLevel world)) return;

        Collection<RadiationSystemChunksNT.RadPocket> active = RadiationSystemChunksNT.getAllActivePockets();
        if (active.isEmpty()) return;

        int threshold = 5;
        RadiationSystemChunksNT.RadPocket pocket = pickRandomAboveThreshold(active, world, threshold);
        if (pocket == null) return;

        Set<RadiationSystemChunksNT.RadPocket> graph = RadiationSystemChunksNT.RadPocket.floodConnected(pocket, world);
        if(graph == null) {

            graph = new HashSet<>();
            graph.add(pocket);
        }
        for (RadiationSystemChunksNT.RadPocket p : graph) {
            // for each pocket, iterate its subchunk and flat‐index map, etc.
            // grab the pocket’s subchunk storage:
            RadiationSystemChunksNT.SubChunkRadiationStorage sc = p.parent;
            int[] map = sc.pocketsByBlock;

            // compute the world-origin of this subchunk:
            int chunkX = sc.parentChunk.chunk.getPos().x;
            int chunkZ = sc.parentChunk.chunk.getPos().z;
            int baseY   = (sc.yIndex << 4) - 64;

            // iterate every local block in the 16×16×16 subchunk:
            for (int flat = 0; flat < map.length; flat++) {
                if (map[flat] != p.index) continue;  // only blocks in *this* pocket

                // decode flat → local coords
                int localX =  flat        & 0xF;          // bits 0–3
                int localZ = (flat >> 4)  & 0xF;          // bits 4–7
                int localY = (flat >> 8)  & 0xF;          // bits 8–11

                // compute the global BlockPos
                BlockPos pos = new BlockPos(
                        (chunkX << 4) + localX,
                        baseY   + localY,
                        (chunkZ << 4) + localZ
                );

                BlockState state = world.getBlockState(pos);
                Block b = state.getBlock();
                if (state.isAir()) continue;

                // now your “waste effect” replacements:
                if (b == Blocks.GRASS_BLOCK) {
                    world.setBlock(pos, RegisterBlocks.BLOCK_TITANIUM.get().defaultBlockState(), 2);
                }
                // …etc.
            }
        }
    }

    // helper to choose a random pocket above threshold:
    private static RadiationSystemChunksNT.RadPocket pickRandomAboveThreshold(Collection<RadiationSystemChunksNT.RadPocket> pockets, ServerLevel w, int th) {
        List<RadiationSystemChunksNT.RadPocket> good = pockets.stream()
                .filter(r -> r.radiation >= th)
                .collect(Collectors.toList());
        if (good.isEmpty()) return null;
        return good.get(w.random.nextInt(good.size()));
    }

}
