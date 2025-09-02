package com.hbm.nucleartech.capability.energy;

import com.hbm.nucleartech.interfaces.IWattHourStorage;
import com.hbm.nucleartech.util.FloatingLong;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class WattHourStorage implements IWattHourStorage, INBTSerializable<CompoundTag> {

    public static final double MCSECOND_TO_TICK = 3.6;

    protected FloatingLong storedWattHours;

    /// in watt-hours (Wh)
    protected FloatingLong capacity;
    /// in watt-hours (Wh)
    protected FloatingLong maxReceive;
    /// in watt-hours (Wh)
    protected FloatingLong maxExtract;

    public WattHourStorage(
            FloatingLong capacity) {

        this(capacity, capacity, capacity, FloatingLong.ZERO);
    }

    public WattHourStorage(
            FloatingLong capacity,
            FloatingLong maxTransfer) {

        this(capacity, maxTransfer, maxTransfer, FloatingLong.ZERO);
    }

    public WattHourStorage(
            FloatingLong capacity,
            FloatingLong maxReceive,
            FloatingLong maxExtract) {

        this(capacity, maxReceive, maxExtract, FloatingLong.ZERO);
    }

    public WattHourStorage(
            FloatingLong capacity,
            FloatingLong maxReceive,
            FloatingLong maxExtract,
            FloatingLong initialStorage) {

        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;

        FloatingLong init = Max(FloatingLong.ZERO, Min(initialStorage, capacity));

        this.storedWattHours = init;

//        System.err.println("[Debug] capacity: " + capacity.toString() + ", maxReceive: " + maxReceive + ", maxExtract: " + maxExtract + ", initialStorage: " + initialStorage + ", init: " + init);
    }

    @Override
    public String formatWattHoursStored() {

        var value = storedWattHours;

        if (value.compareTo(FloatingLong.create(1000)) < 0)
            return String.format("%.1f", value.floatValue()) + "Wh";

        final String[] units = {"Wh", "kWh", "MWh", "GWh", "TWh", "PWh", "EWh", "ZWh", "YWh", "RWh", "QWh", "?Wh"};

        int unitIndex = 0;
        while (value.compareTo(FloatingLong.create(1000)) >= 0 && unitIndex < units.length - 1) {

            value = value.divide(1000);
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", value.floatValue());

        return formatted + units[unitIndex];
    }

    @Override
    public String formatMaxWattHoursStored() {

        var value = capacity;

        if (value.compareTo(FloatingLong.create(1000)) < 0)
            return String.format("%.1f", value.floatValue()) + "Wh";

        final String[] units = {"Wh", "kWh", "MWh", "GWh", "TWh", "PWh", "EWh", "ZWh", "YWh", "RWh", "QWh", "?Wh"};

        int unitIndex = 0;
        while (value.compareTo(FloatingLong.create(1000)) >= 0 && unitIndex < units.length - 1) {

            value = value.divide(1000);
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", value.floatValue());

        return formatted + units[unitIndex];
    }

    @Override
    public String formatWattsCharge() {

        var value = translateWattHours(maxReceive.divide(WattHourStorage.MCSECOND_TO_TICK));

        if (value.compareTo(FloatingLong.create(1000)) < 0)
            return String.format("%.1f", value.floatValue()) + "W";

        final String[] units = {"W", "kW", "MW", "GW", "TW", "PW", "EW", "ZW", "YW", "RW", "QW", "?W"};

        int unitIndex = 0;
        while (value.compareTo(FloatingLong.create(1000)) >= 0 && unitIndex < units.length - 1) {

            value = value.divide(1000);
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", value.floatValue());

        return formatted + units[unitIndex];
    }

    @Override
    public String formatWattsDischarge() {

        var value = translateWattHours(maxExtract.divide(WattHourStorage.MCSECOND_TO_TICK));

        if (value.compareTo(FloatingLong.create(1000)) < 0)
            return String.format("%.1f", value.floatValue()) + "W";

        final String[] units = {"W", "kW", "MW", "GW", "TW", "PW", "EW", "ZW", "YW", "RW", "QW", "?W"};

        int unitIndex = 0;
        while (value.compareTo(FloatingLong.create(1000)) >= 0 && unitIndex < units.length - 1) {

            value = value.divide(1000);
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", value.floatValue());

        return formatted + units[unitIndex];
    }

    @Override
    public FloatingLong receiveWattage(FloatingLong maxReceive, boolean simulate) {

        if(!canReceive())
            return FloatingLong.ZERO;

        FloatingLong wattHoursReceived = Min(capacity.subtract(storedWattHours), Min(this.maxReceive, maxReceive));
        if(!simulate)
            storedWattHours = storedWattHours.add(wattHoursReceived);
        return wattHoursReceived;
    }

    @Override
    public FloatingLong extractWattage(FloatingLong maxExtract, boolean simulate) {

        if(!canExtract())
            return FloatingLong.ZERO;

        FloatingLong wattHoursExtracted = Min(storedWattHours, Min(this.maxExtract, maxExtract));
        if(!simulate)
            storedWattHours = storedWattHours.subtract(wattHoursExtracted);
        return wattHoursExtracted;
    }

    @Override
    public FloatingLong getWattageStored() {

        return storedWattHours;
    }

    @Override
    public FloatingLong getMaxWattageStored() {

        return capacity;
    }

    @Override
    public void setMaxWattageStored(FloatingLong set) {

        capacity = set;
    }

    @Override
    public void setWattageStored(FloatingLong wattHours) {

        storedWattHours = Max(FloatingLong.ZERO, Min(wattHours, capacity));
    }

    public FloatingLong getMaxExtract() {

        return maxExtract;
    }

    public FloatingLong getMaxReceive() {

        return maxReceive;
    }

    public void setMaxExtract(FloatingLong set) {

        maxExtract = set;
    }

    public void setMaxReceive(FloatingLong set) {

        maxReceive = set;
    }

    @Override
    public boolean canExtract() {

        return maxExtract.compareTo(FloatingLong.ZERO) > 0;
    }

    @Override
    public boolean canReceive() {

        return maxReceive.compareTo(FloatingLong.ZERO) > 0;
    }

    public static final String TAG_STORED = "Stored";
    public static final String TAG_CAPACITY = "Capacity";
    public static final String TAG_MAX_EXTRACT = "MaxExtract";
    public static final String TAG_MAX_RECEIVE = "MaxReceive";

    @Override
    public CompoundTag serializeNBT() {

        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_STORED, storedWattHours.toString());
        tag.putString(TAG_CAPACITY, capacity.toString());
        tag.putString(TAG_MAX_EXTRACT, maxExtract.toString());
        tag.putString(TAG_MAX_RECEIVE, maxReceive.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

        if(nbt.contains(TAG_STORED)) storedWattHours = FloatingLong.create(nbt.getString(TAG_STORED));
        if(nbt.contains(TAG_CAPACITY)) capacity = FloatingLong.create(nbt.getString(TAG_CAPACITY));
        if(nbt.contains(TAG_MAX_EXTRACT)) maxExtract = FloatingLong.create(nbt.getString(TAG_MAX_EXTRACT));
        if(nbt.contains(TAG_MAX_RECEIVE)) maxReceive = FloatingLong.create(nbt.getString(TAG_MAX_RECEIVE));
    }

    public static FloatingLong Min(FloatingLong a, FloatingLong b) {

        if (a == null) return b == null ? FloatingLong.ZERO : b;
        if (b == null) return a;
        return a.compareTo(b) <= 0 ? a : b;
    }
    public static FloatingLong Max(FloatingLong a, FloatingLong b) {
        if (a == null) return b == null ? FloatingLong.ZERO : b;
        if (b == null) return a;
        return a.compareTo(b) >= 0 ? a : b;
    }

    public static FloatingLong translateWatts(FloatingLong inputWatts) {

        FloatingLong wattHours = inputWatts.divide(3600);

//        System.out.println(
//                        "[Debug] Watts: " + inputWatts +
//                        ", Watt-hours removed: " + wattHours +
//                        " Wh/t, Total Wh in storage: " + this.storedWattHours
//                );

        return wattHours;
    }
    public static double translateWattHours(double inputWattHours) {

        double watts = inputWattHours * 3600;

//        System.out.println(
//                "[Debug] Watts: " + watts +
//                        ", Watt-hours added: " + inputWattHours +
//                        " Wh/t, Total Wh in storage: " + this.storedWattHours
//        );

        return watts;
    }
    public static FloatingLong translateWattHours(FloatingLong inputWattHours) {

        FloatingLong watts = inputWattHours.multiply(FloatingLong.create(3600));

//        System.out.println(
//                "[Debug] Watts: " + watts +
//                        ", Watt-hours added: " + inputWattHours +
//                        " Wh/t, Total Wh in storage: " + this.storedWattHours
//        );

        return watts;
    }
}
