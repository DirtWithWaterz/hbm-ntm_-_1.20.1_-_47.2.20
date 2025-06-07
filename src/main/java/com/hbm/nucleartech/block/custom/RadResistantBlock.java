package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.interfaces.IRadResistantBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;

public class RadResistantBlock extends Block implements IRadResistantBlock {

    public RadResistantBlock(Properties pProperties) {
        super(pProperties);
    }

    public static final TagKey<Block> RAD_RESISTANT_BLOCKS =
            BlockTags.create(new ResourceLocation(HBM.MOD_ID, "rad_resistant_blocks"));

    @Override
    public boolean isRadResistant(ServerLevel level, BlockPos pos) {

        BlockState state = level.getBlockState(pos);
        return state.is(RAD_RESISTANT_BLOCKS);
    }

    @Override
    public int getResistance() {

        return 1;
    }

    public static void register(IEventBus eventBus) {

        eventBus.register(RAD_RESISTANT_BLOCKS);
    }
}
