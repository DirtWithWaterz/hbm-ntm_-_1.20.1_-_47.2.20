package com.hbm.worldgen;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class HBMPlacedFeatures {

    public static final ResourceKey<PlacedFeature> ORE_TITANIUM_PLACED_KEY = registerKey("ore_titanium_placed");
    public static final ResourceKey<PlacedFeature> ORE_URANIUM_PLACED_KEY = registerKey("ore_uranium_placed");
    public static final ResourceKey<PlacedFeature> ORE_THORIUM_PLACED_KEY = registerKey("ore_thorium_placed");
    public static final ResourceKey<PlacedFeature> ORE_SULFUR_PLACED_KEY = registerKey("ore_sulfur_placed");
    public static final ResourceKey<PlacedFeature> ORE_NITER_PLACED_KEY = registerKey("ore_niter_placed");
    public static final ResourceKey<PlacedFeature> ORE_TUNGSTEN_PLACED_KEY = registerKey("ore_tungsten_placed");
    public static final ResourceKey<PlacedFeature> ORE_ALUMINIUM_PLACED_KEY = registerKey("ore_aluminium_placed");
    public static final ResourceKey<PlacedFeature> ORE_FLUORITE_PLACED_KEY = registerKey("ore_fluorite_placed");
    public static final ResourceKey<PlacedFeature> ORE_BERYLLIUM_PLACED_KEY = registerKey("ore_beryllium_placed");
    public static final ResourceKey<PlacedFeature> ORE_LEAD_PLACED_KEY = registerKey("ore_lead_placed");
    public static final ResourceKey<PlacedFeature> ORE_LIGNITE_PLACED_KEY = registerKey("ore_lignite_placed");
    public static final ResourceKey<PlacedFeature> ORE_ASBESTOS_PLACED_KEY = registerKey("ore_asbestos_placed");
    public static final ResourceKey<PlacedFeature> ORE_SCHRABIDIUM_PLACED_KEY = registerKey("ore_schrabidium_placed");
    public static final ResourceKey<PlacedFeature> ORE_AUSTRALIUM_PLACED_KEY = registerKey("ore_australium_placed");
    public static final ResourceKey<PlacedFeature> ORE_RARE_EARTH_PLACED_KEY = registerKey("ore_rare_earth_placed");
    public static final ResourceKey<PlacedFeature> ORE_COBALT_PLACED_KEY = registerKey("ore_cobalt_placed");
    public static final ResourceKey<PlacedFeature> ORE_CINNABAR_PLACED_KEY = registerKey("ore_cinnabar_placed");
    public static final ResourceKey<PlacedFeature> ORE_COLTAN_PLACED_KEY = registerKey("ore_coltan_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ORE_TITANIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_TITANIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(45))));

        register(context, ORE_SULFUR_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_SULFUR_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_NITER_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_NITER_KEY),
                HBMOrePlacement.commonOrePlacement(16,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(200))));

        register(context, ORE_TUNGSTEN_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_TUNGSTEN_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_ALUMINIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_ALUMINIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_FLUORITE_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_FLUORITE_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_BERYLLIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_BERYLLIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_LEAD_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_LEAD_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_LIGNITE_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_LIGNITE_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_ASBESTOS_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_ASBESTOS_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_SCHRABIDIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_SCHRABIDIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_AUSTRALIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_AUSTRALIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_RARE_EARTH_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_RARE_EARTH_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_COBALT_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_COBALT_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_CINNABAR_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_CINNABAR_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));

        register(context, ORE_COLTAN_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_COLTAN_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {

        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
