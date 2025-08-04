package com.hbm.nucleartech.screen;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class BurnerPressMenu extends AbstractContainerMenu {

    public final BurnerPressEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public BurnerPressMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {

        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public BurnerPressMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {

        super(RegisterMenuTypes.BURNER_PRESS_MENU.get(), pContainerId);

        checkContainerSize(inv, 4);
        blockEntity = ((BurnerPressEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {

            this.addSlot(new SlotItemHandler(iItemHandler, 0, 26, 53));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 80, 17));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 80, 53));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 140, 35));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {

        return data.get(0) > 0;
    }

    public int getScaledPressProgress() {
        // raw tick count and dynamic max
        float progress   = this.data.get(0);
        float maxProgress = this.data.get(1);
        final int STAMP_HEIGHT = 16;

        if (maxProgress <= 0f) {
            return 0;
        }

        // 1) get fractional part of progress/maxProgress
        float phase = (progress / maxProgress) % 1f;
        if (phase < 0f) phase += 1f;

        // 2) triangle wave: 0→1 over [0,0.5], then 1→0 over (0.5,1)
        float triangular;
        if (phase <= 0.5f) {
            triangular = phase * 2f;
        } else {
            triangular = (1f - phase) * 2f;
        }

        // 3) scale to pixel height
        return Math.round(triangular * STAMP_HEIGHT);
    }

    public int getScaledFuelProgress() {

        int fuel = this.data.get(4);
        int maxFuel = this.data.get(5);

        int fireStampSize = 14;

        if(fuel <= 0 || maxFuel <= 0)
            return 0;

        return (fuel * fireStampSize / maxFuel);
    }

    public int getHeatGaugeProgress() {

        int heat = this.data.get(2);
        int maxHeat = this.data.get(3);

        int idxSize = 12;

        if(heat <= 0 || maxHeat <= 0)
            return 0;

        return (heat * idxSize / maxHeat);
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, RegisterBlocks.BURNER_PRESS.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
    }

    private void addPlayerHotbar(Inventory playerInventory) {

        for(int i = 0; i < 9; i++)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
    }

}
