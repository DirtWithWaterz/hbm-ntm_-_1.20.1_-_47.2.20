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
        injectMap.put("bypasses_armor.json", List.of("hbm:radiation"));
        injectMap.put("is_explosion.json", List.of("hbm:nuclear_blast", "hbm:blast"));

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
