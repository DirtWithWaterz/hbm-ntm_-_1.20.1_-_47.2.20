package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.hbm.nucleartech.item.custom.base.EnergyItem;

import static com.hbm.nucleartech.capability.energy.WattHourStorage.translateWattHours;

public class SelfChargingBatteryItem extends EnergyItem {

    protected final double dischargeRate;

    public SelfChargingBatteryItem(Properties pProperties, double dischargeRate) {
        super(pProperties.stacksTo(1), false, true);

        this.dischargeRate = dischargeRate;
    }

    public double getDischargeRate() {

        return dischargeRate;
    }

    public String formatWattsDischarge() {

        var value = translateWattHours(dischargeRate / WattHourStorage.MCSECOND_TO_TICK);

        if (value < 1000)
            return (float)value + "W";

        final String[] units = {"W", "kW", "MW", "GW", "TW", "PW", "EW", "ZW", "YW", "RW", "QW", "?W"};

        int unitIndex = 0;
        while (value >= 1000 && unitIndex < units.length - 1) {

            value = value / 1000;
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", (float)value);

        return formatted + units[unitIndex];
    }
}
