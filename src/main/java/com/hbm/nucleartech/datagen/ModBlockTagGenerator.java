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

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(RegisterBlocks.BLOCK_TITANIUM.get(),
                        RegisterBlocks.ORE_TITANIUM.get(),
                        RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(RegisterBlocks.ORE_TITANIUM.get()).addTag(Tags.Blocks.ORES);
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get()).addTag(Tags.Blocks.ORES);

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(RegisterBlocks.BLOCK_TITANIUM.get());
    }
}
