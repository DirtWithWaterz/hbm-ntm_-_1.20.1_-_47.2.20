package com.hbm.nucleartech.block.custom.base;

import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.hbm.nucleartech.interfaces.IWattHourStorage;
import com.hbm.nucleartech.util.FloatingLong;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class BaseHbmBlockEntity extends BlockEntity {

    public static final FloatingLong WATT_HOUR_TO_FE = FloatingLong.create(1440); // 1 Wh == 1440 FE == 3600 mekJ

    // --- Systems ---

    private final WattHourStorage wattHourHandler;

    private final LazyOptional<IWattHourStorage> lazyWattInternal;
    private final LazyOptional<IWattHourStorage> lazyWattExternal;

    private final LazyOptional<IEnergyStorage> lazyFEInternal;
    private final LazyOptional<IEnergyStorage> lazyFEExternal;

    public BaseHbmBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, FloatingLong energyCapacity,
                              FloatingLong maxReceive, FloatingLong maxExtract, FloatingLong initialStorage) {
        super(pType, pPos, pBlockState);

        this.wattHourHandler = new WattHourStorage(
                energyCapacity,
                maxReceive,
                maxExtract,
                initialStorage
        );

        this.lazyWattInternal = LazyOptional.of(() -> createWattSideWrapper(true, true));
        this.lazyWattExternal = LazyOptional.of(() -> createWattSideWrapper(true, false));

        this.lazyFEInternal = LazyOptional.of(() -> createFESideWrapper(true, true));
        this.lazyFEExternal = LazyOptional.of(() -> createFESideWrapper(true, false));
    }

    private IWattHourStorage createWattSideWrapper(final boolean allowReceive, final boolean allowExtract) {

        final WattHourStorage real = this.wattHourHandler;

        return new IWattHourStorage() {

            @Override
            public String formatWattHoursStored() {

                return real.formatWattHoursStored();
            }

            @Override
            public String formatMaxWattHoursStored() {

                return real.formatMaxWattHoursStored();
            }

            @Override
            public String formatWattsCharge() {

                return real.formatWattsCharge();
            }

            @Override
            public String formatWattsDischarge() {

                return real.formatWattsDischarge();
            }

            @Override
            public FloatingLong receiveWattage(FloatingLong maxReceive, boolean simulate) {

                if(!allowReceive) return FloatingLong.ZERO;
                return real.receiveWattage(maxReceive, simulate);
            }

            @Override
            public FloatingLong extractWattage(FloatingLong maxExtract, boolean simulate) {

                if(!allowExtract) return FloatingLong.ZERO;
                return real.extractWattage(maxExtract, simulate);
            }

            @Override
            public FloatingLong getWattageStored() {

                return real.getWattageStored();
            }

            @Override
            public void setWattageStored(FloatingLong set) {

                real.setWattageStored(set);
            }

            @Override
            public FloatingLong getMaxWattageStored() {

                return real.getMaxWattageStored();
            }

            @Override
            public void setMaxWattageStored(FloatingLong set) {

                real.setMaxWattageStored(set);
            }

            @Override
            public boolean canExtract() {

                return allowExtract && real.canExtract();
            }

            @Override
            public boolean canReceive() {

                return allowReceive && real.canReceive();
            }
        };
    }

    private IEnergyStorage createFESideWrapper(final boolean allowReceive, final boolean allowExtract) {

        final WattHourStorage real = this.wattHourHandler;

        return new IEnergyStorage() {

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

                if(!allowReceive) return 0;

                var received = real.receiveWattage(toWh(maxReceive), simulate);
                return fromWh(received);
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {

                if(!allowExtract) return 0;

                var extracted = real.extractWattage(toWh(maxExtract), simulate);
                return fromWh(extracted);
            }

            @Override
            public int getEnergyStored() {

                return fromWh(real.getWattageStored());
            }

            @Override
            public int getMaxEnergyStored() {

                return fromWh(real.getMaxWattageStored());
            }

            @Override
            public boolean canExtract() {

                return allowExtract && real.canExtract();
            }

            @Override
            public boolean canReceive() {

                return allowReceive && real.canReceive();
            }
        };
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if (cap == HbmCapabilities.WATT_HOUR_STORAGE) {

            if(side == null)
                return lazyWattInternal.cast();

            return switch(Objects.requireNonNull(side)) {

                case DOWN, EAST, WEST -> lazyWattExternal.cast();
                default   -> super.getCapability(cap, side);
            };
        }
        else if (cap == ForgeCapabilities.ENERGY) {

            if(side == null)
                return lazyFEInternal.cast();

            return switch(Objects.requireNonNull(side)) {

                case DOWN, EAST, WEST -> lazyFEExternal.cast();
                default   -> super.getCapability(cap, side);
            };
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void setRemoved() {

        super.setRemoved();
        lazyWattInternal.invalidate();
        lazyFEInternal.invalidate();
        lazyWattExternal.invalidate();
        lazyFEExternal.invalidate();
    }

    @Override
    public void invalidateCaps() {

        super.invalidateCaps();
        lazyWattInternal.invalidate();
        lazyFEInternal.invalidate();
        lazyWattExternal.invalidate();
        lazyFEExternal.invalidate();
    }

    public String formatWattHoursStored() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattHoursStored)
                .orElse(null);
    }

    public String formatMaxWattHoursStored() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatMaxWattHoursStored)
                .orElse(null);
    }

    public String formatWattsCharge() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattsCharge)
                .orElse(null);
    }

    public String formatWattsDischarge() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::formatWattsDischarge)
                .orElse(null);
    }

    public void setEnergy(FloatingLong amount) {

        this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {

            if(cap instanceof WattHourStorage ws)
                ws.setWattageStored(amount);
        });
        setChanged();
    }

    public FloatingLong getEnergy() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::getWattageStored)
                .orElse(FloatingLong.ZERO);
    }

    public FloatingLong getMaxEnergy() {

        return this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE)
                .map(IWattHourStorage::getMaxWattageStored)
                .orElse(FloatingLong.ZERO);
    }

    public FloatingLong insertEnergy(FloatingLong amount, boolean simulate) {

        AtomicReference<FloatingLong> val = new AtomicReference<>();

        this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {

            val.set(cap.receiveWattage(amount, simulate));
        });

        setChanged();
        return val.get();
    }

    public FloatingLong extractEnergy(FloatingLong amount, boolean simulate) {

        AtomicReference<FloatingLong> val = new AtomicReference<>();

        this.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {

            val.set(cap.extractWattage(amount, simulate));
        });

        setChanged();
        return val.get();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {

        super.saveAdditional(tag);
        tag.put("WattHourStorage", wattHourHandler.serializeNBT());
    }

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);
        if(tag.contains("WattHourStorage"))
            wattHourHandler.deserializeNBT(tag.getCompound("WattHourStorage"));
    }
}
