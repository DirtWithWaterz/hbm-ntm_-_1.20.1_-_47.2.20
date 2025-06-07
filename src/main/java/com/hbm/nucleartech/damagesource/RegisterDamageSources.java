package com.hbm.nucleartech.damagesource;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;

public class RegisterDamageSources {

    public static final DamageSource RADIATION_DAMAGE;
    public static final DamageSource NUCLEAR_BLAST_DAMAGE;
    public static final DamageSource BLAST_DAMAGE;

    static {

        assert Minecraft.getInstance().level != null;
        RADIATION_DAMAGE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.RADIATION
        ));
        NUCLEAR_BLAST_DAMAGE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.NUCLEAR_BLAST
        ));
        BLAST_DAMAGE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BLAST
        ));
    }
}
