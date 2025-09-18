package com.hbm.nucleartech.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbm.nucleartech.HBM;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PressRecipe implements Recipe<SimpleContainer> {

    private final NonNullList<Ingredient> ingredients;
    private final ItemStack result;
    private final ResourceLocation id;

    public PressRecipe(NonNullList<Ingredient> ingredients,
                       ItemStack result, ResourceLocation id) {

        this.ingredients = ingredients;
        this.result = result;
        this.id = id;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer pContainer, Level pLevel) {

        if(pLevel.isClientSide)
            return false;

        return ingredients.get(0).test(pContainer.getItem(1)) &&
                ingredients.get(1).test(pContainer.getItem(2));
    }

    @Override
    public String toString() {
        return "PressRecipe{" +
                "ingredients=" + ingredients +
                ", result=" + result +
                ", id=" + id +
                '}';
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
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

    public static class Type implements RecipeType<PressRecipe> {

        public static final Type INSTANCE = new Type();
        public static final String ID = "press";
    }

    public static class Serializer implements RecipeSerializer<PressRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "press");

        @Override
        public PressRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {

            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));

            return new PressRecipe(inputs, result, pRecipeId);
        }

        @Override
        public @Nullable PressRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {

            NonNullList<Ingredient> inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromNetwork(pBuffer));

            ItemStack result = pBuffer.readItem();

            return new PressRecipe(inputs, result, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, PressRecipe pRecipe) {

            pBuffer.writeInt(pRecipe.ingredients.size());

            for(Ingredient ingredient : pRecipe.getIngredients())
                ingredient.toNetwork(pBuffer);

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
