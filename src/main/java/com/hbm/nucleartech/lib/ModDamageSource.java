package com.hbm.nucleartech.lib;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSource {

    public static final ResourceKey<DamageType> RADIATION = register("radiation");

    private static ResourceKey<DamageType> register(String name)
    {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, name));
    }

}
