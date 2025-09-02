package com.hbm.nucleartech.util;

import com.hbm.nucleartech.HBM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class RegisterTags {

    public static class Blocks {

        private static TagKey<Block> tag(String name) {

            return BlockTags.create(new ResourceLocation(HBM.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> PLATE_STAMPS = tag("plate_stamps");
        public static final TagKey<Item> SHREDDER_BLADES = tag("shredder_blades");

        private static TagKey<Item> tag(String name) {

            return ItemTags.create(new ResourceLocation(HBM.MOD_ID, name));
        }
    }
}
