package com.hbm.nucleartech.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.datagen.ModRecipeProvider.MetaData;
import com.hbm.nucleartech.util.FloatingLong;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShredderRecipe implements Recipe<SimpleContainer> {

    private final Ingredient ingredient;
    private final NonNullList<Pair<Item, MetaData>> results;
    private final int ticks;
    private final FloatingLong powerConsumption;
    private final ResourceLocation id;

    public ShredderRecipe(Ingredient ingredient,
                          NonNullList<Pair<Item, MetaData>> results,
                          int ticks,
                          FloatingLong powerConsumption,
                          ResourceLocation id) {
        this.ingredient = ingredient;
        this.results = results;
        this.ticks = ticks;
        this.powerConsumption = powerConsumption;
        this.id = id;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide) return false;
        return ingredient.test(pContainer.getItem(0));
    }

    /**
     * For "crafting table" logic. Here we just return the first output.
     */
    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer,
                                       @NotNull RegistryAccess pRegistryAccess) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).left().getDefaultInstance().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    /**
     * Used by JEI and book lookups. Return the "main" result (first in list).
     */
    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return results.isEmpty() ? ItemStack.EMPTY : results.get(0).left().getDefaultInstance().copy();
    }

    /** Our own accessor for *all* results */
    public NonNullList<Pair<Item, MetaData>> getResults() {
        return results;
    }

    public int getTicks() {

        return ticks;
    }

    public FloatingLong getPowerConsumption() {

        return powerConsumption;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {

        NonNullList<Ingredient> result = NonNullList.createWithCapacity(1);
        result.add(0, ingredient);

        return result;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ShredderRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "shredder";
    }

    public static class Serializer implements RecipeSerializer<ShredderRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(HBM.MOD_ID, "shredder");

        @Override
        public ShredderRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
            // Ingredient
            Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));

            // Multiple results
            NonNullList<Pair<Item, MetaData>> results = NonNullList.create();

            JsonArray arr = GsonHelper.getAsJsonArray(json, "results");
            for (JsonElement e : arr) {

                JsonObject obj = e.getAsJsonObject();

                Item left = GsonHelper.getAsItem(obj, "item");

                JsonObject metadata = GsonHelper.getAsJsonObject(obj, "metadata");

                int minAmount = GsonHelper.getAsInt(metadata, "min_amount");
                int maxAmount = GsonHelper.getAsInt(metadata, "max_amount");
                int chance = GsonHelper.getAsInt(metadata, "chance");

                MetaData right = new MetaData(minAmount, maxAmount, chance);

                results.add(Pair.of(left, right));
            }

            int ticks = GsonHelper.getAsInt(json, "ticks");
            FloatingLong powerConsumption = FloatingLong.create(GsonHelper.getAsString(json, "power_consumption"));

            return new ShredderRecipe(input, results, ticks, powerConsumption, pRecipeId);
        }

        @Override
        public @Nullable ShredderRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf buf) {
            Ingredient input = Ingredient.fromNetwork(buf);

            int count = buf.readVarInt();
            NonNullList<Pair<Item, MetaData>> results = NonNullList.withSize(count, Pair.of(null, null));
            for (int i = 0; i < count; i++) {
                results.set(i, Pair.of(buf.readItem().getItem(), new MetaData(buf.readInt(), buf.readInt(), buf.readInt())));
            }

            int ticks = buf.readInt();
            FloatingLong powerConsumption = FloatingLong.create(buf.readUtf());

            return new ShredderRecipe(input, results, ticks, powerConsumption, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ShredderRecipe recipe) {
            recipe.ingredient.toNetwork(buf);

            buf.writeVarInt(recipe.results.size());
            for (Pair<Item, MetaData> stack : recipe.results) {
                buf.writeItem(stack.left().getDefaultInstance());
                buf.writeInt(stack.right().getMinCount());
                buf.writeInt(stack.right().getMaxCount());
                buf.writeInt(stack.right().getChance());
            }

            buf.writeInt(recipe.ticks);
            buf.writeUtf(recipe.powerConsumption.toString());
        }
    }
}
