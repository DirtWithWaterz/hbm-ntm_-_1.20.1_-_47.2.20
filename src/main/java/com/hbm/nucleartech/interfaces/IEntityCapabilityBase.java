package com.hbm.nucleartech.interfaces;

import com.hbm.nucleartech.capability.entity.LivingEntityCapability.ContaminationEffect;

import java.util.List;

public interface IEntityCapabilityBase {

    double getValue(Type type);
    List<ContaminationEffect> getValue();
    void setValue(Type type, double value);
    void setValue(List<ContaminationEffect> contamination);
    void addValue(Type type, double value);
    void addValue(ContaminationEffect contaminationEffect);

    enum Type {
        OLD_MAX_HEALTH,
        OLD_ROUNDED_DAMAGE,
        PERMANENT_CONTAMINATION,
        INTERNAL_DAMAGE,
        RADIATION,
        NEUTRON,
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
