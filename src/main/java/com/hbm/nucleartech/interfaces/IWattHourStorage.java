package com.hbm.nucleartech.interfaces;

import com.hbm.nucleartech.util.FloatingLong;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IWattHourStorage {

    String formatWattHoursStored();
    String formatMaxWattHoursStored();

    String formatWattsCharge();
    String formatWattsDischarge();

    FloatingLong receiveWattage(FloatingLong maxReceive, boolean simulate);
    FloatingLong extractWattage(FloatingLong maxExtract, boolean simulate);

    FloatingLong getWattageStored();
    void setWattageStored(FloatingLong set);

    FloatingLong getMaxWattageStored();
    void setMaxWattageStored(FloatingLong set);

    boolean canExtract();
    boolean canReceive();
}
