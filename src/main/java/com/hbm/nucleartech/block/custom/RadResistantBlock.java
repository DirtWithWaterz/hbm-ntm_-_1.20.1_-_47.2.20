package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT;
import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;

@SuppressWarnings("NonAsciiCharacters")
public class RadResistantBlock extends Block implements IRadResistantBlock {

    float μM;
    float ρ;

    public float μ;

    public RadResistantBlock(Properties pProperties, float ρ, float μM) {
        super(pProperties);

        this.ρ = ρ;
        this.μM = μM;

        this.μ = μM * ρ * 100F;
    }


    @Override
    public boolean isRadResistant(Level level, BlockPos pos) {

        Block block = level.getBlockState(pos).getBlock();
        return block instanceof IRadResistantBlock;
    }

    @Override
    public int getResistance() {

        return 1;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);

        System.out.println("[Debug] " + pState.getBlock().getName() + " placed at " + pPos + ", marking position for rebuild");
        RadiationSystemChunksNT.RadiationEventHandlers.markChunkForRebuild(pLevel, pPos);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);

        System.out.println("[Debug] " + pState.getBlock().getName() + " removed at " + pPos + ", marking position for rebuild");
        RadiationSystemChunksNT.RadiationEventHandlers.markChunkForRebuild(pLevel, pPos);
    }
}
