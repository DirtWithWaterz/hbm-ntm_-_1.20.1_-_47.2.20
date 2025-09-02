package com.hbm.nucleartech.screen;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.block.entity.ShredderEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;

public class ShredderMenu extends AbstractContainerMenu {

    public final ShredderEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public ShredderMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {

        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public ShredderMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {

        super(RegisterMenuTypes.SHREDDER_MENU.get(), pContainerId);

        checkContainerSize(inv, 30);
        blockEntity = ((ShredderEntity) entity);
        this.level = inv.player.level();
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {

            this.addSlot(new SlotItemHandler(iItemHandler, 0, 8, 108-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 44, 108-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 80, 108-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 44, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 62, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 5, 80, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 6, 44, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 7, 62, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 8, 80, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 9, 44, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 10, 62, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 11, 80, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 12, 116, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 13, 134, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 14, 152, 18-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 15, 116, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 16, 134, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 17, 152, 36-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 18, 116, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 19, 134, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 20, 152, 54-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 21, 116, 72-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 22, 134, 72-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 23, 152, 72-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 24, 116, 90-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 25, 134, 90-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 26, 152, 90-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 27, 116, 108-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 28, 134, 108-28));
            this.addSlot(new SlotItemHandler(iItemHandler, 29, 152, 108-28));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {

        return this.data.get(0) > 0;
    }

    public int getScaledShredderProgress() {

        int progress = this.data.get(0);
        int maxProgress = this.data.get(6);
        final int LENGTH = 33;

        return (progress * LENGTH / maxProgress);
    }

    public int getScaledEnergyProgress() {

//        System.out.println(this.data.get(1));

        return this.data.get(1);
    }

    public int[] getBladeDurability(byte side) {

        switch (side) {

            case 0 -> {

                int[] intAr = new int[]{this.data.get(2), this.data.get(4)};

//                System.out.println(Arrays.toString(Arrays.stream(intAr).toArray()) + " : left");

                return intAr;
            }
            case 1 -> {

                int[] intAr = new int[]{this.data.get(3), this.data.get(5)};

//                System.out.println(Arrays.toString(Arrays.stream(intAr).toArray()) + " : right");

                return intAr;
            }
        }
        return new int[]{-1, -1};
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
    private static final int TE_INVENTORY_SLOT_COUNT = 30;  // must be the number of slots you have!
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
                pPlayer, RegisterBlocks.SHREDDER.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {

        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 9; j++)
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, (84 + i * 18)+28));
    }

    private void addPlayerHotbar(Inventory playerInventory) {

        for(int i = 0; i < 9; i++)
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142+28));
    }

}
