package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.util.RegisterTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {

        super(p_275343_, p_275729_, p_275322_, HBM.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(RegisterTags.Items.PLATE_STAMPS)
                .add(SharedTagLists.PLATE_STAMPS.toArray(new Item[0]));
        tag(RegisterTags.Items.SHREDDER_BLADES)
                .add(SharedTagLists.SHREDDER_BLADES.toArray(new Item[0]));
    }

    public static class SharedTagLists {
        public static final List<Item> PLATE_STAMPS = List.of(
                RegisterItems.IRON_PLATE_STAMP.get()
        );
        public static final List<Item> SHREDDER_BLADES = List.of(
                RegisterItems.ALUMINUM_BLADE.get(),
                RegisterItems.GOLD_BLADE.get(),
                RegisterItems.IRON_BLADE.get(),
                RegisterItems.STEEL_BLADE.get(),
                RegisterItems.TITANIUM_BLADE.get(),
                RegisterItems.ADVANCED_BLADE.get(),
                RegisterItems.CMB_BLADE.get(),
                RegisterItems.SCHRABIDIUM_BLADE.get(),
                RegisterItems.DESH_BLADE.get()
        );
    }
}
