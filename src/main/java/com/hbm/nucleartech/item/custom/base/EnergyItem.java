package com.hbm.nucleartech.item.custom.base;

import com.hbm.nucleartech.interfaces.IEnergyItem;
import net.minecraft.world.item.Item;

public class EnergyItem extends Item implements IEnergyItem {

    protected final boolean charges;
    protected final boolean discharges;

    public EnergyItem(Properties pProperties, boolean canCharge, boolean canDischarge) {
        super(pProperties);

        this.charges = canCharge;
        this.discharges = canDischarge;
    }

    @Override
    public boolean canCharge() {

        return charges;
    }

    @Override
    public boolean canDischarge() {

        return discharges;
    }
}
