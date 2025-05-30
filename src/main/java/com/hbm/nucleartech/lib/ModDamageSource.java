package com.hbm.nucleartech.lib;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageSource {

    public static DamageSource radiationDamage(ServerLevel level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> holder = registry.getHolderOrThrow(
                ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "radiation_damage"))
        );
        return new DamageSource(holder);
    }
    public static DamageSource nuclearBlastDamage(ServerLevel level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> holder = registry.getHolderOrThrow(
                ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "nuclear_blast_damage"))
        );
        return new DamageSource(holder);
    }
    public static DamageSource BlastDamage(ServerLevel level) {
        Registry<DamageType> registry = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        Holder<DamageType> holder = registry.getHolderOrThrow(
                ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(HBM.MOD_ID, "blast_damage"))
        );
        return new DamageSource(holder);
    }

}
