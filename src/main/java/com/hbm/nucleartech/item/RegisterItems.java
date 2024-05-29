package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HBM.MOD_ID);

    public static final RegistryObject<Item> INGOT_BERYLLIUM = ITEMS.register("ingot_beryllium",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> INGOT_TITANIUM = ITEMS.register("ingot_titanium",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
}
