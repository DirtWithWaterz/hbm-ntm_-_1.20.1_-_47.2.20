package com.hbm.nucleartech.item.custom.base;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class StampItem extends Item {

    public StampItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isDamageable(@NotNull ItemStack stack) {
        return true;
    }
}
