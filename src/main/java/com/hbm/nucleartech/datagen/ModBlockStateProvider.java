package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {

        super(output, HBM.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(RegisterBlocks.BLOCK_TITANIUM);

        blockWithItem(RegisterBlocks.BLOCK_URANIUM);

        blockWithItem(RegisterBlocks.ORE_TITANIUM);
        blockWithItem(RegisterBlocks.DEEPSLATE_ORE_TITANIUM);

        blockWithItem(RegisterBlocks.ORE_URANIUM);
        blockWithItem(RegisterBlocks.DEEPSLATE_ORE_URANIUM);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {

        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
