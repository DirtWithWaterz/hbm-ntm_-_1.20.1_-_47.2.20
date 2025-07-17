package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {

        super(output, HBM.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        simpleItem(RegisterItems.PLATE_IRON);

        simpleItem(RegisterItems.INGOT_URANIUM);
        simpleItem(RegisterItems.INGOT_BERYLLIUM);
        simpleItem(RegisterItems.INGOT_TITANIUM);

        simpleItem(RegisterItems.RAW_TITANIUM);
        simpleItem(RegisterItems.RAW_URANIUM);

        simpleItem(RegisterItems.PILE_ROD_URANIUM);
        simpleItem(RegisterItems.BILLET_URANIUM);
        simpleItem(RegisterItems.CRYSTAL_URANIUM);
        simpleItem(RegisterItems.NUGGET_URANIUM);
        simpleItem(RegisterItems.POWDER_URANIUM);

        simpleItem(RegisterItems.REACHER);
        simpleItem(RegisterItems.GEIGER_COUNTER);

        simpleItem(RegisterItems.PLACEHOLDER);

        simpleItem(RegisterItems.M65_MASK);
        simpleItem(RegisterItems.GAS_MASK_FILTER);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {

        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HBM.MOD_ID, "item/" + item.getId().getPath()));
    }
}
