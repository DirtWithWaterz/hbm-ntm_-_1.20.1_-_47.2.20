package com.hbm.nucleartech.datagen.loot;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {


    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(RegisterBlocks.WASTE_BLOCK.get());
        this.dropSelf(RegisterBlocks.TITANIUM_BLOCK.get());
        this.dropSelf(RegisterBlocks.URANIUM_BLOCK.get());
        this.dropSelf(RegisterBlocks.RADIATION_DECONTAMINATOR.get());

        this.add(RegisterBlocks.TITANIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.TITANIUM_ORE.get(), RegisterItems.RAW_TITANIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(), RegisterItems.RAW_TITANIUM.get()));

        this.add(RegisterBlocks.URANIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.URANIUM_ORE.get(), RegisterItems.RAW_URANIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(), RegisterItems.RAW_URANIUM.get()));

        this.add(RegisterBlocks.RED_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.RED_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));
        this.add(RegisterBlocks.ORANGE_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.ORANGE_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));
        this.add(RegisterBlocks.YELLOW_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.YELLOW_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));
        this.add(RegisterBlocks.WHITE_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.WHITE_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));
        this.add(RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));
        this.add(RegisterBlocks.BROWN_THORIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.BROWN_THORIUM_ORE.get(), RegisterItems.THORIUM_SHALE.get()));

        this.dropSelf(RegisterBlocks.RAD_RESISTANT_BLOCK.get());

        this.dropSelf(RegisterBlocks.DEAD_GRASS.get());

        this.dropSelf(RegisterBlocks.BURNER_PRESS.get());

        this.dropSelf(RegisterBlocks.SHREDDER.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {

        return RegisterBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
