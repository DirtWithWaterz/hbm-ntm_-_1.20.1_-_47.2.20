package com.hbm.nucleartech.damagesource;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public interface HbmDamageTypes {

    ResourceKey<DamageType> RADIATION = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "radiation"));
    ResourceKey<DamageType> NUCLEAR_BLAST = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "nuclear_blast"));
    ResourceKey<DamageType> BLAST = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "blast"));
}
