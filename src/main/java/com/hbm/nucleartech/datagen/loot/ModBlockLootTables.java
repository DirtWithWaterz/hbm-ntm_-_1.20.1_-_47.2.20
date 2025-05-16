package com.hbm.nucleartech.datagen.loot;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {


    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(RegisterBlocks.BLOCK_TITANIUM.get());
        this.dropSelf(RegisterBlocks.BLOCK_URANIUM.get());
        this.dropSelf(RegisterBlocks.RADIATION_DECONTAMINATOR.get());

        this.add(RegisterBlocks.ORE_TITANIUM.get(),
                block -> createOreDrop(RegisterBlocks.ORE_TITANIUM.get(), RegisterItems.RAW_TITANIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get(), RegisterItems.RAW_TITANIUM.get()));

        this.add(RegisterBlocks.ORE_URANIUM.get(),
                block -> createOreDrop(RegisterBlocks.ORE_URANIUM.get(), RegisterItems.RAW_URANIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_ORE_URANIUM.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_ORE_URANIUM.get(), RegisterItems.RAW_URANIUM.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {

        return RegisterBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
