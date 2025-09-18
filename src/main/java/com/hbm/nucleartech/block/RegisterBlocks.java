package com.hbm.nucleartech.block;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.custom.*;
import com.hbm.nucleartech.hazard.HazardBlock;
import com.hbm.nucleartech.hazard.HazardBlockItem;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RegisterBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HBM.MOD_ID);

    public static final List<RegistryObject<? extends Block>> HAZARD_BLOCKS = new ArrayList<>();

    public static final RegistryObject<Block> WASTE_BLOCK = registerHazardBlock(4500, "waste_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    4500
            ));

    public static final RegistryObject<Block> TITANIUM_BLOCK = registerBlock("titanium_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f)
            ));

    public static final RegistryObject<Block> URANIUM_BLOCK = registerHazardBlock(3.5, "uranium_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)
                    .strength(5.0f, 4.0f),
                    3.5
            ));

    public static final RegistryObject<Block> TITANIUM_ORE = registerBlock("titanium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(3, 6)
            ));

    public static final RegistryObject<Block> DEEPSLATE_TITANIUM_ORE = registerBlock("deepslate_titanium_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(3, 7)
            ));

    public static final RegistryObject<Block> URANIUM_ORE = registerHazardBlock(0.003, "uranium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_URANIUM_ORE = registerHazardBlock(0.0035, "deepslate_uranium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> RED_THORIUM_ORE = registerHazardBlock(0.1, "red_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> ORANGE_THORIUM_ORE = registerHazardBlock(0.1, "orange_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> YELLOW_THORIUM_ORE = registerHazardBlock(0.1, "yellow_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> WHITE_THORIUM_ORE = registerHazardBlock(0.1, "white_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> LIGHT_GRAY_THORIUM_ORE = registerHazardBlock(0.1, "light_gray_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> BROWN_THORIUM_ORE = registerHazardBlock(0.1, "brown_thorium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(3.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.001
            ));

    public static final RegistryObject<Block> RADIATION_DECONTAMINATOR = registerBlock("radiation_decontaminator",
            () -> new DeconRadBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f)
            ));

    public static final RegistryObject<Block> SHREDDER = BLOCKS.register("shredder",
            () -> new ShredderBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> DEAD_GRASS = registerBlock("dead_grass",
            () -> new GrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));

    public static final RegistryObject<Block> BURNER_PRESS = BLOCKS.register("burner_press",
            () -> new BurnerPressBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .noOcclusion()
                    .strength(5.0f, 4.0f)));

    public static final RegistryObject<Block> BURNER_PRESS_PART = registerBlock("burner_press_part",
            () -> new BurnerPressPartBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .mapColor(MapColor.NONE)
                    .noOcclusion()
                    .strength(5.0f, 4.0f)
                    .noLootTable()
                    .pushReaction(PushReaction.BLOCK)
            ));

    public static final RegistryObject<Block> RAD_RESISTANT_BLOCK = registerBlock("rad_resistant_block",
            () -> new RadResistantBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f)
                    , 1f, 0.085f, 1f
            )); // has values of lead ^^ ~100% resistance (water is 1f, 0.085f. Lead is 11.34f, 0.07f)

    /*
    Needs values done.
     */

    public static final RegistryObject<Block> SULFUR_ORE = registerHazardBlock(0.003, "sulfur_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_SULFUR_ORE = registerHazardBlock(0.0035, "deepslate_sulfur_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> NITER_ORE = registerHazardBlock(0.003, "niter_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_NITER_ORE = registerHazardBlock(0.0035, "deepslate_niter_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> TUNGSTEN_ORE = registerHazardBlock(0.003, "tungsten_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_TUNGSTEN_ORE = registerHazardBlock(0.0035, "deepslate_tungsten_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> ALUMINIUM_ORE = registerHazardBlock(0.003, "aluminum_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_ALUMINIUM_ORE = registerHazardBlock(0.0035, "deepslate_aluminum_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> FLUORITE_ORE = registerHazardBlock(0.003, "fluorite_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_FLUORITE_ORE = registerHazardBlock(0.0035, "deepslate_fluorite_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> BERYLLIUM_ORE = registerHazardBlock(0.003, "beryllium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_BERYLLIUM_ORE = registerHazardBlock(0.0035, "deepslate_beryllium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> LEAD_ORE = registerHazardBlock(0.003, "lead_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_LEAD_ORE = registerHazardBlock(0.0035, "deepslate_lead_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> LIGNITE_ORE = registerHazardBlock(0.003, "lignite_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_LIGNITE_ORE = registerHazardBlock(0.0035, "deepslate_lignite_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> ASBESTOS_ORE = registerHazardBlock(0.003, "asbestos_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_ASBESTOS_ORE = registerHazardBlock(0.0035, "deepslate_asbestos_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> SCHRABIDIUM_ORE = registerHazardBlock(0.003, "schrabidium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_SCHRABIDIUM_ORE = registerHazardBlock(0.0035, "deepslate_schrabidium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> AUSTRALIUM_ORE = registerHazardBlock(0.003, "australium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_AUSTRALIUM_ORE = registerHazardBlock(0.0035, "deepslate_australium_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> RARE_EARTH_ORE = registerHazardBlock(0.003, "rare_earth_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_RARE_EARTH_ORE = registerHazardBlock(0.0035, "deepslate_rare_earth_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> COBALT_ORE = registerHazardBlock(0.003, "cobalt_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_COBALT_ORE = registerHazardBlock(0.0035, "deepslate_cobalt_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> CINNABAR_ORE = registerHazardBlock(0.003, "cinnabar_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_CINNABAR_ORE = registerHazardBlock(0.0035, "deepslate_cinnabar_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> COLTAN_ORE = registerHazardBlock(0.003, "coltan_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> DEEPSLATE_COLTAN_ORE = registerHazardBlock(0.0035, "deepslate_coltan_ore",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));


    public static final RegistryObject<Block> SULFUR_STORAGE_BLOCK = registerHazardBlock(0.003, "sulfur_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_SULFUR_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_sulfur_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> NITER_STORAGE_BLOCK = registerHazardBlock(0.003, "niter_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_NITER_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_niter_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> TUNGSTEN_STORAGE_BLOCK = registerHazardBlock(0.003, "tungsten_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_TUNGSTEN_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_tungsten_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> ALUMINIUM_STORAGE_BLOCK = registerHazardBlock(0.003, "aluminum_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_ALUMINIUM_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_aluminum_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> FLUORITE_STORAGE_BLOCK = registerHazardBlock(0.003, "fluorite_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_FLUORITE_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_fluorite_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> BERYLLIUM_STORAGE_BLOCK = registerHazardBlock(0.003, "beryllium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_BERYLLIUM_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_beryllium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> LEAD_STORAGE_BLOCK = registerHazardBlock(0.003, "lead_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_LEAD_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_lead_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> LIGNITE_STORAGE_BLOCK = registerHazardBlock(0.003, "lignite_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_LIGNITE_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_lignite_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> ASBESTOS_STORAGE_BLOCK = registerHazardBlock(0.003, "asbestos_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_ASBESTOS_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_asbestos_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> SCHRABIDIUM_STORAGE_BLOCK = registerHazardBlock(0.003, "schrabidium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_SCHRABIDIUM_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_schrabidium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> AUSTRALIUM_STORAGE_BLOCK = registerHazardBlock(0.003, "australium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_AUSTRALIUM_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_australium_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_RARE_EARTH_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_rare_earth_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> COBALT_STORAGE_BLOCK = registerHazardBlock(0.003, "cobalt_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_COBALT_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_cobalt_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> CINNABAR_STORAGE_BLOCK = registerHazardBlock(0.003, "cinnabar_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_CINNABAR_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_cinnabar_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));

    public static final RegistryObject<Block> COLTAN_STORAGE_BLOCK = registerHazardBlock(0.003, "coltan_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 4.0f),
                    UniformInt.of(2, 4),
                    0.0035
            ));

    public static final RegistryObject<Block> RAW_COLTAN_STORAGE_BLOCK = registerHazardBlock(0.0035, "raw_coltan_storage_block",
            () -> new HazardBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5),
                    0.0035
            ));
    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {

        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {

        return RegisterItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static <T extends Block>RegistryObject<T> registerHazardBlock(double radiation, String name, Supplier<T> block) {

        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerHazardBlockItem(radiation, name, toReturn);

        HAZARD_BLOCKS.add(toReturn);

        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerHazardBlockItem(double radiation, String name, RegistryObject<T> block) {

        return RegisterItems.ITEMS.register(name, () -> new HazardBlockItem(radiation, block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {

        BLOCKS.register(eventBus);
    }
}
