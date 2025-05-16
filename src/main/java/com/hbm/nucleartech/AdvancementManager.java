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
        preload(server, "story/root");
        preload(server, "story/achradpoison");
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
