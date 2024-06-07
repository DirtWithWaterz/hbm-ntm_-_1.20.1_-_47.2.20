package com.hbm.nucleartech.saveddata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Properties;

public class RadiationSavedData extends SavedData {


    private static RadiationSavedData openInstance;

    public Level worldObj;



    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        return null;
    }

//    public static RadiationSavedData getData(Level worldObj) {
//
//        if(openInstance != null && openInstance.worldObj == worldObj)
//            return openInstance;
//
//        RadiationSavedData data = (RadiationSavedData)worldObj
//    }


}
