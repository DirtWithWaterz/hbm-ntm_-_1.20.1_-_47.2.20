package com.hbm.nucleartech.datagen;

import com.google.gson.*;
import com.hbm.nucleartech.HBM;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ModDamageTypeTagProvider extends DamageTypeTagsProvider {

    private final PackOutput output;

    public ModDamageTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, HBM.MOD_ID, existingFileHelper);
        this.output = pOutput;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        CompletableFuture<?> value = super.run(pOutput);
        try {

            System.out.println("Attempting to inject values into tag JSONs...");
            injectIntoGeneratedJsons();
        } catch (IOException e) {

            throw new RuntimeException("Failed to inject values into tag JSONs", e);
        }
        return value;
    }

    private void injectIntoGeneratedJsons() throws IOException {
        Path tagsFolder = this.output.getOutputFolder().resolve("data/minecraft/tags/damage_type");
        if (!Files.exists(tagsFolder)) return;

        // Map of specific tag file name -> list of resource locations to inject
        Map<String, List<String>> injectMap = new HashMap<>();

        // ========= Damage types list here by tag =========

        injectMap.put("bypasses_armor.json", List.of(
                "hbm:radiation",
                "hbm:mud_poisoning",
                "hbm:euthanized_self",
                "hbm:euthanized_self_2",
                "hbm:tau_blast",
                "hbm:digamma",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:spikes",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan",
                "hbm:tau",
                "hbm:cmb",
                "hbm:sub_atomic",
                "hbm:euthanized",
                "hbm:laser"
        ));

        injectMap.put("is_explosion.json", List.of(
                "hbm:nuclear_blast",
                "hbm:blast"
        ));

        injectMap.put("bypasses_effects.json", List.of(
                "hbm:digamma",
                "hbm:teleporter",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan"
        ));

        injectMap.put("bypasses_enchantments.json", List.of(
                "hbm:digamma",
                "hbm:teleporter",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan"
        ));

        injectMap.put("bypasses_invulnerability.json", List.of(
                "hbm:digamma",
                "hbm:teleporter",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan"
        ));

        injectMap.put("bypasses_resistance.json", List.of(
                "hbm:digamma",
                "hbm:teleporter",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan"
        ));

        injectMap.put("bypasses_shield.json", List.of(
                "hbm:digamma",
                "hbm:teleporter",
                "hbm:cheater",
                "hbm:black_hole",
                "hbm:blender",
                "hbm:meteorite",
                "hbm:boxcar",
                "hbm:boat",
                "hbm:building",
                "hbm:taint",
                "hbm:ams",
                "hbm:ams_core",
                "hbm:broadcast",
                "hbm:bang",
                "hbm:pc",
                "hbm:cloud",
                "hbm:lead",
                "hbm:enervation",
                "hbm:electricity",
                "hbm:exhaust",
                "hbm:lunar",
                "hbm:monoxide",
                "hbm:asbestos",
                "hbm:blacklung",
                "hbm:mku",
                "hbm:vacuum",
                "hbm:overdose",
                "hbm:microwave",
                "hbm:nitan"
        ));

        injectMap.put("is_projectile.json", List.of(
                "hbm:suicide",
                "hbm:rubble",
                "hbm:shrapnel",
                "hbm:revolver_bullet",
                "hbm:chopper_bullet",
                "hbm:tau",
                "hbm:cmb",
                "hbm:sub_atomic"
        ));

        // =========                               =========

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (var paths = Files.walk(tagsFolder)) {
            for (Path path : paths.collect(Collectors.toList())) {
                if (!Files.isRegularFile(path) || !path.toString().endsWith(".json")) continue;

                String fileName = path.getFileName().toString();
                System.out.println("Checking injection map for file: " + fileName);
                if (!injectMap.containsKey(fileName)) continue;
                System.out.println("Found " + injectMap.get(fileName).size() + " resource locations to inject into " + fileName);

                JsonObject json = gson.fromJson(Files.readString(path), JsonObject.class);

                if (json.has("values") && json.get("values").isJsonArray()) {
                    JsonArray values = json.getAsJsonArray("values");
                    List<String> toInject = injectMap.get(fileName);

                    for (String id : toInject) {
                        boolean alreadyPresent = false;
                        for (JsonElement element : values) {
                            if (element.isJsonPrimitive() && element.getAsString().equals(id)) {
                                alreadyPresent = true;
                                break;
                            }
                        }
                        if (!alreadyPresent) {
                            values.add(id);
                        }
                    }

                    Files.writeString(path, gson.toJson(json));
                }
            }
        }
    }
}
