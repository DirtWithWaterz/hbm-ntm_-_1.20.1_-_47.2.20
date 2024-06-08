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

    public static void bootstrap(BootstapContext<PlacedFeature> context) {

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ORE_TITANIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_TITANIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(45))));

        register(context, ORE_URANIUM_PLACED_KEY, configuredFeatures.getOrThrow(HBMConfiguredFeatures.OVERWORLD_ORE_URANIUM_KEY),
                HBMOrePlacement.commonOrePlacement(9,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(34))));
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {

        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(HBM.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
