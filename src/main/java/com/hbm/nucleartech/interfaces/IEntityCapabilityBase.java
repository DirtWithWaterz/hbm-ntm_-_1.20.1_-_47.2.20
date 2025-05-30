package com.hbm.nucleartech.interfaces;

import com.hbm.nucleartech.capability.entity.LivingEntityCapability.ContaminationEffect;

import java.util.List;

public interface IEntityCapabilityBase {

    float getValue(Type type);
    List<ContaminationEffect> getValue();
    void setValue(Type type, float value);
    void setValue(List<ContaminationEffect> contamination);
    void addValue(Type type, float value);
    void addValue(ContaminationEffect contaminationEffect);

    enum Type {
        RADIATION,
        DIGAMMA,
        ASBESTOS,
        BLACKLUNG,
        RADENV,
        RADBUF,
        BOMB_TIMER,
        CONTAGION,
        OIL,
        FIRE,
        PHOSPHORUS,
        BALEFIRE
    }
}
