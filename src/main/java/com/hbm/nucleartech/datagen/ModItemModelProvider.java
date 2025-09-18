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

        simpleItem(RegisterItems.IRON_PLATE);

        simpleItem(RegisterItems.URANIUM_INGOT);
        simpleItem(RegisterItems.BERYLLIUM_INGOT);
        simpleItem(RegisterItems.TITANIUM_INGOT);
        simpleItem(RegisterItems.SULFUR_INGOT);
        simpleItem(RegisterItems.NITER_INGOT);
        simpleItem(RegisterItems.TUNGSTEN_INGOT);
        simpleItem(RegisterItems.ALUMINIUM_INGOT);
        simpleItem(RegisterItems.FLUORITE_INGOT);
        simpleItem(RegisterItems.LEAD_INGOT);
        simpleItem(RegisterItems.LIGNITE_INGOT);
        simpleItem(RegisterItems.ASBESTOS_INGOT);
        simpleItem(RegisterItems.SCHRABIDIUM_INGOT);
        simpleItem(RegisterItems.AUSTRALIUM_INGOT);
        simpleItem(RegisterItems.RARE_EARTH_INGOT);
        simpleItem(RegisterItems.COBALT_INGOT);
        simpleItem(RegisterItems.CINNABAR_INGOT);
        simpleItem(RegisterItems.COLTAN_INGOT);

        simpleItem(RegisterItems.RAW_TITANIUM);
        simpleItem(RegisterItems.RAW_URANIUM);
        simpleItem(RegisterItems.RAW_SULFUR);
        simpleItem(RegisterItems.RAW_NITER);
        simpleItem(RegisterItems.RAW_TUNGSTEN);
        simpleItem(RegisterItems.RAW_ALUMINIUM);
        simpleItem(RegisterItems.RAW_FLUORITE);
        simpleItem(RegisterItems.RAW_BERYLLIUM);
        simpleItem(RegisterItems.RAW_LEAD);
        simpleItem(RegisterItems.RAW_LIGNITE);
        simpleItem(RegisterItems.RAW_ASBESTOS);
        simpleItem(RegisterItems.RAW_SCHRABIDIUM);
        simpleItem(RegisterItems.RAW_AUSTRALIUM);
        simpleItem(RegisterItems.RAW_RARE_EARTH);
        simpleItem(RegisterItems.RAW_COBALT);
        simpleItem(RegisterItems.RAW_CINNABAR);
        simpleItem(RegisterItems.RAW_COLTAN);

        simpleItem(RegisterItems.URANIUM_PILE_ROD);
        simpleItem(RegisterItems.URANIUM_BILLET);
        simpleItem(RegisterItems.URANIUM_CRYSTAL);
        simpleItem(RegisterItems.URANIUM_NUGGET);
        simpleItem(RegisterItems.URANIUM_POWDER);

        simpleItem(RegisterItems.THORIUM_SHALE);
        simpleItem(RegisterItems.RAW_THORIUM);
        simpleItem(RegisterItems.THORIUM_INGOT);
        simpleItem(RegisterItems.THORIUM_POWDER);

        simpleItem(RegisterItems.ALUMINUM_BLADE);
        simpleItem(RegisterItems.GOLD_BLADE);
        simpleItem(RegisterItems.IRON_BLADE);
        simpleItem(RegisterItems.STEEL_BLADE);
        simpleItem(RegisterItems.TITANIUM_BLADE);
        simpleItem(RegisterItems.ADVANCED_BLADE);
        simpleItem(RegisterItems.CMB_BLADE);
        simpleItem(RegisterItems.SCHRABIDIUM_BLADE);
        simpleItem(RegisterItems.DESH_BLADE);

        simpleItem(RegisterItems.GENERIC_BATTERY);
        simpleItem(RegisterItems.ADVANCED_4_CELL);
        simpleItem(RegisterItems.ADVANCED_12_CELL);
        simpleItem(RegisterItems.ADVANCED_CELL);
        simpleItem(RegisterItems.ADVANCED_BATTERY);
        simpleItem(RegisterItems.AMERICIUM_SC_BATTERY);
        simpleItem(RegisterItems.CREATIVE_BATTERY);
        simpleItem(RegisterItems.GOLD_SC_BATTERY);
        simpleItem(RegisterItems.LEAD_SC_BATTERY);
        simpleItem(RegisterItems.LITHIUM_3_CELL);
        simpleItem(RegisterItems.LITHIUM_6_CELL);
        simpleItem(RegisterItems.LITHIUM_BATTERY);
        simpleItem(RegisterItems.LITHIUM_CELL);
        simpleItem(RegisterItems.PLUTONIUM_SC_BATTERY);
        simpleItem(RegisterItems.POLONIUM_SC_BATTERY);
        simpleItem(RegisterItems.POTATO_BATTERY);
        simpleItem(RegisterItems.REDSTONE_6_CELL);
        simpleItem(RegisterItems.REDSTONE_24_CELL);
        simpleItem(RegisterItems.REDSTONE_CELL);
        simpleItem(RegisterItems.SCHRABIDIUM_2_CELL);
        simpleItem(RegisterItems.SCHRABIDIUM_4_CELL);
        simpleItem(RegisterItems.SCHRABIDIUM_CELL);
        simpleItem(RegisterItems.SCHRABIDIUM_BATTERY);
        simpleItem(RegisterItems.SPARK_6_CELL);
        simpleItem(RegisterItems.SPARK_BATTERY);
        simpleItem(RegisterItems.SPARK_CAR_BATTERY);
        simpleItem(RegisterItems.SPARK_100_CELL);
        simpleItem(RegisterItems.TECHNETIUM_SC_BATTERY);
        simpleItem(RegisterItems.TRIXITE_BATTERY);
        simpleItem(RegisterItems.URANIUM_SC_BATTERY);
        simpleItem(RegisterItems.SPARK_1000_CELL);
        simpleItem(RegisterItems.SPARK_2500_CELL);
        simpleItem(RegisterItems.SPARK_10000_CELL);
        simpleItem(RegisterItems.SPARK_POWER_CELL);
        simpleItem(RegisterItems.ELECTRONIUM_CUBE);
        simpleItem(RegisterItems.POTATOS_BATTERY);
        simpleItem(RegisterItems.INFINITE_FUSION_CORE);
        simpleItem(RegisterItems.MAKESHIFT_ENERGY_CORE);

        simpleItem(RegisterItems.GENERIC_BATTERY_EMPTY);
        simpleItem(RegisterItems.ADVANCED_4_CELL_EMPTY);
        simpleItem(RegisterItems.ADVANCED_12_CELL_EMPTY);
        simpleItem(RegisterItems.ADVANCED_CELL_EMPTY);
        simpleItem(RegisterItems.ADVANCED_BATTERY_EMPTY);
        simpleItem(RegisterItems.LITHIUM_3_CELL_EMPTY);
        simpleItem(RegisterItems.LITHIUM_6_CELL_EMPTY);
        simpleItem(RegisterItems.LITHIUM_BATTERY_EMPTY);
        simpleItem(RegisterItems.LITHIUM_CELL_EMPTY);
        simpleItem(RegisterItems.REDSTONE_6_CELL_EMPTY);
        simpleItem(RegisterItems.REDSTONE_24_CELL_EMPTY);
        simpleItem(RegisterItems.REDSTONE_CELL_EMPTY);
        simpleItem(RegisterItems.SCHRABIDIUM_2_CELL_EMPTY);
        simpleItem(RegisterItems.SCHRABIDIUM_4_CELL_EMPTY);
        simpleItem(RegisterItems.SCHRABIDIUM_CELL_EMPTY);
        simpleItem(RegisterItems.SCHRABIDIUM_BATTERY_EMPTY);
        simpleItem(RegisterItems.SPARK_6_CELL_EMPTY);
        simpleItem(RegisterItems.SPARK_BATTERY_EMPTY);
        simpleItem(RegisterItems.SPARK_CAR_BATTERY_EMPTY);
        simpleItem(RegisterItems.SPARK_100_CELL_EMPTY);
        simpleItem(RegisterItems.TRIXITE_BATTERY_EMPTY);
        simpleItem(RegisterItems.SPARK_1000_CELL_EMPTY);
        simpleItem(RegisterItems.SPARK_2500_CELL_EMPTY);
        simpleItem(RegisterItems.SPARK_10000_CELL_EMPTY);
        simpleItem(RegisterItems.SPARK_POWER_CELL_EMPTY);
        simpleItem(RegisterItems.ELECTRONIUM_CUBE_EMPTY);

        simpleItem(RegisterItems.REACHER);
        simpleItem(RegisterItems.GEIGER_COUNTER);

        simpleItem(RegisterItems.PLACEHOLDER);

        simpleItem(RegisterItems.M65_MASK);

        simpleItem(RegisterItems.GAS_MASK_FILTER_MONO);
        simpleItem(RegisterItems.GAS_MASK_FILTER);
        simpleItem(RegisterItems.GAS_MASK_FILTER_COMBO);
        simpleItem(RegisterItems.GAS_MASK_FILTER_RADON);

        simpleItem(RegisterItems.HAZMAT_HELMET);
        simpleItem(RegisterItems.HAZMAT_CHESTPLATE);
        simpleItem(RegisterItems.HAZMAT_LEGGINGS);
        simpleItem(RegisterItems.HAZMAT_BOOTS);

        simpleItem(RegisterItems.HAZMAT_HELMET_RED);
        simpleItem(RegisterItems.HAZMAT_CHESTPLATE_RED);
        simpleItem(RegisterItems.HAZMAT_LEGGINGS_RED);
        simpleItem(RegisterItems.HAZMAT_BOOTS_RED);

        simpleItem(RegisterItems.HAZMAT_HELMET_GREY);
        simpleItem(RegisterItems.HAZMAT_CHESTPLATE_GREY);
        simpleItem(RegisterItems.HAZMAT_LEGGINGS_GREY);
        simpleItem(RegisterItems.HAZMAT_BOOTS_GREY);

        simpleItem(RegisterItems.IRON_PLATE_STAMP);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {

        return withExistingParent(item.getId().getPath(),
                ResourceLocation.tryParse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "item/" + item.getId().getPath().replace("_empty", "")));
    }
}
