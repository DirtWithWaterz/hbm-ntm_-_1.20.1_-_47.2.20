package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.capability.energy.ItemBatteryProvider;
import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.hbm.nucleartech.interfaces.IWattHourStorage;
import com.hbm.nucleartech.item.custom.base.EnergyItem;
import com.hbm.nucleartech.util.FloatingLong;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class BatteryItem extends EnergyItem {

    private final FloatingLong capacity;
    private final FloatingLong maxReceive;
    private final FloatingLong maxExtract;
    private final FloatingLong startEnergy;

    public BatteryItem(Properties properties, FloatingLong capacity, FloatingLong chargeRate, FloatingLong dischargeRate, FloatingLong initialCharge) {
        super(properties.setNoRepair().stacksTo(1), true, true);
        this.capacity = capacity;
        this.maxReceive = chargeRate;
        this.maxExtract = dischargeRate;
        this.startEnergy = initialCharge;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        double energy = getEnergyStored(stack).doubleValue();
        double max = getMaxEnergyStored(stack).doubleValue();
        if (max <= 0.0) return 0;
        double frac = energy / max;
        frac = Math.max(0.0, Math.min(1.0, frac)); // clamp
        return (int)Math.round(frac * 13.0);
    }

    public static String formatWattHoursStored(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattHoursStored)
                .orElse(null);
    }

    public static String formatMaxWattHoursStored(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatMaxWattHoursStored)
                .orElse(null);
    }

    public static String formatWattsCharge(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattsCharge)
                .orElse(null);
    }

    public static String formatWattsDischarge(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattsDischarge)
                .orElse(null);
    }

    public static FloatingLong getEnergyStored(ItemStack stack) {
        // Try capability first (when provider exists in-memory)
        LazyOptional<IWattHourStorage> cap = stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE);
        if (cap.isPresent()) {
            return cap.map(IWattHourStorage::getWattageStored).orElse(FloatingLong.ZERO);
        }

        // Fall back to reading serialized NBT (client copies will have this)
        if (stack.hasTag() && stack.getTag().contains("WattHourStorage")) {
            CompoundTag t = stack.getTag().getCompound("WattHourStorage");
            if (t.contains(WattHourStorage.TAG_STORED)) {
                return FloatingLong.create(t.getString(WattHourStorage.TAG_STORED));
            }
        }

        // Last resort: default start energy from item (safe fallback)
        return ((BatteryItem) stack.getItem()).startEnergy;
    }

    public static FloatingLong getMaxEnergyStored(ItemStack stack) {
        // Try capability first (when provider exists in-memory)
        LazyOptional<IWattHourStorage> cap = stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE);
        if (cap.isPresent()) {
            return cap.map(IWattHourStorage::getMaxWattageStored).orElse(FloatingLong.ZERO);
        }

        // Fall back to reading serialized NBT (client copies will have this)
        if (stack.hasTag() && stack.getTag().contains("WattHourStorage")) {
            CompoundTag t = stack.getTag().getCompound("WattHourStorage");
            if (t.contains(WattHourStorage.TAG_CAPACITY)) {
                return FloatingLong.create(t.getString(WattHourStorage.TAG_CAPACITY));
            }
        }

        // Last resort: default start energy from item (safe fallback)
        return ((BatteryItem) stack.getItem()).capacity;
    }

//    public static FloatingLong setEnergy(ItemStack stack, FloatingLong amount) {
//
//        FloatingLong capped = WattHourStorage.Min(amount, getMaxEnergyStored(stack));
//        stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {
//            cap.setWattageStored(capped); // you need to add setWattageStored to the IWattHourStorage impl (or expose it)
//        });
//
//        return capped;
//    }

    public static FloatingLong addEnergy(ItemStack stack, FloatingLong amount, boolean simulate) {

        AtomicReference<FloatingLong> val = new AtomicReference<>(FloatingLong.ZERO);

        stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {

            val.set(cap.receiveWattage(amount, simulate));
        });

        return val.get();
    }

    public static FloatingLong removeEnergy(ItemStack stack, FloatingLong amount, boolean simulate) {

        AtomicReference<FloatingLong> val = new AtomicReference<>(FloatingLong.ZERO);

        stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {

            val.set(cap.extractWattage(amount, simulate));
        });

        return val.get();
    }

    public static boolean canExtract(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::canExtract).orElse(false);
    }

    public static boolean canReceive(ItemStack stack) {

        return stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::canReceive).orElse(false);
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0x00FF00;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {

        FloatingLong start = (nbt != null && nbt.contains("WattHourStorage")) ?
                FloatingLong.create(nbt.getCompound("WattHourStorage").getString(WattHourStorage.TAG_STORED)) : startEnergy;

        WattHourStorage storage = new WattHourStorage(capacity, maxReceive, maxExtract, start);
        return new ItemBatteryProvider(stack, storage);
    }
}
