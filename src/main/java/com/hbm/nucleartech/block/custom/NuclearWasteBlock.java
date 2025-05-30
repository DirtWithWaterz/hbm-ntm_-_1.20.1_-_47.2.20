package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.hazard.HazardBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NuclearWasteBlock extends HazardBlock {
    public NuclearWasteBlock(Properties pProperties) {
        super(pProperties, 0);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        Direction dir = Direction.values()[pRandom.nextInt(6)];

        if(pRandom.nextInt(2) == 0 && pLevel.getBlockState(new BlockPos(pPos.getX() + dir.getStepX(), pPos.getY() + dir.getStepY(), pPos.getZ() + dir.getStepZ())) == Blocks.AIR.defaultBlockState()) {

        }
        super.tick(pState, pLevel, pPos, pRandom);
    }
}
