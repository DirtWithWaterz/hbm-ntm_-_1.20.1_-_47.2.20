package com.hbm.nucleartech.damagesource;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

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

    static {

        assert Minecraft.getInstance().level != null;

        BLAST = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BLAST
        ));
        NUCLEAR_BLAST = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.NUCLEAR_BLAST
        ));
        MUD_POISONING = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.MUD_POISONING
        ));
        ACID = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ACID
        ));
        EUTHANIZED_SELF = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.EUTHANIZED_SELF
        ));
        EUTHANIZED_SELF_2 = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.EUTHANIZED_SELF_2
        ));
        TAU_BLAST = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.TAU_BLAST
        ));
        RADIATION = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.RADIATION
        ));
        DIGAMMA = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.DIGAMMA
        ));
        SUICIDE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.SUICIDE
        ));
        TELEPORTER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.TELEPORTER
        ));
        CHEATER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.CHEATER
        ));
        RUBBLE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.RUBBLE
        ));
        SHRAPNEL = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.SHRAPNEL
        ));
        BLACK_HOLE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BLACK_HOLE
        ));
        BLENDER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BLENDER
        ));
        METEORITE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.METEORITE
        ));
        BOXCAR = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BOXCAR
        ));
        BOAT = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BOAT
        ));
        BUILDING = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BUILDING
        ));
        TAINT = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.TAINT
        ));
        AMS = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.AMS
        ));
        AMS_CORE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.AMS_CORE
        ));
        BROADCAST = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BROADCAST
        ));
        BANG = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BANG
        ));
        PC = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.PC
        ));
        CLOUD = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.CLOUD
        ));
        LEAD = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.LEAD
        ));
        ENERVATION = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ENERVATION
        ));
        ELECTRICITY = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ELECTRICITY
        ));
        EXHAUST = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.EXHAUST
        ));
        SPIKES = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.SPIKES
        ));
        LUNAR = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.LUNAR
        ));
        MONOXIDE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.MONOXIDE
        ));
        ASBESTOS = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ASBESTOS
        ));
        BLACKLUNG = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BLACKLUNG
        ));
        MKU = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.MKU
        ));
        VACUUM = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.VACUUM
        ));
        OVERDOSE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.OVERDOSE
        ));
        MICROWAVE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.MICROWAVE
        ));
        NITAN = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.NITAN
        ));


        REVOLVER_BULLET = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.REVOLVER_BULLET
        ));
        CHOPPER_BULLET = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.CHOPPER_BULLET
        ));
        TAU = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.TAU
        ));
        CMB = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.CMB
        ));
        SUB_ATOMIC = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.SUB_ATOMIC
        ));
        EUTHANIZED = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.EUTHANIZED
        ));
//        LASER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(
//
//                HbmDamageTypes.LASER
//        ));
        ELECTRIFIED = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ELECTRIFIED
        ));
        FLAMETHROWER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.FLAMETHROWER
        ));
        PLASMA = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.PLASMA
        ));
        ICE = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ICE
        ));
        BOIL = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.BOIL
        ));
        ACID_PLAYER = new DamageSource(Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(

                HbmDamageTypes.ACID_PLAYER
        ));
    }

//    public static DamageSource causeBulletDamage(BulletEntity ent, LivingEntity hit) {
//
//        return new DamageSource(REVOLVER_BULLET.typeHolder(), hit, ent);
//    }

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
