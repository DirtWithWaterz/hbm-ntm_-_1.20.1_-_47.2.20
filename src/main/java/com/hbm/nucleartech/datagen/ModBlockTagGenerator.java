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
                        RegisterBlocks.BROWN_THORIUM_ORE.get());
    }
}
