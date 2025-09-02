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

import java.util.function.Supplier;

public class RegisterBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HBM.MOD_ID);

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
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerHazardBlockItem(double radiation, String name, RegistryObject<T> block) {

        return RegisterItems.ITEMS.register(name, () -> new HazardBlockItem(radiation, block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {

        BLOCKS.register(eventBus);
    }
}
