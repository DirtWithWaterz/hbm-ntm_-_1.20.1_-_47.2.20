package com.hbm.nucleartech.damagesource;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

public class RegisterDamageSources {

    public static final DamageSource BLAST;
    public static final DamageSource NUCLEAR_BLAST;
    public static final DamageSource MUD_POISONING;
    public static final DamageSource ACID;
    public static final DamageSource EUTHANIZED_SELF;
    public static final DamageSource EUTHANIZED_SELF_2;
    public static final DamageSource TAU_BLAST;
    public static final DamageSource RADIATION;
    public static final DamageSource DIGAMMA;
    public static final DamageSource SUICIDE;
    public static final DamageSource TELEPORTER;
    public static final DamageSource CHEATER;
    public static final DamageSource RUBBLE;
    public static final DamageSource SHRAPNEL;
    public static final DamageSource BLACK_HOLE;
    public static final DamageSource BLENDER;
    public static final DamageSource METEORITE;
    public static final DamageSource BOXCAR;
    public static final DamageSource BOAT;
    public static final DamageSource BUILDING;
    public static final DamageSource TAINT;
    public static final DamageSource AMS;
    public static final DamageSource AMS_CORE;
    public static final DamageSource BROADCAST;
    public static final DamageSource BANG;
    public static final DamageSource PC;
    public static final DamageSource CLOUD;
    public static final DamageSource LEAD;
    public static final DamageSource ENERVATION;
    public static final DamageSource ELECTRICITY;
    public static final DamageSource EXHAUST;
    public static final DamageSource SPIKES;
    public static final DamageSource LUNAR;
    public static final DamageSource MONOXIDE;
    public static final DamageSource ASBESTOS;
    public static final DamageSource BLACKLUNG;
    public static final DamageSource MKU;
    public static final DamageSource VACUUM;
    public static final DamageSource OVERDOSE;
    public static final DamageSource MICROWAVE;
    public static final DamageSource NITAN;

    private static final DamageSource REVOLVER_BULLET;
    private static final DamageSource CHOPPER_BULLET ;
    private static final DamageSource TAU;
    private static final DamageSource CMB;
    private static final DamageSource SUB_ATOMIC;
    private static final DamageSource EUTHANIZED;
    public static final DamageSource ELECTRIFIED;
    public static final DamageSource FLAMETHROWER;
    public static final DamageSource PLASMA;
    public static final DamageSource ICE;
//    private static final DamageSource LASER;
    public static final DamageSource BOIL;
    public static final DamageSource ACID_PLAYER;

    /**
     * Creates a DamageSource for the given damage type key.
     * This is safe to call on both client and server.
     */
    @NotNull
    private static DamageSource createDamageSource(ResourceKey<DamageType> damageTypeKey) {
        try {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            if (server != null) {
                return new DamageSource(server.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey));
            }
        } catch (Exception e) {
            // Fall through to direct creation
        }
        
        // Create a temporary damage type that will be replaced when the registry is available
        DamageType tempType = new DamageType("hbm." + damageTypeKey.location().getPath(), 0.1F);
        return new DamageSource(Holder.direct(tempType));
    }
    
    static {
        // Initialize all damage sources using the safe method
        BLAST = createDamageSource(HbmDamageTypes.BLAST);
        NUCLEAR_BLAST = createDamageSource(HbmDamageTypes.NUCLEAR_BLAST);
        MUD_POISONING = createDamageSource(HbmDamageTypes.MUD_POISONING);
        ACID = createDamageSource(HbmDamageTypes.ACID);
        EUTHANIZED_SELF = createDamageSource(HbmDamageTypes.EUTHANIZED_SELF);
        EUTHANIZED_SELF_2 = createDamageSource(HbmDamageTypes.EUTHANIZED_SELF_2);
        TAU_BLAST = createDamageSource(HbmDamageTypes.TAU_BLAST);
        RADIATION = createDamageSource(HbmDamageTypes.RADIATION);
        DIGAMMA = createDamageSource(HbmDamageTypes.DIGAMMA);
        SUICIDE = createDamageSource(HbmDamageTypes.SUICIDE);
        TELEPORTER = createDamageSource(HbmDamageTypes.TELEPORTER);
        CHEATER = createDamageSource(HbmDamageTypes.CHEATER);
        RUBBLE = createDamageSource(HbmDamageTypes.RUBBLE);
        SHRAPNEL = createDamageSource(HbmDamageTypes.SHRAPNEL);
        BLACK_HOLE = createDamageSource(HbmDamageTypes.BLACK_HOLE);
        BLENDER = createDamageSource(HbmDamageTypes.BLENDER);
        METEORITE = createDamageSource(HbmDamageTypes.METEORITE);
        BOXCAR = createDamageSource(HbmDamageTypes.BOXCAR);
        BOAT = createDamageSource(HbmDamageTypes.BOAT);
        BUILDING = createDamageSource(HbmDamageTypes.BUILDING);
        TAINT = createDamageSource(HbmDamageTypes.TAINT);
        AMS = createDamageSource(HbmDamageTypes.AMS);
        AMS_CORE = createDamageSource(HbmDamageTypes.AMS_CORE);
        BROADCAST = createDamageSource(HbmDamageTypes.BROADCAST);
        BANG = createDamageSource(HbmDamageTypes.BANG);
        PC = createDamageSource(HbmDamageTypes.PC);
        CLOUD = createDamageSource(HbmDamageTypes.CLOUD);
        LEAD = createDamageSource(HbmDamageTypes.LEAD);
        ENERVATION = createDamageSource(HbmDamageTypes.ENERVATION);
        ELECTRICITY = createDamageSource(HbmDamageTypes.ELECTRICITY);
        EXHAUST = createDamageSource(HbmDamageTypes.EXHAUST);
        SPIKES = createDamageSource(HbmDamageTypes.SPIKES);
        LUNAR = createDamageSource(HbmDamageTypes.LUNAR);
        MONOXIDE = createDamageSource(HbmDamageTypes.MONOXIDE);
        ASBESTOS = createDamageSource(HbmDamageTypes.ASBESTOS);
        BLACKLUNG = createDamageSource(HbmDamageTypes.BLACKLUNG);
        MKU = createDamageSource(HbmDamageTypes.MKU);
        VACUUM = createDamageSource(HbmDamageTypes.VACUUM);
        OVERDOSE = createDamageSource(HbmDamageTypes.OVERDOSE);
        MICROWAVE = createDamageSource(HbmDamageTypes.MICROWAVE);
        NITAN = createDamageSource(HbmDamageTypes.NITAN);
        REVOLVER_BULLET = createDamageSource(HbmDamageTypes.REVOLVER_BULLET);
        CHOPPER_BULLET = createDamageSource(HbmDamageTypes.CHOPPER_BULLET);
        TAU = createDamageSource(HbmDamageTypes.TAU);
        CMB = createDamageSource(HbmDamageTypes.CMB);
        SUB_ATOMIC = createDamageSource(HbmDamageTypes.SUB_ATOMIC);
        EUTHANIZED = createDamageSource(HbmDamageTypes.EUTHANIZED);
        ELECTRIFIED = createDamageSource(HbmDamageTypes.ELECTRIFIED);
        FLAMETHROWER = createDamageSource(HbmDamageTypes.FLAMETHROWER);
        PLASMA = createDamageSource(HbmDamageTypes.PLASMA);
        ICE = createDamageSource(HbmDamageTypes.ICE);
        // LASER = createDamageSource(HbmDamageTypes.LASER);
        BOIL = createDamageSource(HbmDamageTypes.BOIL);
        ACID_PLAYER = createDamageSource(HbmDamageTypes.ACID_PLAYER);
    }

    public static DamageSource causeBulletDamage(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(REVOLVER_BULLET.typeHolder(), hit, ent);
    }

    public static DamageSource causeDisplacementDamage(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(CHOPPER_BULLET.typeHolder(), hit, ent);
    }

    public static DamageSource causeTauDamage(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(TAU.typeHolder(), hit, ent);
    }

    public static DamageSource causeCombineDamage(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(CMB.typeHolder(), hit, ent);
    }

    public static DamageSource causeSubatomicDamage(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(SUB_ATOMIC.typeHolder(), hit, ent);
    }

    public static DamageSource euthanized(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(EUTHANIZED.typeHolder(), hit, ent);
    }

//    public static DamageSource causeLaserDamage(LaserBeamEntity ent, LivingEntity hit) {
//
//        return new DamageSource(LASER.typeHolder(), hit, ent);
//    }

//    public static DamageSource causeLaserDamage(MinerBeamEntity ent, LivingEntity hit) {
//
//        return new DamageSource(LASER.typeHolder(), hit, ent);
//    }

    public static DamageSource ELECTRIFIED(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(ELECTRIFIED.typeHolder(), hit, ent);
    }
    public static DamageSource FLAMETHROWER(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(FLAMETHROWER.typeHolder(), hit, ent);
    }
    public static DamageSource PLASMA(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(PLASMA.typeHolder(), hit, ent);
    }
    public static DamageSource ICE(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(ICE.typeHolder(), hit, ent);
    }
    public static DamageSource BOIL(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(BOIL.typeHolder(), hit, ent);
    }
    public static DamageSource ACID_PLAYER(LivingEntity ent, LivingEntity hit) {

        return new DamageSource(ACID_PLAYER.typeHolder(), hit, ent);
    }

}
