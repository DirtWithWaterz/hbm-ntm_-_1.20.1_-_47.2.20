package com.hbm.nucleartech.block.entity;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.custom.base.BaseHbmBlockEntity;
import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.hbm.nucleartech.datagen.ModRecipeProvider.MetaData;
import com.hbm.nucleartech.interfaces.IEnergyItem;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.item.custom.BatteryItem;
import com.hbm.nucleartech.item.custom.BladeItem;
import com.hbm.nucleartech.item.custom.SelfChargingBatteryItem;
import com.hbm.nucleartech.network.HbmPacketHandler;
import com.hbm.nucleartech.network.packet.ClientboundShredderPacket;
import com.hbm.nucleartech.recipe.ShredderRecipe;
import com.hbm.nucleartech.screen.ShredderMenu;
import com.hbm.nucleartech.util.FloatingLong;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static com.hbm.nucleartech.capability.energy.WattHourStorage.translateWattHours;

public class ShredderEntity extends BaseHbmBlockEntity implements GeoBlockEntity, MenuProvider {

    public static final String ANIM = "animation.shredder.";
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private final ItemStackHandler itemHandler = new ItemStackHandler(30){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            if(slot == POWER_SLOT && !(isEnergyItem(stack) && canExtract(stack)))
                return false;

            else if(slot == LEFT_BLADE_SLOT && !(stack.getItem() instanceof BladeItem))
                return false;
            else if(slot == RIGHT_BLADE_SLOT && !(stack.getItem() instanceof BladeItem))
                return false;

            for(int oS : OUTPUT_SLOT) {

                if(slot == oS)
                    return false;
            }

            return super.isItemValid(slot, stack);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {

            setChanged();
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {

            if(slot == POWER_SLOT && !(isEnergyItem(stack) && canExtract(stack)))
                return stack;

            else if(slot == LEFT_BLADE_SLOT && !(stack.getItem() instanceof BladeItem))
                return stack;
            else if(slot == RIGHT_BLADE_SLOT && !(stack.getItem() instanceof BladeItem))
                return stack;

            for(int oS : OUTPUT_SLOT) {

                if(slot == oS)
                    return stack;
            }

            setChanged();
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private static final int POWER_SLOT = 0;
    private static final int LEFT_BLADE_SLOT = 1;
    private static final int RIGHT_BLADE_SLOT = 2;

    private static final int[] INPUT_SLOT = new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final int[] OUTPUT_SLOT = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    public int progress = 0;

    public int maxProgress = 60; // default, overridden by hasItems() scan
    public FloatingLong currentPowerConsumption = FloatingLong.ZERO; // default

    public int scaledEnergyProgress = 0;

    public boolean shred = false;

    public int leftBIdx = 0;
    public int rightBIdx = 0;

    public int leftDur = 0;
    public int rightDur = 0;
    public int leftMaxDur = 0;
    public int rightMaxDur = 0;

    public ShredderEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegisterBlockEntities.SHREDDER_ENTITY.get(), pPos, pBlockState, FloatingLong.create(5.0E3), FloatingLong.create(1.0E2).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E3).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.ZERO);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch(pIndex) {

                    case 0 -> ShredderEntity.this.progress;
                    case 1 -> ShredderEntity.this.scaledEnergyProgress;
                    case 2 -> ShredderEntity.this.leftDur;
                    case 3 -> ShredderEntity.this.rightDur;
                    case 4 -> ShredderEntity.this.leftMaxDur;
                    case 5 -> ShredderEntity.this.rightMaxDur;
                    case 6 -> ShredderEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch(pIndex) {

                    case 0 -> ShredderEntity.this.progress = pValue;
                    case 1 -> ShredderEntity.this.scaledEnergyProgress = pValue;
                    case 2 -> ShredderEntity.this.leftDur = pValue;
                    case 3 -> ShredderEntity.this.rightDur = pValue;
                    case 4 -> ShredderEntity.this.leftMaxDur = pValue;
                    case 5 -> ShredderEntity.this.rightMaxDur = pValue;
                    case 6 -> ShredderEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 7;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {

        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {

            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.hbm.shredder");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ShredderMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {

        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("shredder.progress", progress);
        pTag.putInt("shredder.scaled_energy", scaledEnergyProgress);
        pTag.putBoolean("shredder.shred", shred);

        pTag.putInt("shredder.left_b_idx", leftBIdx);
        pTag.putInt("shredder.right_b_idx", rightBIdx);

        pTag.putInt("shredder.left_dur", leftDur);
        pTag.putInt("shredder.right_dur", rightDur);

        pTag.putInt("shredder.left_max_dur", leftMaxDur);
        pTag.putInt("shredder.right_max_dur", rightMaxDur);

        pTag.putInt("shredder.max_progress", maxProgress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {

        super.load(pTag);

        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("shredder.progress");
        scaledEnergyProgress = pTag.getInt("shredder.scaled_energy");
        shred = pTag.getBoolean("shredder.shred");

        leftBIdx = pTag.getInt("shredder.left_b_idx");
        rightBIdx = pTag.getInt("shredder.right_b_idx");

        leftDur = pTag.getInt("shredder.left_dur");
        rightDur = pTag.getInt("shredder.right_dur");

        leftMaxDur = pTag.getInt("shredder.left_max_dur");
        rightMaxDur = pTag.getInt("shredder.right_max_dur");

        maxProgress = pTag.getInt("shredder.max_progress");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<>(this, "shred_c", 0, state -> {

            BlockEntity e = state.getData(DataTickets.BLOCK_ENTITY);


            if(e instanceof ShredderEntity shredder) {

                state.setAnimation(RawAnimation.begin().thenLoop(ANIM + shredder.leftBIdx + shredder.rightBIdx));

                state.setControllerSpeed(shredder.shred ? 1 : 0);

                return PlayState.CONTINUE;
            }
//            System.err.println("not correct entity");
                return PlayState.STOP;
        }));
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition, worldPosition.offset(1, 1, 1));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

//        System.out.println("[Debug] ticking");

//        System.err.println("stored energy: " + getEnergy().toString() + "Wh");

        if(canConsumePower() && hasPower()) {

//            System.out.println("consuming power...");
            consumePower();
        }

        boolean check = hasBlades();
//        System.out.println("has blades: " + check + ", has energy: " + hasEnergy() + ", has items: " + hasItems());

        if(hasItems() && hasEnergy() && check) { // Maybe it's hasItems()?? Dunnoo- sleepyyyy

//            System.out.println("Progressing");

            increaseProgress();

            consumeEnergy();

            shred = true;

            if(hasProgressFinished()) {

                craftItems();

                damageBlades(worldPosition);

                resetProgress(false);
            }
        }
        else {

            shred = false;
            resetProgress(true);
        }

        leftBIdx = this.itemHandler.getStackInSlot(LEFT_BLADE_SLOT).getItem() instanceof BladeItem bladeItem ? bladeItem.getIdx() : 0;
        rightBIdx = this.itemHandler.getStackInSlot(RIGHT_BLADE_SLOT).getItem() instanceof BladeItem bladeItem ? bladeItem.getIdx() : 0;

        ClientboundShredderPacket packet = new ClientboundShredderPacket(
                pPos.getX(), pPos.getY(), pPos.getZ(), leftBIdx, rightBIdx, shred, leftDur, leftMaxDur, rightDur, rightMaxDur, getEnergy(), currentPowerConsumption
        );

        HbmPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pPos)), packet);

        updateScaledEnergyProgress();

        setChanged(pLevel, pPos, pState);

//        System.out.println("\n=====================================================\n");
    }

    private void updateScaledEnergyProgress() {

        FloatingLong energy = getEnergy();
        FloatingLong maxEnergy = getMaxEnergy();

        FloatingLong size = FloatingLong.create(8.8E1);

        if(energy.compareTo(FloatingLong.ZERO) <= 0)
            scaledEnergyProgress = 0;
        else
            scaledEnergyProgress = size.divide(maxEnergy).multiply(energy).intValue();
    }

    private void damageBlades(BlockPos pos) {

        ItemStack left = this.itemHandler.getStackInSlot(LEFT_BLADE_SLOT);
        ItemStack right = this.itemHandler.getStackInSlot(RIGHT_BLADE_SLOT);

        left.hurt(1, level.random, null);
        right.hurt(1, level.random, null);

        if(left.getDamageValue() >= left.getMaxDamage()) {

//            itemHandler.setStackInSlot(LEFT_BLADE_SLOT, ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if(right.getDamageValue() >= right.getMaxDamage()) {

//            itemHandler.setStackInSlot(RIGHT_BLADE_SLOT, ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        leftDur = left.getDamageValue();
        rightDur = right.getDamageValue();
    }

    private boolean hasBlades() {

        ItemStack leftStack = this.itemHandler.getStackInSlot(LEFT_BLADE_SLOT);
        ItemStack rightStack = this.itemHandler.getStackInSlot(RIGHT_BLADE_SLOT);

        boolean leftBladeExists = (leftStack.getItem() instanceof BladeItem);
        boolean rightBladeExists = (rightStack.getItem() instanceof BladeItem);

        boolean leftBladeIsBroken = false;
        boolean rightBladeIsBroken = false;

        if(leftBladeExists) {

            leftDur = leftStack.getDamageValue();
            leftMaxDur = leftStack.getMaxDamage();

            leftBladeIsBroken = leftDur >= leftMaxDur;
        } else {

            leftDur = -1;
            leftMaxDur = 0;
        }

        if(rightBladeExists) {

            rightDur = rightStack.getDamageValue();
            rightMaxDur = rightStack.getMaxDamage();

            rightBladeIsBroken = rightDur >= rightMaxDur;
        } else {

            rightDur = -1;
            rightMaxDur = 0;
        }

        boolean result = (leftBladeExists && !leftBladeIsBroken) && (rightBladeExists && !rightBladeIsBroken);

//        System.err.println("[Has Blades] leftDur: " + leftDur + ", leftMaxDur: " + leftMaxDur + ", rightDur: " + rightDur + ", rightMaxDur: " + rightMaxDur + ", leftBladeExists: " + leftBladeExists + ", leftBladeIsBroken: " + leftBladeIsBroken + ", rightBladeExists: " + rightBladeExists + ", rightBladeIsBroken: " + rightBladeIsBroken + ", result: " + result);

        return result;
    }

    private void resetProgress(boolean resetPower) {

        progress = 0;
        if(resetPower)
            currentPowerConsumption = FloatingLong.ZERO;
    }

    private void craftItems() {

        for(int i = 0; i < INPUT_SLOT.length; i++) {

            craftItem(i);
        }
    }

    private void craftItem(int slot) {
        ItemStack input = this.itemHandler.getStackInSlot(INPUT_SLOT[slot]);
        if (input.isEmpty()) return;

        Optional<ShredderRecipe> recipe = getRecipe(INPUT_SLOT[slot]);

        NonNullList<Pair<Item, MetaData>> results;
        if (recipe.isPresent()) {
            results = recipe.get().getResults();
        } else {
            // No recipe: shred into waste
            results = NonNullList.withSize(1, Pair.of(RegisterItems.PLACEHOLDER.get(), new MetaData(1, 1, 100)));
        }

        // First pass: check that all *deterministic important* outputs (chance==100 && minCount>0)
        // have space (using worst-case maxCount)
        boolean skippedImportantResult = false;
        for (Pair<Item, MetaData> result : results) {
            MetaData md = result.right();
            if (md == null) continue;
            if (md.getChance() == 100 && md.getMinCount() > 0) {
                // worst-case space needed for deterministic outputs
                ItemStack checkStack = new ItemStack(result.left(), md.getMaxCount());
                if (findSlot(checkStack) == -1) {
                    skippedImportantResult = true;
                    break;
                }
            }
        }

        // If a required deterministic output does not fit, abort
        if (skippedImportantResult) return;

        // Otherwise: consume input and produce outputs (chance-based ones included).
        this.itemHandler.extractItem(INPUT_SLOT[slot], 1, false);

        for (Pair<Item, MetaData> result : results) {
            MetaData md = result.right();
            if (md == null) continue;

            // roll chance
            if (HBM.random(1, 100) > md.getChance()) continue;

            // ensure min/max are sane
            int minC = Math.max(0, md.getMinCount());
            int maxC = Math.max(minC, md.getMaxCount()); // force max >= min
            int produced = (minC == maxC) ? minC : HBM.random(minC, maxC);

            if (produced <= 0) continue;

            ItemStack stack = new ItemStack(result.left(), produced);
            int outSlot = findSlot(stack);
            if (outSlot == -1) {
                // couldn't fit this particular roll - skip it (not fatal)
                continue;
            }

            ItemStack existing = this.itemHandler.getStackInSlot(outSlot);
            this.itemHandler.setStackInSlot(outSlot,
                    new ItemStack(stack.getItem(), existing.getCount() + stack.getCount())
            );
        }
    }

    private int findSlot(ItemStack check) {

        for(int slot : OUTPUT_SLOT) {

            if(canInsertAmountIntoOutputSlot(check.getCount(), slot) && canInsertItemIntoOutputSlot(check.getItem(), slot))
                return slot;
        }
        return -1;
    }

    private boolean hasProgressFinished() {

        boolean result = progress >= maxProgress;

//        System.out.println("[Debug] has progress finished: " + result);

        return result;
    }

    private void increaseProgress() {

        progress++;

//        System.out.println("[Debug] progress: " + progress);
    }

    private boolean hasItems() {
        // We'll compute the maximum ticks & power across all input recipes
        int maxTicksFound = 0;
        FloatingLong maxPowerFound = FloatingLong.ZERO;

        // iterate every input slot
        for (int slot : INPUT_SLOT) {
            ItemStack input = this.itemHandler.getStackInSlot(slot);
            if (input.isEmpty()) continue;

            Optional<ShredderRecipe> recipeOpt = getRecipe(slot);

            // Build the list of expected outputs for this input:
            NonNullList<Pair<Item, MetaData>> results;
            if (recipeOpt.isPresent()) {
                results = recipeOpt.get().getResults();
                // update global max ticks + power (based on recipe)
                ShredderRecipe recipe = recipeOpt.get();
                maxTicksFound = Math.max(maxTicksFound, recipe.getTicks());
                // assume getPowerConsumption() returns energy for full operation;
                // we'll take the largest such value and later convert to per-tick if desired.
                maxPowerFound = WattHourStorage.Max(maxPowerFound, recipe.getPowerConsumption());
            } else {
                // fallback to waste (100% chance, min=1, max=1)
                results = NonNullList.withSize(1, Pair.of(RegisterItems.PLACEHOLDER.get(), new MetaData(1, 1, 100)));
            }

            // Prepare the *effective* output list for this recipe:
            // ignore entries with chance < 100 or minCount <= 0.
            // We'll take worst-case counts (maxCount) for space checks.
            class Wanted {
                final Item item;
                final int count;
                Wanted(Item item, int count) { this.item = item; this.count = count; }
            }
            java.util.ArrayList<Wanted> wanted = new java.util.ArrayList<>();
            for (Pair<Item, MetaData> r : results) {
                MetaData md = r.right();
                if (md == null) continue;
                if (md.getChance() < 100) continue; // ignore chance < 100
                if (md.getMinCount() <= 0) continue; // ignore minCount == 0
                int want = md.getMaxCount(); // worst-case space need
                if (want <= 0) continue;
                wanted.add(new Wanted(r.left(), want));
            }

            // If after filtering there are no deterministic outputs that require space,
            // then this input is considered shippable (it produces nothing that must be
            // reserved), so return true and still update ticks/power.
            if (wanted.isEmpty()) {
                // record found maxima
                if (maxTicksFound > 0) this.maxProgress = maxTicksFound;
                this.currentPowerConsumption = maxPowerFound;
                return true;
            }

            // Simulate insertion into outputs (greedy): for each output item,
            // try to put its required count into existing same-item stacks first,
            // then into empty slots. If all wanted items fit, we return true.

            // Build arrays of current output slot item and available space
            final int outSlots = OUTPUT_SLOT.length;
            Item[] slotItem = new Item[outSlots];
            int[] slotFree = new int[outSlots];
            for (int i = 0; i < outSlots; i++) {
                int outIdx = OUTPUT_SLOT[i];
                ItemStack s = this.itemHandler.getStackInSlot(outIdx);
                if (s.isEmpty()) {
                    slotItem[i] = null;
                    slotFree[i] = s.getMaxStackSize(); // full empty capacity
                } else {
                    slotItem[i] = s.getItem();
                    slotFree[i] = s.getMaxStackSize() - s.getCount();
                }
            }

            boolean allFit = true;
            // For each wanted item, try to place it into the simulated output slots
            for (Wanted w : wanted) {
                int remain = w.count;

                // 1) Merge into slots that already have the same item
                for (int i = 0; i < outSlots && remain > 0; i++) {
                    if (slotItem[i] != null && slotItem[i].equals(w.item)) {
                        int used = Math.min(slotFree[i], remain);
                        slotFree[i] -= used;
                        remain -= used;
                    }
                }
                // 2) Use empty slots
                for (int i = 0; i < outSlots && remain > 0; i++) {
                    if (slotItem[i] == null) {
                        int used = Math.min(slotFree[i], remain);
                        // mark this slot as now holding that item
                        slotItem[i] = w.item;
                        slotFree[i] -= used;
                        remain -= used;
                    }
                }

                if (remain > 0) {
                    allFit = false;
                    break;
                }
            }

            if (allFit) {
                // Save the maxima we discovered across recipes
                if (maxTicksFound > 0) this.maxProgress = maxTicksFound;
                this.currentPowerConsumption = maxPowerFound;
                return true;
            }
            // else loop to next input
        }

        // if none matched, set max ticks/power from those we found (even if no space)
        if (maxTicksFound > 0) this.maxProgress = maxTicksFound;
        this.currentPowerConsumption = maxPowerFound;

        return false;
    }

    private Optional<ShredderRecipe> getRecipe(int slot) {

        SimpleContainer inventory = new SimpleContainer(1);

        inventory.setItem(0, this.itemHandler.getStackInSlot(slot));

        return this.level.getRecipeManager().getRecipeFor(ShredderRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item, int idx) {

        boolean result = this.itemHandler.getStackInSlot(idx).isEmpty() ||
                this.itemHandler.getStackInSlot(idx).is(item);

//        System.out.println("[Debug] can insert item into output slot: " + result);

        return result;
    }

    private boolean canInsertAmountIntoOutputSlot(int count, int idx) {

        boolean result = this.itemHandler.getStackInSlot(idx).getCount() + count <=
                this.itemHandler.getStackInSlot(idx).getMaxStackSize();

//        System.out.println("[Debug] can insert amount into output slot: " + result);

        return result;
    }

    private boolean canConsumePower() {

        return getEnergy().compareTo(getMaxEnergy()) < 0;
    }

    public FloatingLong storedWattHoursClient = FloatingLong.ZERO;

    @Override
    public String formatWattHoursStored() {

        var value = storedWattHoursClient;

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
    public String formatWattsDischarge() {

        var value = translateWattHours(currentPowerConsumption);

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

    private void consumePower() {

        ItemStack stack = this.itemHandler.getStackInSlot(POWER_SLOT);

//        System.out.println("power stack has capability: " + power.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).isPresent());
//        if (isBatteryItem(stack))
//            System.out.println("stored: " + BatteryItem.getEnergyStored(stack) + " capacity: " + BatteryItem.getMaxEnergyStored(stack));


        if (!isEnergyItem(stack) || !canExtract(stack)) return;

        if(isBatteryItem(stack) || isSelfChargingBatteryItem(stack)) {

            // 1) How much can we extract from the item (simulate)?
            FloatingLong canExtractAmount = drainEnergy(stack, FloatingLong.create(String.format("%e", Double.MAX_VALUE)), true);

            // 2) How much would the machine accept (simulate)?
            FloatingLong wouldAccept = insertEnergy(canExtractAmount, true);

            // 3) Do the real transfer: insert into the machine, then drain the item
            FloatingLong actuallyInserted = insertEnergy(wouldAccept, false);

            FloatingLong actuallyDrained = drainEnergy(stack, actuallyInserted, false);

//            System.err.println("consumePower: canExtract=" + canExtractAmount + " wouldAccept=" + wouldAccept +
//                    " inserted=" + actuallyInserted + " drained=" + actuallyDrained + " storedEnergy=" + getEnergy());
        }
        else if(isEnergyItem(stack)) {

            // 1) How much can we extract from the item (simulate)?
            FloatingLong canExtractAmountFE = drainEnergy(stack, FloatingLong.create(String.format("%e", Double.MAX_VALUE)), true);

            FloatingLong canExtractAmountWh = canExtractAmountFE.divide(WATT_HOUR_TO_FE);

            // 2) How much would the machine accept (simulate)?
            FloatingLong wouldAcceptWh = insertEnergy(canExtractAmountWh, true);

            // 3) Do the real transfer: insert into the machine, then drain the item
            FloatingLong actuallyInsertedWh = insertEnergy(wouldAcceptWh, false);

            FloatingLong actuallyInsertedFE = actuallyInsertedWh.multiply(WATT_HOUR_TO_FE);

            FloatingLong wouldDrainFE = drainEnergy(stack, actuallyInsertedFE, true);

            FloatingLong actuallyDrainedFE = drainEnergy(stack, wouldDrainFE, false);

//            System.err.println("consumePower: canExtractFE=" + canExtractAmountFE + " canExtractWh=" + canExtractAmountWh
//                    + " wouldAcceptWh=" + wouldAcceptWh + " insertedWh=" + actuallyInsertedWh + " insertedFE=" + actuallyInsertedFE
//                    + " wouldDrainFE=" + wouldDrainFE + " drainedFE=" + actuallyDrainedFE + " storedEnergyWh=" + getEnergy());
        }

        updatePowerSlot();

//         Debug logging (remove or guard behind dev flag)
//        System.err.println("consumePower: canExtract=" + canExtractAmount + " wouldAccept=" + wouldAccept +
//                " inserted=" + actuallyInserted + " drained=" + actuallyDrained);
    }

    private void updatePowerSlot() {
        // run only on server
        if(level == null || level.isClientSide)
            return;

        ItemStack stack = itemHandler.getStackInSlot(POWER_SLOT);
        if (stack.isEmpty()) return;

        // write capability -> stack NBT if present
        stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).ifPresent(cap -> {
            CompoundTag capTag = new CompoundTag();
            // store the same key the battery reads in initCapabilities / getEnergyStored
            capTag.putString(WattHourStorage.TAG_STORED, cap.getWattageStored().toString());
            stack.getOrCreateTag().put("WattHourStorage", capTag);
        });

        // Force the ItemStackHandler to notice and sync the slot to clients.
        // setStackInSlot will call onContentsChanged in the handler which already calls setChanged().
        itemHandler.setStackInSlot(POWER_SLOT, stack.copy());

        // mark BE changed and request a block update so clients get the new slot contents
        setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }


    private boolean hasPower() {

        ItemStack item = this.itemHandler.getStackInSlot(POWER_SLOT);

        boolean hasPower = isEnergyItem(item) && canExtract(item);

//        System.out.println("[Debug] has power: " + hasPower);

        return hasPower;
    }

    public static boolean isEnergyItem(ItemStack stack) {
        return  stack.getCapability(HbmCapabilities.WATT_HOUR_STORAGE).isPresent()
                || stack.getItem() instanceof IEnergyItem
                || stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    public static boolean isBatteryItem(ItemStack stack) {

        return stack.getItem() instanceof BatteryItem;
    }

    public static boolean isSelfChargingBatteryItem(ItemStack stack) {

        return stack.getItem() instanceof SelfChargingBatteryItem;
    }

    public static FloatingLong drainEnergy(ItemStack stack, FloatingLong amount, boolean simulate) {

        if(isBatteryItem(stack))
            return BatteryItem.removeEnergy(stack, amount, simulate);
        else if(isSelfChargingBatteryItem(stack))
            return ((SelfChargingBatteryItem)stack.getItem()).getDischargeRate() != Double.POSITIVE_INFINITY ? FloatingLong.create(((SelfChargingBatteryItem)stack.getItem()).getDischargeRate()) : FloatingLong.create(String.format("%e", Double.MAX_VALUE));
        else if (stack.getCapability(ForgeCapabilities.ENERGY).isPresent()) {

            final AtomicInteger extracted = new AtomicInteger(0);

            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(cap -> {
                try {

                    var toExtract = Math.max(0, amount.intValue()); // clamp

                    extracted.set(cap.extractEnergy(toExtract, simulate));
//                    System.out.println("[In] amount=" + amount + " !multiplied=" + multiplied.get() + " !shaved=" + shaved.get() + " integer=" + integer.get() + " toExtract=" + toExtract.get() + " extracted=" + extracted.get());
                } catch (Exception e) {
                    // Worst-case fallback: try full-extract
                    extracted.set(cap.extractEnergy(Integer.MAX_VALUE, simulate));
                }
            });

            var atomicToFloatingLong = FloatingLong.create(extracted.get());
//            System.err.println("[Out] amount=" + amount + " !multiplied=" + multiplied.get() + " !shaved=" + shaved.get() + " integer=" + integer.get() + " toExtract=" + toExtract.get() + " extracted=" + extracted.get() + " atomicToFloatingLong=" + atomicToFloatingLong);
            return atomicToFloatingLong;
        }
        return FloatingLong.ZERO;
    }

    public static boolean canExtract(ItemStack stack) {

        if (isBatteryItem(stack))
            return BatteryItem.canExtract(stack);
        else if (isSelfChargingBatteryItem(stack))
            return true;
        else if (stack.getCapability(ForgeCapabilities.ENERGY).isPresent()) {
            return stack.getCapability(ForgeCapabilities.ENERGY)
                    .map(IEnergyStorage::canExtract)
                    .orElse(false);
        }
        return false;
    }

    private boolean hasEnergy() {

//        System.out.println("[Debug] has energy: " + (getEnergy() > 0) + ", energy: " + getEnergy());

        return getEnergy().compareTo(FloatingLong.ZERO) > 0;
    }

    private void consumeEnergy() {

        extractEnergy(currentPowerConsumption.multiply(WattHourStorage.MCSECOND_TO_TICK), false);
    }
}
