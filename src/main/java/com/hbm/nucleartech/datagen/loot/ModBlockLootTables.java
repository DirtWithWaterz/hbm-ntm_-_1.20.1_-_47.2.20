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

        this.add(RegisterBlocks.SULFUR_ORE.get(),
                block -> createOreDrop(RegisterBlocks.SULFUR_ORE.get(), RegisterItems.RAW_SULFUR.get()));
        this.add(RegisterBlocks.DEEPSLATE_SULFUR_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_SULFUR_ORE.get(), RegisterItems.RAW_SULFUR.get()));

        this.add(RegisterBlocks.NITER_ORE.get(),
                block -> createOreDrop(RegisterBlocks.NITER_ORE.get(), RegisterItems.RAW_NITER.get()));
        this.add(RegisterBlocks.DEEPSLATE_NITER_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_NITER_ORE.get(), RegisterItems.RAW_NITER.get()));

        this.add(RegisterBlocks.TUNGSTEN_ORE.get(),
                block -> createOreDrop(RegisterBlocks.TUNGSTEN_ORE.get(), RegisterItems.RAW_TUNGSTEN.get()));
        this.add(RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get(), RegisterItems.RAW_TUNGSTEN.get()));

        this.add(RegisterBlocks.ALUMINIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.ALUMINIUM_ORE.get(), RegisterItems.RAW_ALUMINIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get(), RegisterItems.RAW_ALUMINIUM.get()));

        this.add(RegisterBlocks.FLUORITE_ORE.get(),
                block -> createOreDrop(RegisterBlocks.FLUORITE_ORE.get(), RegisterItems.RAW_FLUORITE.get()));
        this.add(RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get(), RegisterItems.RAW_FLUORITE.get()));

        this.add(RegisterBlocks.BERYLLIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.BERYLLIUM_ORE.get(), RegisterItems.RAW_BERYLLIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get(), RegisterItems.RAW_BERYLLIUM.get()));

        this.add(RegisterBlocks.LEAD_ORE.get(),
                block -> createOreDrop(RegisterBlocks.LEAD_ORE.get(), RegisterItems.RAW_LEAD.get()));
        this.add(RegisterBlocks.DEEPSLATE_LEAD_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_LEAD_ORE.get(), RegisterItems.RAW_LEAD.get()));

        this.add(RegisterBlocks.LIGNITE_ORE.get(),
                block -> createOreDrop(RegisterBlocks.LIGNITE_ORE.get(), RegisterItems.RAW_LIGNITE.get()));
        this.add(RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get(), RegisterItems.RAW_LIGNITE.get()));

        this.add(RegisterBlocks.ASBESTOS_ORE.get(),
                block -> createOreDrop(RegisterBlocks.ASBESTOS_ORE.get(), RegisterItems.RAW_ASBESTOS.get()));
        this.add(RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get(), RegisterItems.RAW_ASBESTOS.get()));

        this.add(RegisterBlocks.SCHRABIDIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.SCHRABIDIUM_ORE.get(), RegisterItems.RAW_SCHRABIDIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get(), RegisterItems.RAW_SCHRABIDIUM.get()));

        this.add(RegisterBlocks.AUSTRALIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.AUSTRALIUM_ORE.get(), RegisterItems.RAW_AUSTRALIUM.get()));
        this.add(RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get(), RegisterItems.RAW_AUSTRALIUM.get()));

        this.add(RegisterBlocks.RARE_EARTH_ORE.get(),
                block -> createOreDrop(RegisterBlocks.RARE_EARTH_ORE.get(), RegisterItems.RAW_RARE_EARTH.get()));
        this.add(RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get(), RegisterItems.RAW_RARE_EARTH.get()));

        this.add(RegisterBlocks.COBALT_ORE.get(),
                block -> createOreDrop(RegisterBlocks.COBALT_ORE.get(), RegisterItems.RAW_COBALT.get()));
        this.add(RegisterBlocks.DEEPSLATE_COBALT_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_COBALT_ORE.get(), RegisterItems.RAW_COBALT.get()));

        this.add(RegisterBlocks.CINNABAR_ORE.get(),
                block -> createOreDrop(RegisterBlocks.CINNABAR_ORE.get(), RegisterItems.RAW_CINNABAR.get()));
        this.add(RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get(), RegisterItems.RAW_CINNABAR.get()));

        this.add(RegisterBlocks.COLTAN_ORE.get(),
                block -> createOreDrop(RegisterBlocks.COLTAN_ORE.get(), RegisterItems.RAW_COLTAN.get()));
        this.add(RegisterBlocks.DEEPSLATE_COLTAN_ORE.get(),
                block -> createOreDrop(RegisterBlocks.DEEPSLATE_COLTAN_ORE.get(), RegisterItems.RAW_COLTAN.get()));

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
