package com.hbm.nucleartech.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public interface IRadResistantBlock {

    boolean isRadResistant(ServerLevel level, BlockPos pos);

    int getResistance();
}
