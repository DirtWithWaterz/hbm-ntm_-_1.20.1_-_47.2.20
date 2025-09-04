package com.hbm.nucleartech.capability.energy;

import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.interfaces.IWattHourStorage;
import com.hbm.nucleartech.util.FloatingLong;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.hbm.nucleartech.block.custom.base.BaseHbmBlockEntity.WATT_HOUR_TO_FE;

public class ItemBatteryProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final ItemStack stack;
    private final WattHourStorage storage;
    private final LazyOptional<IWattHourStorage> lazyWh;
    private final LazyOptional<IEnergyStorage> lazyFE;

    public ItemBatteryProvider(ItemStack stack, WattHourStorage storage) {
        this.stack = stack;
        this.storage = storage;
        // Provide a wrapper IWattHourStorage that writes storage->stack when non-simulate
        this.lazyWh = LazyOptional.of(() -> new IWattHourStorage() {

            @Override
            public String formatWattHoursStored() {

                return storage.formatWattHoursStored();
            }

            @Override
            public String formatMaxWattHoursStored() {

                return storage.formatMaxWattHoursStored();
            }

            @Override
            public String formatWattsCharge() {

                return storage.formatWattsCharge();
            }

            @Override
            public String formatWattsDischarge() {

                return storage.formatWattsDischarge();
            }

            @Override
            public FloatingLong receiveWattage(FloatingLong maxReceive, boolean simulate) {
                FloatingLong r = storage.receiveWattage(maxReceive, simulate);
                if (!simulate) writeToStack();
                return r;
            }
            @Override
            public FloatingLong extractWattage(FloatingLong maxExtract, boolean simulate) {
                FloatingLong r = storage.extractWattage(maxExtract, simulate);
                if (!simulate) writeToStack();
                return r;
            }
            @Override
            public FloatingLong getWattageStored() {
                return storage.getWattageStored();
            }

            @Override
            public void setWattageStored(FloatingLong set) {
//                storage.setWattageStored(set);
            }

            @Override
            public FloatingLong getMaxWattageStored() {
                return storage.getMaxWattageStored();
            }

            @Override
            public void setMaxWattageStored(FloatingLong set) {
                storage.setMaxWattageStored(set);
            }

            @Override
            public boolean canExtract() {
                return storage.canExtract();
            }
            @Override
            public boolean canReceive() {
                return storage.canReceive();
            }
        });

        this.lazyFE = LazyOptional.of(() -> new IEnergyStorage() {

            private FloatingLong toWh(int fe) {

                return FloatingLong.create(fe).divide(WATT_HOUR_TO_FE);
            }
            private int fromWh(FloatingLong wh) {

                try {

                    return wh.multiply(WATT_HOUR_TO_FE).intValue();
                } catch (Exception ex) {

                    // fallback clamp
                    return (feValueIsPositive(wh) ? Integer.MAX_VALUE : Integer.MIN_VALUE);
                }
            }
            private boolean feValueIsPositive(FloatingLong wh) {

                try {

                    return wh.compareTo(FloatingLong.ZERO) >= 0;
                } catch (Exception e) {

                    return true;
                }
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {

                var r = fromWh(storage.receiveWattage(toWh(maxReceive), simulate));
                if (!simulate) writeToStack();
                return r;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {

                var r = fromWh(storage.extractWattage(toWh(maxExtract), simulate));
                if (!simulate) writeToStack();
                return r;
            }

            @Override
            public int getEnergyStored() {

                return fromWh(storage.getWattageStored());
            }

            @Override
            public int getMaxEnergyStored() {

                return fromWh(storage.getMaxWattageStored());
            }

            @Override
            public boolean canExtract() {

                return storage.canExtract();
            }

            @Override
            public boolean canReceive() {

                return storage.canReceive();
            }
        });
    }

    private void writeToStack() {
        // write the storage's NBT into the ItemStack tag so that when the slot is resent the client sees new values
        CompoundTag tag = stack.getOrCreateTag();
        tag.put("WattHourStorage", storage.serializeNBT()); // use the same tag name storage uses
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == HbmCapabilities.WATT_HOUR_STORAGE)
            return lazyWh.cast();
        else if (cap == ForgeCapabilities.ENERGY)
            return lazyFE.cast();

        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return storage.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        storage.deserializeNBT(nbt);
        // we don't automatically write to stack here (constructor already sets initial)
    }
}
