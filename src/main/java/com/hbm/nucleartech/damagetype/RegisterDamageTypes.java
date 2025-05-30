package com.hbm.nucleartech.damagetype;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterDamageTypes {
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, HBM.MOD_ID);

//    public static final RegistryObject<DamageType> RADIATION_DAMAGE =
//            DAMAGE_TYPES.register("radiation_damage", () ->
//                    new DamageType("death.attack.radiation_damage", 0.1F));
//    public static final RegistryObject<DamageType> NUCLEAR_BLAST_DAMAGE =
//            DAMAGE_TYPES.register("nuclear_blast_damage", () ->
//                    new DamageType("death.attack.nuclear_blast_damage", 0.1F));
//    public static final RegistryObject<DamageType> BLAST_DAMAGE =
//            DAMAGE_TYPES.register("blast_damage", () ->
//                    new DamageType("death.attack.blast_damage", 0.1F));

    public static void register(IEventBus eventBus){

        DAMAGE_TYPES.register(eventBus);
    }
}
