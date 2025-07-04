package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.hazard.HazardItem;
import com.hbm.nucleartech.item.special.CustomLoreItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HBM.MOD_ID);

    public static final RegistryObject<Item> PLATE_IRON = ITEMS.register("plate_iron",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_BERYLLIUM = ITEMS.register("ingot_beryllium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_TITANIUM = ITEMS.register("ingot_titanium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_TITANIUM = ITEMS.register("raw_titanium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_URANIUM = ITEMS.register("ingot_uranium",
            () -> new HazardItem(0.35, new Item.Properties()));

    public static final RegistryObject<Item> RAW_URANIUM = ITEMS.register("raw_uranium",
            () -> new HazardItem(0.035, new Item.Properties()));

    public static final RegistryObject<Item> POWDER_URANIUM = ITEMS.register("powder_uranium",
            () -> new HazardItem(1.05, new Item.Properties()));

    public static final RegistryObject<Item> NUGGET_URANIUM = ITEMS.register("nugget_uranium",
            () -> new HazardItem(0.035, new Item.Properties()));

    public static final RegistryObject<Item> CRYSTAL_URANIUM = ITEMS.register("crystal_uranium",
            () -> new HazardItem(1.75, new Item.Properties()));

    public static final RegistryObject<Item> BILLET_URANIUM = ITEMS.register("billet_uranium",
            () -> new HazardItem(0.175, new Item.Properties()));

    public static final RegistryObject<Item> PILE_ROD_URANIUM = ITEMS.register("pile_rod_uranium",
            () -> new HazardItem(0.525, new Item.Properties()));


    public static final RegistryObject<Item> REACHER = ITEMS.register("reacher",
            () -> new CustomLoreItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
}
