package com.hbm.nucleartech.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IRadResistantBlock {

    boolean isRadResistant(Level level, BlockPos pos);

    int getResistance();
}
