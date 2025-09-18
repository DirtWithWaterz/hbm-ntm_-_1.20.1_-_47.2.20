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
                        output.accept(RegisterItems.SULFUR_INGOT.get());
                        output.accept(RegisterItems.NITER_INGOT.get());
                        output.accept(RegisterItems.TUNGSTEN_INGOT.get());
                        output.accept(RegisterItems.ALUMINIUM_INGOT.get());
                        output.accept(RegisterItems.FLUORITE_INGOT.get());
                        output.accept(RegisterItems.LEAD_INGOT.get());
                        output.accept(RegisterItems.LIGNITE_INGOT.get());
                        output.accept(RegisterItems.ASBESTOS_INGOT.get());
                        output.accept(RegisterItems.SCHRABIDIUM_INGOT.get());
                        output.accept(RegisterItems.AUSTRALIUM_INGOT.get());
                        output.accept(RegisterItems.RARE_EARTH_INGOT.get());
                        output.accept(RegisterItems.COBALT_INGOT.get());
                        output.accept(RegisterItems.CINNABAR_INGOT.get());
                        output.accept(RegisterItems.COLTAN_INGOT.get());

                        output.accept(RegisterItems.URANIUM_POWDER.get());
                        output.accept(RegisterItems.URANIUM_NUGGET.get());
                        output.accept(RegisterItems.URANIUM_CRYSTAL.get());
                        output.accept(RegisterItems.URANIUM_BILLET.get());
                        output.accept(RegisterItems.URANIUM_PILE_ROD.get());

                        output.accept(RegisterItems.RAW_TITANIUM.get());
                        output.accept(RegisterItems.RAW_URANIUM.get());
                        output.accept(RegisterItems.RAW_THORIUM.get());
                        output.accept(RegisterItems.RAW_SULFUR.get());
                        output.accept(RegisterItems.RAW_NITER.get());
                        output.accept(RegisterItems.RAW_TUNGSTEN.get());
                        output.accept(RegisterItems.RAW_ALUMINIUM.get());
                        output.accept(RegisterItems.RAW_FLUORITE.get());
                        output.accept(RegisterItems.RAW_BERYLLIUM.get());
                        output.accept(RegisterItems.RAW_LEAD.get());
                        output.accept(RegisterItems.RAW_LIGNITE.get());
                        output.accept(RegisterItems.RAW_ASBESTOS.get());
                        output.accept(RegisterItems.RAW_SCHRABIDIUM.get());
                        output.accept(RegisterItems.RAW_AUSTRALIUM.get());
                        output.accept(RegisterItems.RAW_RARE_EARTH.get());
                        output.accept(RegisterItems.RAW_COBALT.get());
                        output.accept(RegisterItems.RAW_CINNABAR.get());
                        output.accept(RegisterItems.RAW_COLTAN.get());

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

                        output.accept(RegisterBlocks.SULFUR_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_SULFUR_ORE.get());
                        output.accept(RegisterBlocks.NITER_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_NITER_ORE.get());
                        output.accept(RegisterBlocks.TUNGSTEN_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get());
                        output.accept(RegisterBlocks.ALUMINIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get());
                        output.accept(RegisterBlocks.FLUORITE_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get());
                        output.accept(RegisterBlocks.BERYLLIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get());
                        output.accept(RegisterBlocks.LEAD_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_LEAD_ORE.get());
                        output.accept(RegisterBlocks.LIGNITE_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get());
                        output.accept(RegisterBlocks.ASBESTOS_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get());
                        output.accept(RegisterBlocks.SCHRABIDIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get());
                        output.accept(RegisterBlocks.AUSTRALIUM_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get());
                        output.accept(RegisterBlocks.RARE_EARTH_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get());
                        output.accept(RegisterBlocks.COBALT_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_COBALT_ORE.get());
                        output.accept(RegisterBlocks.CINNABAR_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get());
                        output.accept(RegisterBlocks.COLTAN_ORE.get());
                        output.accept(RegisterBlocks.DEEPSLATE_COLTAN_ORE.get());

                        output.accept(RegisterBlocks.SULFUR_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_SULFUR_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.NITER_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_NITER_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.TUNGSTEN_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_TUNGSTEN_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.ALUMINIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_ALUMINIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.FLUORITE_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_FLUORITE_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.BERYLLIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_BERYLLIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.LEAD_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_LEAD_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.LIGNITE_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_LIGNITE_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.ASBESTOS_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_ASBESTOS_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.SCHRABIDIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_SCHRABIDIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.AUSTRALIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_AUSTRALIUM_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_RARE_EARTH_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.COBALT_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_COBALT_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.CINNABAR_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_CINNABAR_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.COLTAN_STORAGE_BLOCK.get());
                        output.accept(RegisterBlocks.RAW_COLTAN_STORAGE_BLOCK.get());

                        output.accept(RegisterBlocks.BLACK_CONCRETE.get());
                        output.accept(RegisterBlocks.BLUE_CONCRETE.get());
                        output.accept(RegisterBlocks.BROWN_CONCRETE.get());
                        output.accept(RegisterBlocks.CYAN_CONCRETE.get());
                        output.accept(RegisterBlocks.GRAY_CONCRETE.get());
                        output.accept(RegisterBlocks.GREEN_CONCRETE.get());
                        output.accept(RegisterBlocks.LIGHT_BLUE_CONCRETE.get());
                        output.accept(RegisterBlocks.LIGHT_GRAY_CONCRETE.get());
                        output.accept(RegisterBlocks.LIME_CONCRETE.get());
                        output.accept(RegisterBlocks.MAGENTA_CONCRETE.get());
                        output.accept(RegisterBlocks.ORANGE_CONCRETE.get());
                        output.accept(RegisterBlocks.PINK_CONCRETE.get());
                        output.accept(RegisterBlocks.PURPLE_CONCRETE.get());
                        output.accept(RegisterBlocks.RED_CONCRETE.get());
                        output.accept(RegisterBlocks.WHITE_CONCRETE.get());
                        output.accept(RegisterBlocks.YELLOW_CONCRETE.get());

                        output.accept(RegisterBlocks.JUNGLE_BRICK.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_CIRCLE.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_CRACKED.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_FRAGILE.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_0.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_1.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_2.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_3.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_4.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_5.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_6.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_7.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_8.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_9.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_10.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_11.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_12.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_13.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_14.get());
                        output.accept(RegisterBlocks.JUNGLE_BRICK_GLYPH_15.get());
                        output.accept(RegisterBlocks.BRICK_FIRE.get());
                        output.accept(RegisterBlocks.BRICK_FORGOTTEN.get());
                        output.accept(RegisterBlocks.BRICK_JUNGLE_LAVA.get());
                        output.accept(RegisterBlocks.BRICK_JUNGLE_MYSTIC.get());
                        output.accept(RegisterBlocks.BRICK_JUNGLE_OOZE.get());
                        output.accept(RegisterBlocks.BRICK_JUNGLE_TRAP.get());
                        output.accept(RegisterBlocks.BRICK_LIGHT.get());
                        output.accept(RegisterBlocks.BRICK_LIGHT_ALT.get());
                        output.accept(RegisterBlocks.BRICK_OBSIDIAN.get());
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
