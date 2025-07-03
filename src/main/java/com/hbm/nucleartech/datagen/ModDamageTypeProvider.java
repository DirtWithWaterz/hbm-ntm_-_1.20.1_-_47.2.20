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

        Map<ResourceLocation, JsonObject> damageTypes = Map.ofEntries(

                Map.entry(new ResourceLocation(HBM.MOD_ID, "blast"),
                        createDamageTypeJson("blast", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "nuclear_blast"),
                        createDamageTypeJson("nuclear_blast", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "mud_poisoning"),
                        createDamageTypeJson("mud_poisoning", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "acid"),
                        createDamageTypeJson("acid", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "euthanized_self"),
                        createDamageTypeJson("euthanized_self", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "euthanized_self_2"),
                        createDamageTypeJson("euthanized_self_2", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "tau_blast"),
                        createDamageTypeJson("tau_blast", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "radiation"),
                        createDamageTypeJson("radiation", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "digamma"),
                        createDamageTypeJson("digamma", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "suicide"),
                        createDamageTypeJson("suicide", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "teleporter"),
                        createDamageTypeJson("teleporter", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "cheater"),
                        createDamageTypeJson("cheater", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "rubble"),
                        createDamageTypeJson("rubble", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "shrapnel"),
                        createDamageTypeJson("shrapnel", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "black_hole"),
                        createDamageTypeJson("black_hole", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "blender"),
                        createDamageTypeJson("blender", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "meteorite"),
                        createDamageTypeJson("meteorite", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "boxcar"),
                        createDamageTypeJson("boxcar", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "boat"),
                        createDamageTypeJson("boat", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "building"),
                        createDamageTypeJson("building", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "taint"),
                        createDamageTypeJson("taint", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "ams"),
                        createDamageTypeJson("ams", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "ams_core"),
                        createDamageTypeJson("ams_core", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "broadcast"),
                        createDamageTypeJson("broadcast", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "bang"),
                        createDamageTypeJson("bang", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "pc"),
                        createDamageTypeJson("pc", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "cloud"),
                        createDamageTypeJson("cloud", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "lead"),
                        createDamageTypeJson("lead", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "enervation"),
                        createDamageTypeJson("enervation", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "electricity"),
                        createDamageTypeJson("electricity", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "exhaust"),
                        createDamageTypeJson("exhaust", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "spikes"),
                        createDamageTypeJson("spikes", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "lunar"),
                        createDamageTypeJson("lunar", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "monoxide"),
                        createDamageTypeJson("monoxide", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "asbestos"),
                        createDamageTypeJson("asbestos", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "blacklung"),
                        createDamageTypeJson("blacklung", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "mku"),
                        createDamageTypeJson("mku", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "vacuum"),
                        createDamageTypeJson("vacuum", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "overdose"),
                        createDamageTypeJson("overdose", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "microwave"),
                        createDamageTypeJson("microwave", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "nitan"),
                        createDamageTypeJson("nitan", 0.1f, Scaling.never, Effect.hurt)),
                Map.entry(new ResourceLocation(HBM.MOD_ID, "revolver_bullet"), // Indirect
                        createDamageTypeJson("revolver_bullet", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "chopper_bullet"), // Indirect
                        createDamageTypeJson("chopper_bullet", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "tau"), // Indirect
                        createDamageTypeJson("tau", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "cmb"), // Indirect
                        createDamageTypeJson("cmb", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "sub_atomic"), // Indirect
                        createDamageTypeJson("sub_atomic", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "euthanized"), // Indirect
                        createDamageTypeJson("euthanized", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "electrified"), // Indirect
                        createDamageTypeJson("electrified", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "flamethrower"), // Indirect
                        createDamageTypeJson("flamethrower", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "plasma"), // Indirect
                        createDamageTypeJson("plasma", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "ice"), // Indirect
                        createDamageTypeJson("ice", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "laser"), // Indirect
                        createDamageTypeJson("laser", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "boil"), // Indirect
                        createDamageTypeJson("boil", 0.1f, Scaling.never, Effect.hurt)), // Indirect
                Map.entry(new ResourceLocation(HBM.MOD_ID, "acid_player"), // Indirect
                        createDamageTypeJson("acid_player", 0.1f, Scaling.never, Effect.hurt)) // Indirect
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
}

