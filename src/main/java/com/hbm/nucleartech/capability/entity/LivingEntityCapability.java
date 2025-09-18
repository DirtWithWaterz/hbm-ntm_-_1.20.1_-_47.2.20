package com.hbm.nucleartech.capability.entity;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.capability.HbmCapabilities.LivingCapabilitiesSyncMessage;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LivingEntityCapability implements IEntityCapabilityBase {

    public static final UUID digamma_UUID = UUID.fromString("2a3d8aec-5ab9-4218-9b8b-ca812bdf378b");

    /// Values ///
    private float oldMaxHealth = 20;
    private int oldRoundedDamage = -1;
    private double internalDamage = 0;
    private double permanentContamination = 0;
    private float radiation = 0;
    private float neutron = 0;
    private float digamma = 0;
    private int asbestos = 0;
    public static final int maxAsbestos = 60 * 60 * 20;
    private int blacklung = 0;
    public static final int maxBlacklung = 2 * 60 * 60 * 20;
    private float radEnv = 0;
    private float radBuf = 0;
    private int bombTimer = 0;
    private int contagion = 0;
    private int oil = 0;
    private int fire = 0;
    private int phosphorus = 0;
    private int balefire = 0;
    private List<ContaminationEffect> contamination = new ArrayList<>();

    public void syncLivingVariables(Entity entity) {

        if(entity instanceof ServerPlayer serverPlayer)
            HBM.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new LivingCapabilitiesSyncMessage(this));
    }

    @Override
    public double getValue(Type type) {

        switch(type) {

            case OLD_MAX_HEALTH -> {
                return oldMaxHealth;
            }
            case OLD_ROUNDED_DAMAGE -> {
                return oldRoundedDamage;
            }
            case PERMANENT_CONTAMINATION -> {
                return permanentContamination;
            }
            case INTERNAL_DAMAGE -> {
                return internalDamage;
            }
            case RADIATION -> {
                return radiation;
            }
            case NEUTRON -> {
                return neutron;
            }
            case DIGAMMA -> {
                return digamma;
            }
            case ASBESTOS -> {
                return asbestos;
            }
            case BLACKLUNG -> {
                return blacklung;
            }
            case RADENV -> {
                return radEnv;
            }
            case RADBUF -> {
                return radBuf;
            }
            case BOMB_TIMER -> {
                return bombTimer;
            }
            case CONTAGION -> {
                return contagion;
            }
            case OIL -> {
                return oil;
            }
            case FIRE -> {
                return fire;
            }
            case PHOSPHORUS -> {
                return phosphorus;
            }
            case BALEFIRE -> {
                return balefire;
            }
            default -> {
                return -1F;
            }
        }
    }

    @Override
    public List<ContaminationEffect> getValue() {
        return contamination;
    }

    @Override
    public void setValue(Type type, double value) {

        switch(type) {

            case OLD_MAX_HEALTH -> {

                oldMaxHealth = (float)value;
                break;
            }
            case OLD_ROUNDED_DAMAGE -> {

                oldRoundedDamage = (int)value;
                break;
            }
            case PERMANENT_CONTAMINATION -> {

                permanentContamination = value;
                break;
            }
            case INTERNAL_DAMAGE -> {

                internalDamage = value;
                break;
            }
            case RADIATION -> {

                radiation = (float)value;
                break;
            }
            case NEUTRON -> {

                neutron = (float)value;
                break;
            }
            case DIGAMMA -> {

                digamma = (float)value;
                break;
            }
            case ASBESTOS -> {

                asbestos = Mth.floor(value);
                break;
            }
            case BLACKLUNG -> {

                blacklung = Mth.floor(value);
                break;
            }
            case RADENV -> {

                radEnv = (float)value;
                break;
            }
            case RADBUF -> {

                radBuf = (float)value;
                break;
            }
            case BOMB_TIMER -> {

                bombTimer = Mth.floor(value);
                break;
            }
            case CONTAGION -> {

                contagion = Mth.floor(value);
                break;
            }
            case OIL -> {

                oil = Mth.floor(value);
                break;
            }
            case FIRE -> {

                fire = Mth.floor(value);
                break;
            }
            case PHOSPHORUS -> {

                phosphorus = Mth.floor(value);
                break;
            }
            case BALEFIRE -> {

                balefire = Mth.floor(value);
                break;
            }
            default -> {


                break;
            }
        }
    }

    @Override
    public void setValue(List<ContaminationEffect> contaminations) {

        contamination.clear();
        contamination.addAll(contaminations);
    }

    @Override
    public void addValue(Type type, double value) {

        switch(type) {

            case OLD_MAX_HEALTH -> {

                float hp = oldMaxHealth + (float)value;

                setValue(type, hp);
            }
            case OLD_ROUNDED_DAMAGE -> {

                int val = oldRoundedDamage + (int)value;

                setValue(type, val);
            }
            case PERMANENT_CONTAMINATION -> {

                double perm = permanentContamination + value;

                perm = Mth.clamp(perm, 0f, 1000f);

                setValue(type, perm);
            }
            case INTERNAL_DAMAGE -> {

                double dam = internalDamage + value;

                dam = Mth.clamp(dam, 0f, 100f);

                setValue(type, dam);
            }
            case RADIATION -> {

                float rad = radiation + (float)value;

//                rad = Mth.clamp(rad, 0F, 2500F);

                setValue(type, rad);
            }
            case NEUTRON -> {

                float rad = neutron + (float)value;

//                rad = Mth.clamp(rad, 0F, 2500F);

                setValue(type, rad);
            }
            case DIGAMMA -> {

                float dRad = digamma + (float)value;

                dRad = Mth.clamp(dRad, 0, 10);

                setValue(type, dRad);
            }
            case ASBESTOS -> {

                setValue(type, Mth.clamp(asbestos + value, 0, maxAsbestos));

                // send player inform packet
            }
            case BLACKLUNG -> {

                setValue(type, Mth.clamp(blacklung + value, 0, maxBlacklung));

                // send player inform packet
            }
            case RADENV -> {

                float radE = radEnv + (float)value;

                radE = Mth.clamp(radE, 0, 2500F);

                setValue(type, radE);
            }
            case RADBUF -> {

                float e = radBuf + (float)value;

                e = Mth.clamp(e, 0, 2500F);

                setValue(type, e);
            }
            case BOMB_TIMER -> {

                float e = bombTimer + (float)value;

                setValue(type, e);
            }
            case CONTAGION -> {

                float e = contagion + (float)value;

                setValue(type, e);
            }
            case OIL -> {

                float e = oil + (float)value;

                setValue(type, e);
            }
            case FIRE -> {

                float e = fire + (float)value;

                setValue(type, e);
            }
            case PHOSPHORUS -> {

                float e = phosphorus + (float)value;

                setValue(type, e);
            }
            case BALEFIRE -> {

                float e = balefire + (float)value;

                setValue(type, e);
            }
            default -> {


            }
        }
    }

    @Override
    public void addValue(ContaminationEffect contaminationEffect) {

        contamination.add(contaminationEffect);
    }

    public static class ContaminationEffect {

        public float maxRad;
        public int maxTime;
        public int time;
        public boolean ignoreArmor;

        public ContaminationEffect(float rad, int time, boolean ignoreArmor) {

            this.maxRad = rad;
            this.maxTime = this.time = time;
            this.ignoreArmor = ignoreArmor;
        }

        public float getRad() {

            return maxRad * ((float)time / (float)maxTime);
        }

        public CompoundTag serializeNBT() {

            CompoundTag tag = new CompoundTag();
            tag.putFloat("maxRad", this.maxRad); // example
            tag.putInt("maxTime", this.maxTime);
            tag.putInt("time", this.time);
            tag.putBoolean("ignoreArmor", this.ignoreArmor);
            return tag;
        }

        public static ContaminationEffect deserializeNBT(CompoundTag tag) {

            float maxRad = tag.getFloat("maxRad");
            int maxTime = tag.getInt("maxTime");
            int time = tag.getInt("time");
            boolean ignoreArmor = tag.getBoolean("ignoreArmor");
            ContaminationEffect effect = new ContaminationEffect(maxRad, maxTime, ignoreArmor);
            effect.time = time;
            return effect;
        }
    }

    public CompoundTag writeNBT() {

        CompoundTag props = new CompoundTag();

        props.putFloat("hfr_old_max_health", (float)getValue(Type.OLD_MAX_HEALTH));
        props.putFloat("hfr_old_rounded_damage", (float)getValue(Type.OLD_ROUNDED_DAMAGE));
        props.putDouble("hfr_internal_damage", getValue(Type.INTERNAL_DAMAGE));
        props.putDouble("hfr_permanent_contamination", getValue(Type.PERMANENT_CONTAMINATION));
        props.putFloat("hfr_radiation", (float)getValue(Type.RADIATION));
        props.putFloat("hfr_neutron", (float)getValue(Type.NEUTRON));
        props.putFloat("hfr_digamma", (float)getValue(Type.DIGAMMA));
        props.putInt("hfr_asbestos", (int)getValue(Type.ASBESTOS));
        props.putInt("hfr_blacklung", (int)getValue(Type.BLACKLUNG));
        props.putFloat("hfr_radenv", (float)getValue(Type.RADENV));
        props.putFloat("hfr_radbuf", (float)getValue(Type.RADBUF));
        props.putInt("hfr_bomb", (int)getValue(Type.BOMB_TIMER));
        props.putInt("hfr_contagion", (int)getValue(Type.CONTAGION));
        props.putInt("hfr_oil", (int)getValue(Type.OIL));
        props.putInt("hfr_fire", (int)getValue(Type.FIRE));
        props.putInt("hfr_phosphorus", (int)getValue(Type.PHOSPHORUS));
        props.putInt("hfr_balefire", (int)getValue(Type.BALEFIRE));

        props.putFloat("hfr_cont_count", getValue().size());

        for(int i = 0; i < getValue().size(); i++) {

            props.put("hfr_buf_" + i, getValue().get(i).serializeNBT());
        }

        return props;
    }

    public void readNBT(CompoundTag nbt) {

        setValue(Type.OLD_MAX_HEALTH, nbt.getFloat("hfr_old_max_health"));
        setValue(Type.OLD_ROUNDED_DAMAGE, nbt.getFloat("hfr_old_rounded_damage"));
        setValue(Type.INTERNAL_DAMAGE, nbt.getDouble("hfr_internal_damage"));
        setValue(Type.PERMANENT_CONTAMINATION, nbt.getDouble("hfr_permanent_contamination"));
        setValue(Type.RADIATION, nbt.getFloat("hfr_radiation"));
        setValue(Type.NEUTRON, nbt.getFloat("hfr_neutron"));
        setValue(Type.DIGAMMA, nbt.getFloat("hfr_digamma"));
        setValue(Type.ASBESTOS, nbt.getFloat("hfr_asbestos"));
        setValue(Type.BLACKLUNG, nbt.getFloat("hfr_blacklung"));
        setValue(Type.RADENV, nbt.getFloat("hfr_radenv"));
        setValue(Type.RADBUF, nbt.getFloat("hfr_radbuf"));
        setValue(Type.BOMB_TIMER, nbt.getFloat("hfr_bomb"));
        setValue(Type.CONTAGION, nbt.getFloat("hfr_contagion"));
        setValue(Type.OIL, nbt.getFloat("hfr_oil"));
        setValue(Type.FIRE, nbt.getFloat("hfr_fire"));
        setValue(Type.PHOSPHORUS, nbt.getFloat("hfr_phosphorus"));
        setValue(Type.BALEFIRE, nbt.getFloat("hfr_balefire"));

        getValue().clear();
        for (int i = 0; i < nbt.getFloat("hfr_cont_count"); i++) {
            CompoundTag contaminationTag = nbt.getCompound("hfr_buf_" + i);
            getValue().add(ContaminationEffect.deserializeNBT(contaminationTag));
        }
    }

}
