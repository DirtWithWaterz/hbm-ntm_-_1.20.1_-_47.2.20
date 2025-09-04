package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.capability.energy.WattHourStorage;
import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.hazard.HazardItem;
import com.hbm.nucleartech.item.custom.*;
import com.hbm.nucleartech.item.custom.base.StampItem;
import com.hbm.nucleartech.item.special.CustomLoreItem;
import com.hbm.nucleartech.util.FloatingLong;
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

    public static final RegistryObject<Item> THORIUM_SHALE = ITEMS.register("thorium_shale",
            () -> new HazardItem(0.01, new Item.Properties()));

    public static final RegistryObject<Item> RAW_THORIUM = ITEMS.register("raw_thorium",
            () -> new HazardItem(0.01, new Item.Properties()));

    public static final RegistryObject<Item> THORIUM_POWDER = ITEMS.register("thorium_powder",
            () -> new HazardItem(0.3, new Item.Properties()));

    public static final RegistryObject<Item> THORIUM_INGOT = ITEMS.register("thorium_ingot",
            () -> new HazardItem(0.1, new Item.Properties()));

    public static final RegistryObject<Item> ALUMINUM_BLADE = ITEMS.register("aluminum_blade",
            () -> new BladeItem(new Item.Properties().durability(24), 1));

    public static final RegistryObject<Item> GOLD_BLADE = ITEMS.register("gold_blade",
            () -> new BladeItem(new Item.Properties().durability(32), 2));

    public static final RegistryObject<Item> IRON_BLADE = ITEMS.register("iron_blade",
            () -> new BladeItem(new Item.Properties().durability(64), 3));

    public static final RegistryObject<Item> STEEL_BLADE = ITEMS.register("steel_blade",
            () -> new BladeItem(new Item.Properties().durability(128), 4));

    public static final RegistryObject<Item> TITANIUM_BLADE = ITEMS.register("titanium_blade",
            () -> new BladeItem(new Item.Properties().durability(96), 5));

    public static final RegistryObject<Item> ADVANCED_BLADE = ITEMS.register("advanced_blade",
            () -> new BladeItem(new Item.Properties().durability(256), 6));

    public static final RegistryObject<Item> CMB_BLADE = ITEMS.register("cmb_blade",
            () -> new BladeItem(new Item.Properties().durability(1024), 7));

    public static final RegistryObject<Item> SCHRABIDIUM_BLADE = ITEMS.register("schrabidium_blade",
            () -> new BladeItem(new Item.Properties().durability(4096), 8));

    public static final RegistryObject<Item> DESH_BLADE = ITEMS.register("desh_blade",
            () -> new BladeItem(new Item.Properties().stacksTo(1), 9));

//    ====================================== CELLS ======================================

    public static final RegistryObject<Item> SCHRABIDIUM_4_CELL_EMPTY = ITEMS.register("schrabidium_4_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.2E5), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SCHRABIDIUM_2_CELL_EMPTY = ITEMS.register("schrabidium_2_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SCHRABIDIUM_CELL_EMPTY = ITEMS.register("schrabidium_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(3.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SCHRABIDIUM_4_CELL = ITEMS.register("schrabidium_4_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.2E5), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.2E5)));
    public static final RegistryObject<Item> SCHRABIDIUM_2_CELL = ITEMS.register("schrabidium_2_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(6.0E4)));
    public static final RegistryObject<Item> SCHRABIDIUM_CELL = ITEMS.register("schrabidium_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(3.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(3.0E4)));

    public static final RegistryObject<Item> ADVANCED_12_CELL_EMPTY = ITEMS.register("advanced_12_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(7.2E3), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> ADVANCED_4_CELL_EMPTY = ITEMS.register("advanced_4_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.4E3), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> ADVANCED_CELL_EMPTY = ITEMS.register("advanced_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E2), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> ADVANCED_12_CELL = ITEMS.register("advanced_12_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(7.2E3), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(7.2E3)));
    public static final RegistryObject<Item> ADVANCED_4_CELL = ITEMS.register("advanced_4_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.4E3), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.4E3)));
    public static final RegistryObject<Item> ADVANCED_CELL = ITEMS.register("advanced_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E2), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(6.0E2)));

    public static final RegistryObject<Item> LITHIUM_6_CELL_EMPTY = ITEMS.register("lithium_6_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(4.5E4), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> LITHIUM_3_CELL_EMPTY = ITEMS.register("lithium_3_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.25E4), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> LITHIUM_CELL_EMPTY = ITEMS.register("lithium_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(7.5E3), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> LITHIUM_6_CELL = ITEMS.register("lithium_6_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(4.5E4), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(4.5E4)));
    public static final RegistryObject<Item> LITHIUM_3_CELL = ITEMS.register("lithium_3_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.25E4), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.25E4)));
    public static final RegistryObject<Item> LITHIUM_CELL = ITEMS.register("lithium_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(7.5E3), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(7.5E3)));

    public static final RegistryObject<Item> REDSTONE_24_CELL_EMPTY = ITEMS.register("redstone_24_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(3.6E3), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> REDSTONE_6_CELL_EMPTY = ITEMS.register("redstone_6_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(9.0E2), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> REDSTONE_CELL_EMPTY = ITEMS.register("redstone_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.5E2), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> REDSTONE_24_CELL = ITEMS.register("redstone_24_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(3.6E3), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(3.6E3)));
    public static final RegistryObject<Item> REDSTONE_6_CELL = ITEMS.register("redstone_6_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(9.0E2), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(9.0E2)));
    public static final RegistryObject<Item> REDSTONE_CELL = ITEMS.register("redstone_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.5E2), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.5E2)));

    public static final RegistryObject<Item> SPARK_6_CELL_EMPTY = ITEMS.register("spark_6_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E6), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_6_CELL = ITEMS.register("spark_6_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(6.0E6), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(6.0E6)));
    public static final RegistryObject<Item> SPARK_100_CELL_EMPTY = ITEMS.register("spark_100_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E8), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_100_CELL = ITEMS.register("spark_100_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E8), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E8)));
    public static final RegistryObject<Item> SPARK_1000_CELL_EMPTY = ITEMS.register("spark_1000_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E9), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_1000_CELL = ITEMS.register("spark_1000_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E9), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E9)));
    public static final RegistryObject<Item> SPARK_2500_CELL_EMPTY = ITEMS.register("spark_2500_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E9), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_2500_CELL = ITEMS.register("spark_2500_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E9), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E5).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.5E9)));
    public static final RegistryObject<Item> SPARK_10000_CELL_EMPTY = ITEMS.register("spark_10000_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E10), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_10000_CELL = ITEMS.register("spark_10000_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E10), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E10)));
    public static final RegistryObject<Item> SPARK_POWER_CELL_EMPTY = ITEMS.register("spark_power_cell_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E12), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));
    public static final RegistryObject<Item> SPARK_POWER_CELL = ITEMS.register("spark_power_cell",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E12), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E6).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E12)));

//    ====================================== BATTERIES ======================================

    public static final RegistryObject<Item> GENERIC_BATTERY_EMPTY = ITEMS.register("generic_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(5.0E1), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> ADVANCED_BATTERY_EMPTY = ITEMS.register("advanced_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.0E2), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> LITHIUM_BATTERY_EMPTY = ITEMS.register("lithium_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E3), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> TRIXITE_BATTERY_EMPTY = ITEMS.register("trixite_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(5.0E4), FloatingLong.create(4.0E2).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E3).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> SPARK_BATTERY_EMPTY = ITEMS.register("spark_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E6), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> SPARK_CAR_BATTERY_EMPTY = ITEMS.register("spark_car_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E7), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> SCHRABIDIUM_BATTERY_EMPTY = ITEMS.register("schrabidium_battery_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));

    public static final RegistryObject<Item> ELECTRONIUM_CUBE_EMPTY = ITEMS.register("electronium_cube_empty",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E16), FloatingLong.create(1.0E13).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E13).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(0)));


    public static final RegistryObject<Item> INFINITE_FUSION_CORE = ITEMS.register("infinite_fusion_core",
            () -> new SelfChargingBatteryItem(new Item.Properties(), Double.POSITIVE_INFINITY));

    public static final RegistryObject<Item> MAKESHIFT_ENERGY_CORE = ITEMS.register("makeshift_energy_core",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E5), FloatingLong.create(0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E5)));

    public static final RegistryObject<Item> POTATO_BATTERY = ITEMS.register("potato_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E1), FloatingLong.create(0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1)));

    public static final RegistryObject<Item> POTATOS_BATTERY = ITEMS.register("potatos_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(5.0E3), FloatingLong.create(0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E3)));

    public static final RegistryObject<Item> GENERIC_BATTERY = ITEMS.register("generic_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(5.0E1), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1)));

    public static final RegistryObject<Item> ADVANCED_BATTERY = ITEMS.register("advanced_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.0E2), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E2)));

    public static final RegistryObject<Item> LITHIUM_BATTERY = ITEMS.register("lithium_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E3), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.5E3)));

    public static final RegistryObject<Item> CREATIVE_BATTERY = ITEMS.register("creative_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), Double.POSITIVE_INFINITY));

    public static final RegistryObject<Item> TRIXITE_BATTERY = ITEMS.register("trixite_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(5.0E4), FloatingLong.create(4.0E2).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E3).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E4)));

    public static final RegistryObject<Item> SPARK_BATTERY = ITEMS.register("spark_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E6), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E6)));

    public static final RegistryObject<Item> SPARK_CAR_BATTERY = ITEMS.register("spark_car_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(2.5E7), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.0E4).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(2.5E7)));

    public static final RegistryObject<Item> SCHRABIDIUM_BATTERY = ITEMS.register("schrabidium_battery",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E4), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(5.0E1).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E4)));

    public static final RegistryObject<Item> ELECTRONIUM_CUBE = ITEMS.register("electronium_cube",
            () -> new BatteryItem(new Item.Properties(), FloatingLong.create(1.0E16), FloatingLong.create(1.0E13).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E13).multiply(WattHourStorage.MCSECOND_TO_TICK), FloatingLong.create(1.0E16)));

    //    ====================================== SC BATTERIES ======================================

    public static final RegistryObject<Item> AMERICIUM_SC_BATTERY = ITEMS.register("americium_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 1.0E2 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> GOLD_SC_BATTERY = ITEMS.register("gold_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 2.5E1 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> LEAD_SC_BATTERY = ITEMS.register("lead_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 5.0E1 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> PLUTONIUM_SC_BATTERY = ITEMS.register("plutonium_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 1.0 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> POLONIUM_SC_BATTERY = ITEMS.register("polonium_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 5.0 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> TECHNETIUM_SC_BATTERY = ITEMS.register("technetium_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 2.5E-1 * WattHourStorage.MCSECOND_TO_TICK));

    public static final RegistryObject<Item> URANIUM_SC_BATTERY = ITEMS.register("uranium_sc_battery",
            () -> new SelfChargingBatteryItem(new Item.Properties(), 5.0E-2 * WattHourStorage.MCSECOND_TO_TICK));


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

    public static final RegistryObject<Item> SHREDDER = ITEMS.register("shredder",
            () -> new ShredderItem(RegisterBlocks.SHREDDER.get(), new Item.Properties()));

    public static final RegistryObject<Item> IRON_PLATE_STAMP = ITEMS.register("iron_plate_stamp",
            () -> new StampItem(new Item.Properties().durability(64)));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
}
