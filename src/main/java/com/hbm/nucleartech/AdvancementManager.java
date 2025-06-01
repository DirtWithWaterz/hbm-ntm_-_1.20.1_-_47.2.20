package com.hbm.nucleartech;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

public class AdvancementManager {
    private static final Map<ResourceLocation, Advancement> CUSTOM_ADVANCEMENTS = new HashMap<>();

    /**
     * Called once on server start. Preloads advancement references from JSON.
     */
    public static void init(MinecraftServer server) {
        CUSTOM_ADVANCEMENTS.clear();

        // Preload all advancements you want to trigger by code
        preload(server, "root");

        preload(server, "sacrifice");
        preload(server, "impossible");
        preload(server, "taste_of_blood");
        preload(server, "go_fish");
        preload(server, "potato");
        preload(server, "c20_5");
        preload(server, "fiend");
        preload(server, "fiend2");
        preload(server, "stratum");
        preload(server, "omega12");

        preload(server, "no9");
        preload(server, "slimeball");
        preload(server, "sulfuric");
        preload(server, "inferno");
        preload(server, "red_room");

        preload(server, "hidden");

        preload(server, "horizons_start");
        preload(server, "horizons_end");
        preload(server, "horizons_bonus");

        preload(server, "boss_creeper");
        preload(server, "boss_meltdown");
        preload(server, "boss_mask_man");
        preload(server, "boss_worm");
        preload(server, "boss_ufo");

        preload(server, "rad_poison");
        preload(server, "rad_death");

        preload(server, "some_wounds");

        preload(server, "digamma_see");
        preload(server, "digamma_feel");
        preload(server, "digamma_know");
        preload(server, "digamma_kauai_moho");
        preload(server, "digamma_up_on_top");

        // progression achievements
        preload(server, "burner_press");
        preload(server, "blast_furnace");
        preload(server, "assembly");
        preload(server, "selenium");
        preload(server, "chem_plant");
        preload(server, "concrete");
        preload(server, "polymer");
        preload(server, "desh");
        preload(server, "tantalum");
        preload(server, "gas_cent");
        preload(server, "centrifuge");
        preload(server, "foeq");
        preload(server, "soyuz");
        preload(server, "space");
        preload(server, "schrab");
        preload(server, "acidizer");
        preload(server, "radium");
        preload(server, "technetium");
        preload(server, "zirnox_boom");
        preload(server, "chicago_pile");
        preload(server, "silex");
        preload(server, "watz");
        preload(server, "watz_boom");
        preload(server, "rbmk");
        preload(server, "rbmk_boom");
        preload(server, "bismuth");
        preload(server, "breeding");
        preload(server, "fusion");
        preload(server, "meltdown");
        preload(server, "red_balloons");
        preload(server, "manhattan");




    }

    private static void preload(MinecraftServer server, String path) {
        ResourceLocation id = new ResourceLocation("hbm", path);
        Advancement advancement = server.getAdvancements().getAdvancement(id);
        if (advancement != null) {
            CUSTOM_ADVANCEMENTS.put(id, advancement);
//            System.err.println("[AdvancementManager] Loaded advancement: " + id);
        } else {
            System.err.println("[AdvancementManager] Could not find advancement: " + id);
        }
    }

    /**
     * Triggers the advancement for the player by unlocking all remaining criteria.
     */
    public static void grant(ServerPlayer player, String path) {
        ResourceLocation id = new ResourceLocation("hbm", path);
        Advancement advancement = CUSTOM_ADVANCEMENTS.get(id);

        if (advancement == null) {
            System.err.println("[AdvancementManager] Advancement not loaded: " + id);
            return;
        }

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (!progress.isDone()) {
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }

    /**
     * Optionally expose this to check advancement state.
     */
    public static boolean hasAdvancement(ServerPlayer player, String path) {
        ResourceLocation id = new ResourceLocation("hbm", path);
        Advancement advancement = CUSTOM_ADVANCEMENTS.get(id);
        if (advancement == null) return false;

        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        return progress.isDone();
    }
}
