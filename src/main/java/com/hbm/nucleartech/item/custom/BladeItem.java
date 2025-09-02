package com.hbm.nucleartech.item.custom;

import net.minecraft.world.item.Item;

public class BladeItem extends Item {

    private int idx;

    public BladeItem(Properties pProperties, int idx) {
        super(pProperties);
        this.idx = idx;
    }

    public int getIdx() {

        return idx;
    }
}
