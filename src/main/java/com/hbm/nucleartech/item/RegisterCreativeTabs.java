package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.util.RegisterTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HBM.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NTM_RESOURCES_AND_PARTS = CREATIVE_TABS.register("resources_and_parts",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterItems.URANIUM_INGOT.get()))
                    .title(Component.translatable("creativetab.resources_and_parts"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(RegisterItems.TITANIUM_INGOT.get());
                        output.accept(RegisterItems.BERYLLIUM_INGOT.get());
                        output.accept(RegisterItems.URANIUM_INGOT.get());

                        output.accept(RegisterItems.URANIUM_POWDER.get());
                        output.accept(RegisterItems.URANIUM_NUGGET.get());
                        output.accept(RegisterItems.URANIUM_CRYSTAL.get());
                        output.accept(RegisterItems.URANIUM_BILLET.get());
                        output.accept(RegisterItems.URANIUM_PILE_ROD.get());

                        output.accept(RegisterItems.RAW_TITANIUM.get());
                        output.accept(RegisterItems.RAW_URANIUM.get());
                        output.accept(RegisterItems.RAW_THORIUM.get());

                        output.accept(RegisterItems.THORIUM_SHALE.get());
                        output.accept(RegisterItems.THORIUM_INGOT.get());
                        output.accept(RegisterItems.THORIUM_POWDER.get());

                        output.acceptAll(getItemsFromTag(RegisterTags.Items.SHREDDER_BLADES));

                        output.accept(RegisterItems.GEIGER_COUNTER.get());

                        output.accept(RegisterItems.M65_MASK.get());

                        output.accept(RegisterItems.GAS_MASK_FILTER_MONO.get());
                        output.accept(RegisterItems.GAS_MASK_FILTER.get());
                        output.accept(RegisterItems.GAS_MASK_FILTER_COMBO.get());
                        output.accept(RegisterItems.GAS_MASK_FILTER_RADON.get());

                        output.accept(RegisterItems.HAZMAT_HELMET.get());
                        output.accept(RegisterItems.HAZMAT_CHESTPLATE.get());
                        output.accept(RegisterItems.HAZMAT_LEGGINGS.get());
                        output.accept(RegisterItems.HAZMAT_BOOTS.get());

                        output.accept(RegisterItems.HAZMAT_HELMET_RED.get());
                        output.accept(RegisterItems.HAZMAT_CHESTPLATE_RED.get());
                        output.accept(RegisterItems.HAZMAT_LEGGINGS_RED.get());
                        output.accept(RegisterItems.HAZMAT_BOOTS_RED.get());

                        output.accept(RegisterItems.HAZMAT_HELMET_GREY.get());
                        output.accept(RegisterItems.HAZMAT_CHESTPLATE_GREY.get());
                        output.accept(RegisterItems.HAZMAT_LEGGINGS_GREY.get());
                        output.accept(RegisterItems.HAZMAT_BOOTS_GREY.get());

                        output.accept(RegisterItems.IRON_PLATE_STAMP.get());
                        output.accept(RegisterItems.IRON_PLATE.get());

                        output.accept(RegisterItems.GENERIC_BATTERY.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NTM_MACHINE_ITEMS_AND_FUEL = CREATIVE_TABS.register("machine_items_and_fuel",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterItems.PLACEHOLDER.get()))
                    .title(Component.translatable("creativetab.machine_items_and_fuel"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(RegisterItems.GENERIC_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.GENERIC_BATTERY.get());
                        output.accept(RegisterItems.REDSTONE_CELL_EMPTY.get());
                        output.accept(RegisterItems.REDSTONE_CELL.get());
                        output.accept(RegisterItems.REDSTONE_6_CELL_EMPTY.get());
                        output.accept(RegisterItems.REDSTONE_6_CELL.get());
                        output.accept(RegisterItems.REDSTONE_24_CELL_EMPTY.get());
                        output.accept(RegisterItems.REDSTONE_24_CELL.get());
                        output.accept(RegisterItems.ADVANCED_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.ADVANCED_BATTERY.get());
                        output.accept(RegisterItems.ADVANCED_CELL_EMPTY.get());
                        output.accept(RegisterItems.ADVANCED_CELL.get());
                        output.accept(RegisterItems.ADVANCED_4_CELL_EMPTY.get());
                        output.accept(RegisterItems.ADVANCED_4_CELL.get());
                        output.accept(RegisterItems.ADVANCED_12_CELL_EMPTY.get());
                        output.accept(RegisterItems.ADVANCED_12_CELL.get());
                        output.accept(RegisterItems.LITHIUM_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.LITHIUM_BATTERY.get());
                        output.accept(RegisterItems.LITHIUM_CELL_EMPTY.get());
                        output.accept(RegisterItems.LITHIUM_CELL.get());
                        output.accept(RegisterItems.LITHIUM_3_CELL_EMPTY.get());
                        output.accept(RegisterItems.LITHIUM_3_CELL.get());
                        output.accept(RegisterItems.LITHIUM_6_CELL_EMPTY.get());
                        output.accept(RegisterItems.LITHIUM_6_CELL.get());
                        output.accept(RegisterItems.SCHRABIDIUM_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.SCHRABIDIUM_BATTERY.get());
                        output.accept(RegisterItems.SCHRABIDIUM_CELL_EMPTY.get());
                        output.accept(RegisterItems.SCHRABIDIUM_CELL.get());
                        output.accept(RegisterItems.SCHRABIDIUM_2_CELL_EMPTY.get());
                        output.accept(RegisterItems.SCHRABIDIUM_2_CELL.get());
                        output.accept(RegisterItems.SCHRABIDIUM_4_CELL_EMPTY.get());
                        output.accept(RegisterItems.SCHRABIDIUM_4_CELL.get());
                        output.accept(RegisterItems.SPARK_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.SPARK_BATTERY.get());
                        output.accept(RegisterItems.TRIXITE_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.TRIXITE_BATTERY.get());
                        output.accept(RegisterItems.SPARK_6_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_6_CELL.get());
                        output.accept(RegisterItems.SPARK_CAR_BATTERY_EMPTY.get());
                        output.accept(RegisterItems.SPARK_CAR_BATTERY.get());
                        output.accept(RegisterItems.SPARK_100_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_100_CELL.get());
                        output.accept(RegisterItems.SPARK_1000_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_1000_CELL.get());
                        output.accept(RegisterItems.SPARK_2500_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_2500_CELL.get());
                        output.accept(RegisterItems.SPARK_10000_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_10000_CELL.get());
                        output.accept(RegisterItems.SPARK_POWER_CELL_EMPTY.get());
                        output.accept(RegisterItems.SPARK_POWER_CELL.get());
                        output.accept(RegisterItems.ELECTRONIUM_CUBE_EMPTY.get());
                        output.accept(RegisterItems.ELECTRONIUM_CUBE.get());
                        output.accept(RegisterItems.CREATIVE_BATTERY.get());
                        output.accept(RegisterItems.POTATO_BATTERY.get());
                        output.accept(RegisterItems.POTATOS_BATTERY.get());
                        output.accept(RegisterItems.URANIUM_SC_BATTERY.get());
                        output.accept(RegisterItems.TECHNETIUM_SC_BATTERY.get());
                        output.accept(RegisterItems.PLUTONIUM_SC_BATTERY.get());
                        output.accept(RegisterItems.POLONIUM_SC_BATTERY.get());
                        output.accept(RegisterItems.GOLD_SC_BATTERY.get());
                        output.accept(RegisterItems.LEAD_SC_BATTERY.get());
                        output.accept(RegisterItems.AMERICIUM_SC_BATTERY.get());
                        output.accept(RegisterItems.MAKESHIFT_ENERGY_CORE.get());
                        output.accept(RegisterItems.INFINITE_FUSION_CORE.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NTM_BLOCKS = CREATIVE_TABS.register("ntm_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterBlocks.TITANIUM_BLOCK.get()))
                    .title(Component.translatable("creativetab.ntm_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(RegisterBlocks.WASTE_BLOCK.get());
                        output.accept(RegisterBlocks.URANIUM_BLOCK.get());
                        output.accept(RegisterBlocks.TITANIUM_BLOCK.get());

                        output.accept(RegisterBlocks.URANIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_URANIUM_ORE.get());

                        output.accept(RegisterBlocks.TITANIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get());
                        output.accept(RegisterBlocks.RAD_RESISTANT_BLOCK.get());

                        output.accept(RegisterBlocks.RED_THORIUM_ORE.get());
                        output.accept(RegisterBlocks.ORANGE_THORIUM_ORE.get());
                        output.accept(RegisterBlocks.YELLOW_THORIUM_ORE.get());
                        output.accept(RegisterBlocks.WHITE_THORIUM_ORE.get());
                        output.accept(RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get());
                        output.accept(RegisterBlocks.BROWN_THORIUM_ORE.get());

                        output.accept(RegisterBlocks.DEAD_GRASS.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NTM_MACHINES = CREATIVE_TABS.register("ntm_machines",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterBlocks.RADIATION_DECONTAMINATOR.get()))
                    .title(Component.translatable("creativetab.ntm_machines"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(RegisterBlocks.RADIATION_DECONTAMINATOR.get());
                        output.accept(RegisterItems.BURNER_PRESS.get());
                        output.accept(RegisterItems.SHREDDER.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {

        CREATIVE_TABS.register(eventBus);
    }

    public static List<ItemStack> getItemsFromTag(TagKey<Item> tagKey) {
        List<ItemStack> items = new ArrayList<>();

        // ForgeRegistries.ITEMS is wrapped with holders
        for (Item holder : ForgeRegistries.ITEMS.tags().getTag(tagKey)) {
            items.add(holder.getDefaultInstance());
        }

        return items;
    }
}
