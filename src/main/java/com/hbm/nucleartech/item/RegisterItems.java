package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.hazard.HazardItem;
import com.hbm.nucleartech.item.custom.*;
import com.hbm.nucleartech.item.custom.base.StampItem;
import com.hbm.nucleartech.item.special.CustomLoreItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HBM.MOD_ID);

    public static final RegistryObject<Item> IRON_PLATE = ITEMS.register("iron_plate",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BERYLLIUM_INGOT = ITEMS.register("beryllium_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_TITANIUM = ITEMS.register("raw_titanium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_INGOT = ITEMS.register("uranium_ingot",
            () -> new HazardItem(0.35, new Item.Properties()));

    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new HazardItem(0.035, new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_POWDER = ITEMS.register("uranium_powder",
            () -> new HazardItem(1.05, new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_NUGGET = ITEMS.register("uranium_nugget",
            () -> new HazardItem(0.035, new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_CRYSTAL = ITEMS.register("uranium_crystal",
            () -> new HazardItem(1.75, new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_BILLET = ITEMS.register("uranium_billet",
            () -> new HazardItem(0.175, new Item.Properties()));

    public static final RegistryObject<Item> URANIUM_PILE_ROD = ITEMS.register("uranium_pile_rod",
            () -> new HazardItem(0.525, new Item.Properties()));


    public static final RegistryObject<Item> REACHER = ITEMS.register("reacher",
            () -> new CustomLoreItem(new Item.Properties()));

    public static final RegistryObject<Item> GEIGER_COUNTER = ITEMS.register("geiger_counter",
            () -> new GeigerCounterItem(new Item.Properties()));

    public static final RegistryObject<Item> PLACEHOLDER = ITEMS.register("placeholder",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> M65_MASK = ITEMS.register("m65_mask",
            () -> new M65Item(new Item.Properties(), ArmorMaterials.IRON));

    public static final RegistryObject<Item> GAS_MASK_FILTER_MONO = ITEMS.register("gas_mask_filter_mono",
            () -> new FilterItem(new Item.Properties(), 12000));

    public static final RegistryObject<Item> GAS_MASK_FILTER = ITEMS.register("gas_mask_filter",
            () -> new FilterItem(new Item.Properties(), 18000));

    public static final RegistryObject<Item> GAS_MASK_FILTER_COMBO = ITEMS.register("gas_mask_filter_combo",
            () -> new FilterItem(new Item.Properties(), 24000));

    public static final RegistryObject<Item> GAS_MASK_FILTER_RADON = ITEMS.register("gas_mask_filter_radon",
            () -> new FilterItem(new Item.Properties(), 32000));

    public static final RegistryObject<Item> HAZMAT_HELMET = ITEMS.register("hazmat_helmet",
            () -> new HazmatHeadItem(new Item.Properties(), HbmArmorMaterials.HAZMAT));
    public static final RegistryObject<Item> HAZMAT_CHESTPLATE = ITEMS.register("hazmat_chestplate",
            () -> new HazmatItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.plate_only));
    public static final RegistryObject<Item> HAZMAT_LEGGINGS = ITEMS.register("hazmat_leggings",
            () -> new HazmatItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.legs_only));
    public static final RegistryObject<Item> HAZMAT_BOOTS = ITEMS.register("hazmat_boots",
            () -> new HazmatItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.boots_only));

    public static final RegistryObject<Item> HAZMAT_HELMET_RED = ITEMS.register("hazmat_helmet_red",
            () -> new HazmatHeadRedItem(new Item.Properties(), HbmArmorMaterials.HAZMAT));
    public static final RegistryObject<Item> HAZMAT_CHESTPLATE_RED = ITEMS.register("hazmat_chestplate_red",
            () -> new HazmatRedItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.plate_only));
    public static final RegistryObject<Item> HAZMAT_LEGGINGS_RED = ITEMS.register("hazmat_leggings_red",
            () -> new HazmatRedItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.legs_only));
    public static final RegistryObject<Item> HAZMAT_BOOTS_RED = ITEMS.register("hazmat_boots_red",
            () -> new HazmatRedItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.boots_only));

    public static final RegistryObject<Item> HAZMAT_HELMET_GREY = ITEMS.register("hazmat_helmet_grey",
            () -> new HazmatHeadGreyItem(new Item.Properties(), HbmArmorMaterials.HAZMAT));
    public static final RegistryObject<Item> HAZMAT_CHESTPLATE_GREY = ITEMS.register("hazmat_chestplate_grey",
            () -> new HazmatGreyItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.plate_only));
    public static final RegistryObject<Item> HAZMAT_LEGGINGS_GREY = ITEMS.register("hazmat_leggings_grey",
            () -> new HazmatGreyItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.legs_only));
    public static final RegistryObject<Item> HAZMAT_BOOTS_GREY = ITEMS.register("hazmat_boots_grey",
            () -> new HazmatGreyItem(new Item.Properties(), HbmArmorMaterials.HAZMAT, ArmorModHandler.boots_only));

    public static final RegistryObject<Item> BURNER_PRESS = ITEMS.register("burner_press",
            () -> new BurnerPressItem(RegisterBlocks.BURNER_PRESS.get(), new Item.Properties()));

    public static final RegistryObject<Item> IRON_PLATE_STAMP = ITEMS.register("iron_plate_stamp",
            () -> new StampItem(new Item.Properties().durability(64)));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
}
