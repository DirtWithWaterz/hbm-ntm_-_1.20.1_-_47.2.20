package com.hbm.nucleartech.block.entity;

import com.hbm.nucleartech.item.custom.base.StampItem;
import com.hbm.nucleartech.network.HbmPacketHandler;
import com.hbm.nucleartech.network.packet.ClientboundBurnerPressPacket;
import com.hbm.nucleartech.recipe.PressRecipe;
import com.hbm.nucleartech.screen.BurnerPressMenu;
import com.hbm.nucleartech.sound.RegisterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
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
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Optional;

public class BurnerPressEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {

    public static final String STAMP_ANIM = "animation.burner_press.stamp";
    public static final String LIFT_ANIM = "animation.burner_press.lift";
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public Player lastInteractedPlayer = null;
    public ItemStack lastInteractedPlayerHeldItem = null;

    private final ItemStackHandler itemHandler = new ItemStackHandler(4){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {

            if(slot == FUEL_SLOT && ForgeHooks.getBurnTime(stack, null) == 0)
                return false;
            else if(slot == STAMP_SLOT && !(stack.getItem() instanceof StampItem))
                return false;
            else if(slot == OUTPUT_SLOT)
                return false;

            return slot != LOCKED_SLOT && super.isItemValid(slot, stack);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == LOCKED_SLOT) {
                return ItemStack.EMPTY; // deny extraction
            }
            setChanged();
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {

            if (slot == LOCKED_SLOT)
                return stack; // deny insertion
            else if(slot == FUEL_SLOT && ForgeHooks.getBurnTime(stack, null) == 0)
                return stack;
            else if(slot == STAMP_SLOT && !(stack.getItem() instanceof StampItem))
                return stack;
            else if(slot == OUTPUT_SLOT)
                return stack;

            setChanged();
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            setChanged();
        }
    };

    private static final int FUEL_SLOT = 0;
    private static final int STAMP_SLOT = 1;
    private static final int INPUT_SLOT = 2;
    private static final int OUTPUT_SLOT = 3;

    private static int LOCKED_SLOT = -1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    public int progress = 0;
    public int maxProgress = 80;

    public static final int staticMaxProgress = 80;

    public int heat = 0;
    public int maxHeat = 7200;

    public float speedMod = 1;
    public float displaySpeed = 1;

    public int fuel = 0;
    public int maxFuel = 0;

    public boolean stamping = false;
    public boolean stamped = false;

    public BurnerPressEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegisterBlockEntities.BURNER_PRESS_ENTITY.get(), pPos, pBlockState);

        SingletonGeoAnimatable.registerSyncedAnimatable(this);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch(pIndex) {

                    case 0 -> BurnerPressEntity.this.progress;
                    case 1 -> BurnerPressEntity.this.maxProgress;
                    case 2 -> BurnerPressEntity.this.heat;
                    case 3 -> BurnerPressEntity.this.maxHeat;
                    case 4 -> BurnerPressEntity.this.fuel;
                    case 5 -> BurnerPressEntity.this.maxFuel;
                    case 6 -> (int)BurnerPressEntity.this.speedMod;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch(pIndex) {

                    case 0 -> BurnerPressEntity.this.progress = pValue;
                    case 1 -> BurnerPressEntity.this.maxProgress = pValue;
                    case 2 -> BurnerPressEntity.this.heat = pValue;
                    case 3 -> BurnerPressEntity.this.maxHeat = pValue;
                    case 4 -> BurnerPressEntity.this.fuel = pValue;
                    case 5 -> BurnerPressEntity.this.maxFuel = pValue;
                    case 6 -> BurnerPressEntity.this.speedMod = pValue;
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
    public Component getDisplayName() {
        return Component.translatable("block.hbm.burner_press");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new BurnerPressMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {

        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("burner_press.progress", progress);
        pTag.putInt("burner_press.max_progress", maxProgress);
        pTag.putInt("burner_press.heat", heat);
        pTag.putInt("burner_press.max_heat", maxHeat);
        pTag.putInt("burner_press.fuel", fuel);
        pTag.putInt("burner_press.max_fuel", maxFuel);

        pTag.putFloat("burner_press.speed_mod", speedMod);
        pTag.putFloat("burner_press.display_speed", displaySpeed);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {

        super.load(pTag);

        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("burner_press.progress");
        maxProgress = pTag.getInt("burner_press.max_progress");
        heat = pTag.getInt("burner_press.heat");
        maxHeat = pTag.getInt("burner_press.max_heat");
        fuel = pTag.getInt("burner_press.fuel");
        maxFuel = pTag.getInt("burner_press.max_fuel");

        speedMod = pTag.getFloat("burner_press.speed_mod");
        displaySpeed = pTag.getFloat("burner_press.display_speed");
    }

//    public boolean animationDrop = false;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        // Hey dumbass, when you're not sleep-deprived, try making the animation one whole thing, and suck it up- ;3
        // fuck you
        // Still not quite there yet, keep trying stupid, you're getting closer <3
        // happy now-?
        // yup, good job ^^ (now make the item models that go inside)
        // aaahahhhHHHHAHHAHAHAHHAAHHHAHAHADaaagydhAUYDGAWDYGAKJDUYAWGDUYAKTWGVDAW

        controllers.add(new AnimationController<>(this, "stamp_c", 0, state -> {

            BlockEntity e = state.getData(DataTickets.BLOCK_ENTITY);

//            state.setAnimation(RawAnimation.begin().thenLoop(STAMP_ANIM));

            if(e instanceof BurnerPressEntity burnerPress) {

                float targetSpeed = burnerPress.speedMod;

                // Smoothly blend displaySpeed toward targetSpeed
                float lerpFactor = 0.5f; // Smaller = smoother, larger = faster change

                float oldDS = burnerPress.displaySpeed;

                float diff = oldDS - targetSpeed;

                if(Mth.abs(diff) > 0.25f)
                    targetSpeed = diff >= 0 ? oldDS - 0.25f : oldDS + 0.25f;

                burnerPress.displaySpeed = simpleLerp(lerpFactor, oldDS, targetSpeed);

                if(Mth.abs(diff) < 0.5f)
                    burnerPress.displaySpeed = targetSpeed;


                if(burnerPress.stamping && !burnerPress.stamped) {

                    state.setAnimation(RawAnimation.begin().thenPlay(STAMP_ANIM));
                }
                else {

                    state.setAnimation(RawAnimation.begin().thenPlay(LIFT_ANIM));
                }

//                System.out.println("[Animation] old: " + oldDS + ", target: " + targetSpeed + ", new: " + burnerPress.displaySpeed);

//                float animTick = Math.round((float)state.getAnimationTick());
//
//                int seeked = Math.round(seekDecimal(animTick / burnerPress.maxProgress) * burnerPress.maxProgress);

//                System.out.println("[Animation] anim tick: " + seeked + ", check: " + Math.round(burnerPress.maxProgress * 0.3125f));
//                if((seeked == Math.round(burnerPress.maxProgress * 0.3125f)) && burnerPress.stamping && !burnerPress.stamped) {
//
//                    Minecraft.getInstance().level.playLocalSound(burnerPress.getBlockPos(), RegisterSounds.BURNER_PRESS_STAMP.get(), SoundSource.BLOCKS, 1, 1, false);
//                    burnerPress.stamped = true;
//                }

                state.setControllerSpeed(burnerPress.displaySpeed);

                if(burnerPress.displaySpeed == 1)
                    return PlayState.STOP;

                return PlayState.CONTINUE;
            }
            System.err.println("not correct entity");
                return PlayState.STOP;
        })/*.triggerableAnim("stamp", RawAnimation.begin().thenPlay(STAMP_ANIM))*/);
    }

    float simpleLerp(float factor, float start, float end) {

        return start + (end - start) * factor;
    }
    float seekDecimal(float input) {

        return input - (int)input;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition, worldPosition.offset(1, 3, 1));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object blockEntity) {
        return RenderUtils.getCurrentTick();
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {

//        System.out.println("[Debug] ticking");

        if(hasEnergy())
            incrementHeat();
        else if(hasFuel())
            consumeFuel();
        else
            decrementHeat();

        updateSpeedModifier();

        lockInputSlot(hasRecipe() && hasHeat() && hasUsableStamp());

        placeInputModelOnPress();

        if(hasRecipe() && inSolidProgress() && hasUsableStamp() && hasHeat()) {

            increaseProgress();

            stamping = true;

            if(!inSolidProgress()) {

                stamped = true;
                lockInputSlot(false);
                getLevel().playSound(null, worldPosition, RegisterSounds.PRESS_OPERATE.get(), SoundSource.BLOCKS, 1, 1);
                craftItem();
                damageStamp(pPos);
                lockInputSlot(hasRecipe() && hasHeat() && hasUsableStamp());
            }
        }
        else if(hasRecipe() && !inSolidProgress() && hasHeat()) {

            increaseProgress();

            stamping = true;

            if(hasProgressFinished())
                resetProgress(false);
        }
        else
            resetProgress(true);

        ClientboundBurnerPressPacket packet = new ClientboundBurnerPressPacket(
                pPos.getX(), pPos.getY(), pPos.getZ(), stamping, progress, maxProgress, heat, maxHeat, fuel, maxFuel, speedMod, stamped, displayStack
        );

        HbmPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pPos)), packet);

        setChanged(pLevel, pPos, pState);
    }

    private ItemStack displayStack = ItemStack.EMPTY;

    private void placeInputModelOnPress() {

        ItemStack input = itemHandler.getStackInSlot(INPUT_SLOT);
        if (!input.isEmpty()) {

            displayStack = input.copy();
            displayStack.setCount(1);
        } else {
            displayStack = ItemStack.EMPTY;
        }
    }

    public ItemStack getDisplayStack() {

        return displayStack;
    }

    public void setDisplayStack(ItemStack stack) {

        displayStack = stack;
    }

    private void updateSpeedModifier() {

        speedMod = (getHeatGaugeProgress() - 1) / 2.5f;

        if(!hasRecipe() || !hasUsableStamp() || !hasHeat())
            speedMod = 1;

        speedMod = Mth.clamp(speedMod, 1f, 4.4f);

        maxProgress = (int)Mth.clamp(staticMaxProgress/speedMod, 0, 80);
    }

    private void damageStamp(BlockPos pos) {

        ItemStack stack = this.itemHandler.getStackInSlot(STAMP_SLOT);

        stack.hurt(1, level.random, null);

        if(stack.getDamageValue() >= stack.getMaxDamage()) {

            itemHandler.setStackInSlot(STAMP_SLOT, ItemStack.EMPTY);
            level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean hasUsableStamp() {

        boolean result = this.itemHandler.getStackInSlot(STAMP_SLOT).getItem() instanceof StampItem;

//        System.out.println("[Debug] has usable stamp: " + result);

        return result;
    }

    private void resetProgress(boolean lerp) {

        if(lerp) {

            progress = (int)Mth.lerp(0.05f, progress, 0);
            progress = progress <= 1f ? 0 : progress;
        }
        else
            progress = 0;
        stamping = false;
        stamped = false;
    }

    private void craftItem() {

        Optional<PressRecipe> recipe = getCurrentRecipe();

        ItemStack result = recipe.get().getResultItem(null);

        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
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

    private void lockInputSlot(boolean lock) {

//        System.out.println("[Debug] lock input slot: " + lock);

        LOCKED_SLOT = lock ? INPUT_SLOT : -1;
    }

    private boolean inSolidProgress() {

        boolean result = progress <= (maxProgress/2f);

//        System.out.println("[Debug] in solid progress: " + result);

        return result;
    }

    private boolean hasRecipe() {

        Optional<PressRecipe> recipe = getCurrentRecipe();

        if(recipe.isEmpty())
            return false;

        ItemStack result = recipe.get().getResultItem(null);

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<PressRecipe> getCurrentRecipe() {

        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());

        for(int i = 0; i < itemHandler.getSlots(); i++)
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));

        return this.level.getRecipeManager().getRecipeFor(PressRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {

        boolean result = this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);

//        System.out.println("[Debug] can insert item into output slot: " + result);

        return result;
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {

        boolean result = this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <=
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();

//        System.out.println("[Debug] can insert amount into output slot: " + result);

        return result;
    }

    private void decrementHeat() {

        heat -= 14;

        heat = Mth.clamp(heat, 0, maxHeat);

//        System.out.println("[Debug] heat: " + heat);

    }

    private void consumeFuel() {

        ItemStack item = this.itemHandler.getStackInSlot(FUEL_SLOT);
        this.itemHandler.extractItem(FUEL_SLOT, 1, false);
        fuel = ForgeHooks.getBurnTime(item, null);
        maxFuel = ForgeHooks.getBurnTime(item, null);

//        System.out.println("[Debug] item burn time: " + ForgeHooks.getBurnTime(item, null));

    }

    private boolean hasFuel() {

        ItemStack item = this.itemHandler.getStackInSlot(FUEL_SLOT);

        boolean hasFuel = ForgeHooks.getBurnTime(item, null) != 0;

//        System.out.println("[Debug] has fuel: " + hasFuel);

        return hasFuel;
    }

    private void incrementHeat() {

        heat += 14;
        fuel -= 10;

        heat = Mth.clamp(heat, 0, maxHeat);
        fuel = Mth.clamp(fuel, 0, Integer.MAX_VALUE);

//        System.out.println("[Debug] heat: " + heat + ", fuel level: " + fuel);
    }

    private boolean hasEnergy() {

//        System.out.println("[Debug] has energy: " + (fuel > 0));

        return fuel > 0;
    }

    private boolean hasHeat() {

        boolean result = getHeatGaugeProgress() >= 4;

//        System.out.println("[Debug] has heat: " + result);

        return result;
    }

    private int getHeatGaugeProgress() {

        int idxSize = 12;

        if(heat <= 0 || maxHeat <= 0)
            return 0;

        return (heat * idxSize / maxHeat);
    }
}
