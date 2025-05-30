package com.hbm.nucleartech.handler;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.saveddata.RadiationSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
//import com.hbm.nucleartech.handler.RadiationSystemNT.RadPocket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collection;

public class RadiationWorldHandler {

//    public static void handleWorldDestruction(Level pLevel) {
//        if(!(pLevel instanceof ServerLevel))
//            return;
//        if(false) // !RadiationConfig.worldRadEffects || !GeneralConfig.enableRads
//            return;
//
//        int count = 50; // worldRad
//        int threshold = 5; // worldRadThreshold
//
//        if(true) { // if GeneralConfig.advancedRadiation
//
//            Collection<RadPocket> activePockets = RadiationSystemNT.getActiveCollection(pLevel);
//            if(activePockets.size() == 0)
//                return;
//            int randIdx = pLevel.random.nextInt(activePockets.size());
//            int itr = 0;
//            for(RadPocket p : activePockets){
//                if(itr == randIdx){
//                    if(p.radiation < threshold)
//                        return;
//                    BlockPos startPos = p.getSubChunkPos();
//                    RadPocket[] pocketsByBlock = p.parent.pocketsByBlock;
//
//                    for(int i = 0; i < 16; i++){
//                        for(int j = 0; j < 16; j++){
//                            for(int k = 0; k < 16; k++){
//                                if(pLevel.random.nextInt(3) != 0)
//                                    continue;
//                                if(pocketsByBlock != null && pocketsByBlock[i*16*16+j*16+k] != p)
//                                    continue;
//                                BlockPos pos = startPos.offset(i, j, k);
//                                BlockState b = pLevel.getBlockState(pos);
//                                Block block = b.getBlock();
//
//                                if(!pLevel.isEmptyBlock(pos)){
//                                    if(block == Blocks.GRASS){
//                                        pLevel.setBlock(pos, RegisterBlocks.BLOCK_TITANIUM.get().defaultBlockState(), 2); // change to waste_grass
//                                    }
//                                    // vvv other blocks that need to be replaced by waste effects go here. vvv
//
//                                    // ^^^                                                                 ^^^
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//                itr++;
//            }
//            return;
//        }
//
//        // a bunch of non 3d radiation code goes here
//    }
}
