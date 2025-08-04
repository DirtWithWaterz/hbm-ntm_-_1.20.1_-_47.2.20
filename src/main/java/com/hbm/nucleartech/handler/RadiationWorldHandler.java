package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT.ChunkStorageCompat;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT.ChunkStorageCompat.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;
import java.util.List;

public class RadiationWorldHandler {

    public static void handleWorldDestruction(Level level) {
        if (!(level instanceof ServerLevel world)) return;

//        System.err.println("[Debug] Handle world destruction called");

        Collection<RadiationSystemChunksNT.RadPocket> active = RadiationSystemChunksNT.getActivePockets();
        if (active.isEmpty()) return;

        int threshold = 5;
        RadiationSystemChunksNT.RadPocket pocket = pickRandomAboveThreshold(active, world, threshold);
        if (pocket == null) return;

        // grab the pocket’s subchunk storage:
        RadiationSystemChunksNT.SubChunkRadiationStorage sc = pocket.parent;

        ExtendedBlockStorage storage = ChunkStorageCompat.getBlockStorageArray(sc.parentChunk.chunk)[ChunkStorageCompat.getIndexFromWorldY(sc.yLevel)];

        BlockPos subChunkWorldPos = sc.parentChunk.getWorldPos(sc.yLevel);

        for(int x = 0; x < 16; x++) {

            for(int y = 0; y < 16; y++) {

                for(int z = 0; z < 16; z++) {

                    BlockState block = storage.get(x, y, z);
                    if(block.isAir())
                        continue;

                    if(sc.getPocket(subChunkWorldPos.offset(x, y, z)) != pocket)
                        continue;

                    if(level.random.nextInt(100) <= 79)
                        continue;

//                    System.err.println("[Debug] Random succeeded, placing block if grass... " + block.getBlock().getName().getString());

                    if(block.getBlock() == Blocks.GRASS_BLOCK) {

                        BlockPos worldPos = subChunkWorldPos.offset(x, y, z);
                        level.setBlock(worldPos, RegisterBlocks.DEAD_GRASS.get().defaultBlockState(), 18);
                    }
                }
            }
        }

    }

    // helper to choose a random pocket above the threshold:
    private static RadiationSystemChunksNT.RadPocket pickRandomAboveThreshold(Collection<RadiationSystemChunksNT.RadPocket> pockets, ServerLevel w, int th) {
        List<RadiationSystemChunksNT.RadPocket> good = pockets.stream()
                .filter(r -> r.radiation >= th)
                .toList();
        if (good.isEmpty()) return null;
        return good.get(w.random.nextInt(good.size()));
    }

}
