package com.hbm.nucleartech.capability.entity;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.capability.HbmCapabilities.PlayerCapabilitiesSyncMessage;
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

    public void syncPlayerVariables(Entity entity) {

        if(entity instanceof ServerPlayer serverPlayer)
            HBM.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerCapabilitiesSyncMessage(this));
    }

    @Override
    public float getValue(Type type) {

        switch(type) {

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
    public void setValue(Type type, float value) {

        switch(type) {

            case RADIATION -> {

                radiation = value;
                break;
            }
            case NEUTRON -> {

                neutron = value;
                break;
            }
            case DIGAMMA -> {

                digamma = value;
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

                radEnv = value;
                break;
            }
            case RADBUF -> {

                radBuf = value;
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
    public void addValue(Type type, float value) {

        switch(type) {

            case RADIATION -> {

                float rad = radiation + value;

                rad = Mth.clamp(rad, 0F, 2500F);

                setValue(type, rad);
            }
            case NEUTRON -> {

                float rad = neutron + value;

//                rad = Mth.clamp(rad, 0F, 2500F);

                setValue(type, rad);
            }
            case DIGAMMA -> {

                float dRad = digamma + value;

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

                float radE = radEnv + value;

                radE = Mth.clamp(radE, 0, 2500F);

                setValue(type, radE);
            }
            case RADBUF -> {

                float e = radBuf + value;

                e = Mth.clamp(e, 0, 2500F);

                setValue(type, e);
            }
            case BOMB_TIMER -> {

                float e = bombTimer + value;

                setValue(type, e);
            }
            case CONTAGION -> {

                float e = contagion + value;

                setValue(type, e);
            }
            case OIL -> {

                float e = oil + value;

                setValue(type, e);
            }
            case FIRE -> {

                float e = fire + value;

                setValue(type, e);
            }
            case PHOSPHORUS -> {

                float e = phosphorus + value;

                setValue(type, e);
            }
            case BALEFIRE -> {

                float e = balefire + value;

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

        props.putFloat("hfr_radiation", getValue(Type.RADIATION));
        props.putFloat("hfr_neutron", getValue(Type.NEUTRON));
        props.putFloat("hfr_digamma", getValue(Type.DIGAMMA));
        props.putInt("hfr_asbestos", (int)getValue(Type.ASBESTOS));
        props.putInt("hfr_blacklung", (int)getValue(Type.BLACKLUNG));
        props.putFloat("hfr_radenv", getValue(Type.RADENV));
        props.putFloat("hfr_radbuf", getValue(Type.RADBUF));
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
