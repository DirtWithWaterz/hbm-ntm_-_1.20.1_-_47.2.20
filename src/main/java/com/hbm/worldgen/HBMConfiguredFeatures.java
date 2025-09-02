package com.hbm.worldgen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class HBMConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_TITANIUM_KEY = registerKey("ore_titanium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_URANIUM_KEY = registerKey("ore_uranium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_THORIUM_KEY = registerKey("ore_thorium");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {

        RuleTest redTerracottaReplaceables = new BlockMatchTest(Blocks.RED_TERRACOTTA);
        RuleTest orangeTerracottaReplaceables = new BlockMatchTest(Blocks.ORANGE_TERRACOTTA);
        RuleTest yellowTerracottaReplaceables = new BlockMatchTest(Blocks.YELLOW_TERRACOTTA);
        RuleTest whiteTerracottaReplaceables = new BlockMatchTest(Blocks.WHITE_TERRACOTTA);
        RuleTest lightGrayTerracottaReplaceables = new BlockMatchTest(Blocks.LIGHT_GRAY_TERRACOTTA);
        RuleTest brownTerracottaReplaceables = new BlockMatchTest(Blocks.BROWN_TERRACOTTA);

        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackReplaceables = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest endstoneReplaceables = new BlockMatchTest(Blocks.END_STONE);

        List<OreConfiguration.TargetBlockState> overworldTitaniumOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.TITANIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldUraniumOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.URANIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_URANIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldThoriumOres = List.of(
                OreConfiguration.target(redTerracottaReplaceables, RegisterBlocks.RED_THORIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(orangeTerracottaReplaceables, RegisterBlocks.ORANGE_THORIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(yellowTerracottaReplaceables, RegisterBlocks.YELLOW_THORIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(whiteTerracottaReplaceables, RegisterBlocks.WHITE_THORIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(lightGrayTerracottaReplaceables, RegisterBlocks.LIGHT_GRAY_THORIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(brownTerracottaReplaceables, RegisterBlocks.BROWN_THORIUM_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_ORE_TITANIUM_KEY, Feature.ORE, new OreConfiguration(overworldTitaniumOres, 8));
        register(context, OVERWORLD_ORE_URANIUM_KEY, Feature.ORE, new OreConfiguration(overworldUraniumOres, 12));

        register(context, OVERWORLD_ORE_THORIUM_KEY, Feature.ORE, new OreConfiguration(overworldThoriumOres, 20));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {

        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(HBM.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
