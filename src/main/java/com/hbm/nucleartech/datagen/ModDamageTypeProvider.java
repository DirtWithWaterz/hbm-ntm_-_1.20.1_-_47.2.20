package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModDamageTypeProvider implements DataProvider {

    private final PackOutput output;

    public ModDamageTypeProvider(PackOutput output) {

        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {

        Map<ResourceLocation, JsonObject> damageTypes = Map.of(

                new ResourceLocation(HBM.MOD_ID, "radiation"),
                createDamageTypeJson("radiation", 0.1F, Scaling.never, Effect.hurt),
                new ResourceLocation(HBM.MOD_ID, "nuclear_blast"),
                createDamageTypeJson("nuclear_blast", 0.1F, Scaling.never, Effect.hurt),
                new ResourceLocation(HBM.MOD_ID, "blast"),
                createDamageTypeJson("blast", 0.1F, Scaling.never, Effect.hurt)
        );

        return CompletableFuture.allOf(
                damageTypes.entrySet().stream().map(entry -> {
                    Path path = output.getOutputFolder()
                            .resolve("data/" + entry.getKey().getNamespace() + "/damage_type/" + entry.getKey().getPath() + ".json");
                    return DataProvider.saveStable(pOutput, entry.getValue(), path);
                }).toArray(CompletableFuture[]::new)
        );
    }

    private JsonObject createDamageTypeJson(String messageId, float exhaustion, Scaling scaling,
                                            Effect effect) {

        JsonObject json = new JsonObject();
        json.addProperty("effects", effect.toString());
        json.addProperty("scaling", scaling.toString());
        json.addProperty("exhaustion", exhaustion);
        json.addProperty("message_id", messageId);
        return json;
    }

    @Override
    public String getName() {
        return HBM.MOD_ID + ":ModDamageTypeProvider";
    }
}

enum Scaling {

    never,
    always,
    when_caused_by_living_non_player
}

enum Effect {

    hurt,
    thorns,
    drowning,
    burning,
    poking,
    freezing
}
