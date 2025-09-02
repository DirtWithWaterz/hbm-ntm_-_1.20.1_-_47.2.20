package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {

        super(output, HBM.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        blockWithItem(RegisterBlocks.WASTE_BLOCK);

        blockWithItem(RegisterBlocks.TITANIUM_BLOCK);

        blockWithItem(RegisterBlocks.URANIUM_BLOCK);

        blockWithItem(RegisterBlocks.TITANIUM_ORE);
        blockWithItem(RegisterBlocks.DEEPSLATE_TITANIUM_ORE);

        blockWithItem(RegisterBlocks.URANIUM_ORE);
        blockWithItem(RegisterBlocks.DEEPSLATE_URANIUM_ORE);

        blockWithItem(RegisterBlocks.RED_THORIUM_ORE);
        blockWithItem(RegisterBlocks.ORANGE_THORIUM_ORE);
        blockWithItem(RegisterBlocks.YELLOW_THORIUM_ORE);
        blockWithItem(RegisterBlocks.WHITE_THORIUM_ORE);
        blockWithItem(RegisterBlocks.LIGHT_GRAY_THORIUM_ORE);
        blockWithItem(RegisterBlocks.BROWN_THORIUM_ORE);

        blockWithItem(RegisterBlocks.RADIATION_DECONTAMINATOR,
                new ResourceLocation("hbm:block/radiation_decontaminator_top"),
                new ResourceLocation("hbm:block/radiation_decontaminator_side"),
                new ResourceLocation("hbm:block/radiation_decontaminator_side"),
                new ResourceLocation("hbm:block/radiation_decontaminator_side"),
                new ResourceLocation("hbm:block/radiation_decontaminator_side"));

        cubeBottomTopBlockWithItem(RegisterBlocks.DEAD_GRASS,
                new ResourceLocation("hbm:block/dead_grass_top"),
                new ResourceLocation("minecraft:block/dirt"),
                new ResourceLocation("hbm:block/dead_grass_side"));

        blockWithItem(RegisterBlocks.RAD_RESISTANT_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {

        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject, ResourceLocation bottom, ResourceLocation side, ResourceLocation top) {

//        System.err.println(blockRegistryObject.getId().getPath());
        simpleBlockWithItem(blockRegistryObject.get(), models().cube(blockRegistryObject.getId().getPath(), bottom, top, side, side, side, side));
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject, ResourceLocation top, ResourceLocation bottom, ResourceLocation side, ResourceLocation front, ResourceLocation particle) {
        String name = blockRegistryObject.getId().getPath();

        // Create the model using orientable_with_bottom
        ModelFile model = models()
                .withExistingParent(name, mcLoc("block/orientable_with_bottom"))
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("side", side)
                .texture("front", front)
                .texture("particle", particle);

        // Register blockstate with facing direction
        horizontalBlock(blockRegistryObject.get(), model);

        // Register the item model to point to the block model
        itemModels().getBuilder(name).parent(model);
    }

    private void cubeBottomTopBlockWithItem(RegistryObject<Block> blockRegistryObject, ResourceLocation top, ResourceLocation bottom, ResourceLocation side) {
        String name = blockRegistryObject.getId().getPath();

        ModelFile model = models()
                .cubeBottomTop(name, side, bottom, top);

        simpleBlockWithItem(blockRegistryObject.get(), model);
    }
}
