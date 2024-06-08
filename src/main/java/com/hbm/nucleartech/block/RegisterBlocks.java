package com.hbm.nucleartech.block;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.hazard.HazardBlockItem;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class RegisterBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, HBM.MOD_ID);

    public static final RegistryObject<Block> BLOCK_TITANIUM = registerBlock("block_titanium",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0f, 10.0f)
            ));

    public static final RegistryObject<Block> BLOCK_URANIUM = registerHazardBlock(3.5, "block_uranium",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)
                    .strength(5.0f, 10.0f)
            ));

    public static final RegistryObject<Block> ORE_TITANIUM = registerBlock("ore_titanium",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_ORE)
                    .strength(5.0f, 10.0f),
                    UniformInt.of(3, 6)
            ));

    public static final RegistryObject<Block> DEEPSLATE_ORE_TITANIUM = registerBlock("deepslate_ore_titanium",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(3, 7)
            ));

    public static final RegistryObject<Block> ORE_URANIUM = registerHazardBlock(0.003, "ore_uranium",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DIAMOND_ORE)
                    .strength(5.0f, 10.0f),
                    UniformInt.of(2, 4)
            ));

    public static final RegistryObject<Block> DEEPSLATE_ORE_URANIUM = registerHazardBlock(0.0035, "deepslate_ore_uranium",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_DIAMOND_ORE)
                    .strength(8.0f, 15.0f),
                    UniformInt.of(2, 5)
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
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerHazardBlockItem(double radiation, String name, RegistryObject<T> block) {

        return RegisterItems.ITEMS.register(name, () -> new HazardBlockItem(radiation, block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {

        BLOCKS.register(eventBus);
    }
}
