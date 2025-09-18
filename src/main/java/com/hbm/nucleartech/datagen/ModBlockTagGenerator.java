package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,  @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, HBM.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(RegisterBlocks.TITANIUM_BLOCK.get(),
                        RegisterBlocks.TITANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(),
                        RegisterBlocks.URANIUM_BLOCK.get(),
                        RegisterBlocks.URANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(),
                        RegisterBlocks.SULFUR_ORE.get(),
                        RegisterBlocks.NITER_ORE.get(),
                        RegisterBlocks.TUNGSTEN_ORE.get(),
                        RegisterBlocks.ALUMINIUM_ORE.get(),
                        RegisterBlocks.FLUORITE_ORE.get(),
                        RegisterBlocks.BERYLLIUM_ORE.get(),
                        RegisterBlocks.LEAD_ORE.get(),
                        RegisterBlocks.LIGNITE_ORE.get(),
                        RegisterBlocks.ASBESTOS_ORE.get(),
                        RegisterBlocks.SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.AUSTRALIUM_ORE.get(),
                        RegisterBlocks.RARE_EARTH_ORE.get(),
                        RegisterBlocks.COBALT_ORE.get(),
                        RegisterBlocks.CINNABAR_ORE.get(),
                        RegisterBlocks.COLTAN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SULFUR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_NITER_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LEAD_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COBALT_ORE.get(),
                        RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COLTAN_ORE.get(),
                        RegisterBlocks.RADIATION_DECONTAMINATOR.get(),
                        RegisterBlocks.BURNER_PRESS.get(),
                        RegisterBlocks.BURNER_PRESS_PART.get(),
                        RegisterBlocks.RED_THORIUM_ORE.get(),
                        RegisterBlocks.ORANGE_THORIUM_ORE.get(),
                        RegisterBlocks.YELLOW_THORIUM_ORE.get(),
                        RegisterBlocks.WHITE_THORIUM_ORE.get(),
                        RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get(),
                        RegisterBlocks.BROWN_THORIUM_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(RegisterBlocks.TITANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(),
                        RegisterBlocks.BURNER_PRESS.get(),
                        RegisterBlocks.BURNER_PRESS_PART.get(),
                        RegisterBlocks.SULFUR_ORE.get(),
                        RegisterBlocks.NITER_ORE.get(),
                        RegisterBlocks.TUNGSTEN_ORE.get(),
                        RegisterBlocks.ALUMINIUM_ORE.get(),
                        RegisterBlocks.FLUORITE_ORE.get(),
                        RegisterBlocks.BERYLLIUM_ORE.get(),
                        RegisterBlocks.LEAD_ORE.get(),
                        RegisterBlocks.LIGNITE_ORE.get(),
                        RegisterBlocks.ASBESTOS_ORE.get(),
                        RegisterBlocks.SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.AUSTRALIUM_ORE.get(),
                        RegisterBlocks.RARE_EARTH_ORE.get(),
                        RegisterBlocks.COBALT_ORE.get(),
                        RegisterBlocks.CINNABAR_ORE.get(),
                        RegisterBlocks.COLTAN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SULFUR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_NITER_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LEAD_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COBALT_ORE.get(),
                        RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COLTAN_ORE.get(),
                        RegisterBlocks.TITANIUM_BLOCK.get(),
                        RegisterBlocks.BROWN_THORIUM_ORE.get(),
                        RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get(),
                        RegisterBlocks.WHITE_THORIUM_ORE.get(),
                        RegisterBlocks.YELLOW_THORIUM_ORE.get(),
                        RegisterBlocks.ORANGE_THORIUM_ORE.get(),
                        RegisterBlocks.RED_THORIUM_ORE.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(RegisterBlocks.URANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(),
                        RegisterBlocks.URANIUM_BLOCK.get());

        tag(Tags.Blocks.ORES)
                .add(RegisterBlocks.TITANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(),
                        RegisterBlocks.URANIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(),
                        RegisterBlocks.RED_THORIUM_ORE.get(),
                        RegisterBlocks.ORANGE_THORIUM_ORE.get(),
                        RegisterBlocks.YELLOW_THORIUM_ORE.get(),
                        RegisterBlocks.WHITE_THORIUM_ORE.get(),
                        RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get(),
                        RegisterBlocks.BROWN_THORIUM_ORE.get(),
                        RegisterBlocks.SULFUR_ORE.get(),
                        RegisterBlocks.NITER_ORE.get(),
                        RegisterBlocks.TUNGSTEN_ORE.get(),
                        RegisterBlocks.ALUMINIUM_ORE.get(),
                        RegisterBlocks.FLUORITE_ORE.get(),
                        RegisterBlocks.BERYLLIUM_ORE.get(),
                        RegisterBlocks.LEAD_ORE.get(),
                        RegisterBlocks.LIGNITE_ORE.get(),
                        RegisterBlocks.ASBESTOS_ORE.get(),
                        RegisterBlocks.SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.AUSTRALIUM_ORE.get(),
                        RegisterBlocks.RARE_EARTH_ORE.get(),
                        RegisterBlocks.COBALT_ORE.get(),
                        RegisterBlocks.CINNABAR_ORE.get(),
                        RegisterBlocks.COLTAN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SULFUR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_NITER_ORE.get(),
                        RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LEAD_ORE.get(),
                        RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get(),
                        RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get(),
                        RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get(),
                        RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COBALT_ORE.get(),
                        RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get(),
                        RegisterBlocks.DEEPSLATE_COLTAN_ORE.get());
    }
}
