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
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_SULFUR_KEY = registerKey("ore_sulfur");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_NITER_KEY = registerKey("ore_niter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_TUNGSTEN_KEY = registerKey("ore_tungsten");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_ALUMINIUM_KEY = registerKey("ore_aluminium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_FLUORITE_KEY = registerKey("ore_fluorite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_BERYLLIUM_KEY = registerKey("ore_beryllium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_LEAD_KEY = registerKey("ore_lead");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_LIGNITE_KEY = registerKey("ore_lignite");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_ASBESTOS_KEY = registerKey("ore_asbestos");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_SCHRABIDIUM_KEY = registerKey("ore_schrabidium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_AUSTRALIUM_KEY = registerKey("ore_australium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_RARE_EARTH_KEY = registerKey("ore_rare_earth");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_COBALT_KEY = registerKey("ore_cobalt");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_CINNABAR_KEY = registerKey("ore_cinnabar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_ORE_COLTAN_KEY = registerKey("ore_coltan");

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

        List<OreConfiguration.TargetBlockState> overworldSulfurOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.SULFUR_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_SULFUR_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldNiterOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.NITER_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_NITER_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldTungstenOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.TUNGSTEN_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_TUNGSTEN_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldAluminiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.ALUMINIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_ALUMINIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldFluoriteOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.FLUORITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_FLUORITE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldBerylliumOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.BERYLLIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_BERYLLIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldLeadOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.LEAD_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldLigniteOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.LIGNITE_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_LIGNITE_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldAsbestosOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.ASBESTOS_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_ASBESTOS_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldSchrabidiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.SCHRABIDIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_SCHRABIDIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldAustralianOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.AUSTRALIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_AUSTRALIUM_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldRareEarthOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.RARE_EARTH_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_RARE_EARTH_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldCobaltOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.COBALT_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_COBALT_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldCinnabarOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.CINNABAR_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_CINNABAR_ORE.get().defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> overworldColtanOres = List.of(
                OreConfiguration.target(stoneReplaceables, RegisterBlocks.COLTAN_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceables, RegisterBlocks.DEEPSLATE_COLTAN_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_ORE_TITANIUM_KEY, Feature.ORE, new OreConfiguration(overworldTitaniumOres, 8));
        register(context, OVERWORLD_ORE_URANIUM_KEY, Feature.ORE, new OreConfiguration(overworldUraniumOres, 12));

        register(context, OVERWORLD_ORE_THORIUM_KEY, Feature.ORE, new OreConfiguration(overworldThoriumOres, 20));


        register(context, OVERWORLD_ORE_SULFUR_KEY, Feature.ORE, new OreConfiguration(overworldSulfurOres, 12));
        register(context, OVERWORLD_ORE_NITER_KEY, Feature.ORE, new OreConfiguration(overworldNiterOres, 12));
        register(context, OVERWORLD_ORE_TUNGSTEN_KEY, Feature.ORE, new OreConfiguration(overworldTungstenOres, 12));
        register(context, OVERWORLD_ORE_ALUMINIUM_KEY, Feature.ORE, new OreConfiguration(overworldAluminiumOres, 12));
        register(context, OVERWORLD_ORE_FLUORITE_KEY, Feature.ORE, new OreConfiguration(overworldFluoriteOres, 12));
        register(context, OVERWORLD_ORE_BERYLLIUM_KEY, Feature.ORE, new OreConfiguration(overworldBerylliumOres, 12));
        register(context, OVERWORLD_ORE_LEAD_KEY, Feature.ORE, new OreConfiguration(overworldLeadOres, 12));
        register(context, OVERWORLD_ORE_LIGNITE_KEY, Feature.ORE, new OreConfiguration(overworldLigniteOres, 12));
        register(context, OVERWORLD_ORE_ASBESTOS_KEY, Feature.ORE, new OreConfiguration(overworldAsbestosOres, 12));
        register(context, OVERWORLD_ORE_SCHRABIDIUM_KEY, Feature.ORE, new OreConfiguration(overworldSchrabidiumOres, 12));
        register(context, OVERWORLD_ORE_AUSTRALIUM_KEY, Feature.ORE, new OreConfiguration(overworldAustralianOres, 12));
        register(context, OVERWORLD_ORE_RARE_EARTH_KEY, Feature.ORE, new OreConfiguration(overworldRareEarthOres, 12));
        register(context, OVERWORLD_ORE_COBALT_KEY, Feature.ORE, new OreConfiguration(overworldCobaltOres, 12));
        register(context, OVERWORLD_ORE_CINNABAR_KEY, Feature.ORE, new OreConfiguration(overworldCinnabarOres, 12));
        register(context, OVERWORLD_ORE_COLTAN_KEY, Feature.ORE, new OreConfiguration(overworldColtanOres, 12));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {

        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
