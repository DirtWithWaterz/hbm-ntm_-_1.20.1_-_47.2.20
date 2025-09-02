package com.hbm.nucleartech.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.recipe.RegisterRecipes;
import com.hbm.nucleartech.util.FloatingLong;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final List<ItemLike> TITANIUM_SMELTABLES = List.of(RegisterBlocks.TITANIUM_ORE.get(), RegisterBlocks.DEEPSLATE_TITANIUM_ORE.get(), RegisterItems.RAW_TITANIUM.get());
    private static final List<ItemLike> URANIUM_SMELTABLES = List.of(RegisterBlocks.URANIUM_ORE.get(), RegisterBlocks.DEEPSLATE_URANIUM_ORE.get(), RegisterItems.RAW_URANIUM.get(), RegisterItems.URANIUM_CRYSTAL.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        oreSmelting(consumer, TITANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.TITANIUM_INGOT.get(), 0.7f, 200, "ingot_titanium");
        oreBlasting(consumer, TITANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.TITANIUM_INGOT.get(), 0.7f, 100, "ingot_titanium");

        oreSmelting(consumer, URANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.URANIUM_INGOT.get(), 1.0f, 200, "ingot_uranium");
        oreBlasting(consumer, URANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.URANIUM_INGOT.get(), 1.0f, 100, "ingot_uranium");

        platePressing(consumer, Items.IRON_INGOT, RegisterItems.IRON_PLATE.get());

        List<Pair<ItemLike, MetaData>> results;

        results = List.of(
                Pair.of(RegisterItems.RAW_THORIUM.get(), new MetaData(1, 1, 100)),
                Pair.of(Items.CLAY_BALL, new MetaData(0, 2, 100)),
                Pair.of(Items.GOLD_NUGGET, new MetaData(1, 1, 5)),
                Pair.of(RegisterItems.THORIUM_POWDER.get(), new MetaData(1, 1, 2))
        );
        itemShredding(consumer, RegisterItems.THORIUM_SHALE.get(), results, FloatingLong.create(1.39E1), 60);

        results = List.of(
                Pair.of(RegisterItems.THORIUM_POWDER.get(), new MetaData(1, 1, 100))
        );
        itemShredding(consumer, RegisterItems.THORIUM_INGOT.get(), results, FloatingLong.create(2.083E1), 80);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterBlocks.TITANIUM_BLOCK.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', RegisterItems.TITANIUM_INGOT.get())
                .unlockedBy(getHasName(RegisterItems.TITANIUM_INGOT.get()), has(RegisterItems.TITANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterBlocks.TITANIUM_BLOCK.get()) + "_from_" + getItemName(RegisterItems.TITANIUM_INGOT.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterBlocks.URANIUM_BLOCK.get())
                .pattern("UUU")
                .pattern("UUU")
                .pattern("UUU")
                .define('U', RegisterItems.URANIUM_INGOT.get())
                .unlockedBy(getHasName(RegisterItems.URANIUM_INGOT.get()), has(RegisterItems.URANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterBlocks.URANIUM_BLOCK.get()) + "_from_" + getItemName(RegisterItems.URANIUM_INGOT.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterItems.URANIUM_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', RegisterItems.URANIUM_NUGGET.get())
                .unlockedBy(getHasName(RegisterItems.URANIUM_INGOT.get()), has(RegisterItems.URANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_INGOT.get()) + "_from_" + getItemName(RegisterItems.URANIUM_NUGGET.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterItems.URANIUM_PILE_ROD.get())
                .pattern(" B ")
                .pattern("PBP")
                .pattern(" B ")
                .define('B', RegisterItems.URANIUM_BILLET.get())
                .define('P', RegisterItems.IRON_PLATE.get())
                .unlockedBy(getHasName(RegisterItems.URANIUM_INGOT.get()), has(RegisterItems.URANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_PILE_ROD.get()) + "_from_" + getItemName(RegisterItems.URANIUM_BILLET.get()) + "_and_" + getItemName(RegisterItems.IRON_PLATE.get()));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.TITANIUM_INGOT.get(), 9)
                .requires(RegisterBlocks.TITANIUM_BLOCK.get())
                .unlockedBy(getHasName(RegisterBlocks.TITANIUM_BLOCK.get()), has(RegisterBlocks.TITANIUM_BLOCK.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.TITANIUM_INGOT.get()) + "_from_" + getItemName(RegisterBlocks.TITANIUM_BLOCK.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.URANIUM_INGOT.get(), 9)
                .requires(RegisterBlocks.URANIUM_BLOCK.get())
                .unlockedBy(getHasName(RegisterBlocks.URANIUM_BLOCK.get()), has(RegisterBlocks.URANIUM_BLOCK.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_INGOT.get()) + "_from_" + getItemName(RegisterBlocks.URANIUM_BLOCK.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.URANIUM_NUGGET.get(), 9)
                .requires(RegisterItems.URANIUM_INGOT.get())
                .unlockedBy(getHasName(RegisterItems.URANIUM_INGOT.get()), has(RegisterItems.URANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_NUGGET.get()) + "_from_" + getItemName(RegisterItems.URANIUM_INGOT.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.URANIUM_BILLET.get(), 3)
                .requires(RegisterItems.URANIUM_INGOT.get(), 2)
                .unlockedBy(getHasName(RegisterItems.URANIUM_INGOT.get()), has(RegisterItems.URANIUM_INGOT.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_BILLET.get()) + "_from_" + getItemName(RegisterItems.URANIUM_INGOT.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.URANIUM_INGOT.get(), 2)
                .requires(RegisterItems.URANIUM_BILLET.get(), 3)
                .unlockedBy(getHasName(RegisterItems.URANIUM_BILLET.get()), has(RegisterItems.URANIUM_BILLET.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_INGOT.get()) + "_from_" + getItemName(RegisterItems.URANIUM_BILLET.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.URANIUM_NUGGET.get(), 6)
                .requires(RegisterItems.URANIUM_BILLET.get())
                .unlockedBy(getHasName(RegisterItems.URANIUM_BILLET.get()), has(RegisterItems.URANIUM_BILLET.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.URANIUM_NUGGET.get()) + "_from_" + getItemName(RegisterItems.URANIUM_BILLET.get()));
    }

    public static class MetaData {

        protected final int minCount;
        protected final int maxCount;
        protected final int chance;

        public MetaData(int minCount, int maxCount, int chance) {

            this.minCount = minCount;
            this.maxCount = maxCount;
            this.chance = chance;
        }

        public int getMinCount() {
            return minCount;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public int getChance() {
            return chance;
        }
    }

    private static String getItemId(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) {
            throw new IllegalStateException("Item not registered: " + item);
        }
        return id.toString(); // full "namespace:path"
    }

    private static String getItemPath(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) {
            throw new IllegalStateException("Item not registered: " + item);
        }
        return id.getPath(); // just "path"
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        Iterator var9 = pIngredients.iterator();

        while(var9.hasNext()) {
            ItemLike itemlike = (ItemLike)var9.next();
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, HBM.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }

    protected static void itemShredding(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pInput, List<Pair<ItemLike, MetaData>> pResults, FloatingLong pPowerConsumption, int pTicks) {

//        System.err.println(pResults.toString());

        pFinishedRecipeConsumer.accept(
                new ShredderRecipeBuilder(
                        pInput,
                        pResults,
                        pPowerConsumption,
                        pTicks
                )
        );
    }

    public record ShredderRecipeBuilder(ItemLike input, List<Pair<ItemLike, MetaData>> results, FloatingLong powerConsumption, int ticks) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            // Type of recipe
            json.addProperty("type", "hbm:shredder");

            // Serialize ingredient
            json.add("ingredient", Ingredient.of(input).toJson()); // Ingredient has a built-in toJson() method

            // Serialize results as an array
            JsonArray resultsArray = new JsonArray();
            for (Pair<ItemLike, MetaData> stack : results) { // assuming resultItems is a List<ItemLike>

                System.err.println(getItemName(stack.left().asItem()));

                JsonObject jsonResult = new JsonObject();
                jsonResult.addProperty("item", getItemId(stack.left().asItem()));

//                ======= Meta Data =======

                JsonObject metaData = new JsonObject();

                metaData.addProperty("min_amount", stack.right().getMinCount());
                metaData.addProperty("max_amount", stack.right().getMaxCount());
                metaData.addProperty("chance", stack.right().getChance());

//                =========================

                jsonResult.add("metadata", metaData);

                resultsArray.add(jsonResult);
            }

            json.add("results", resultsArray);

            json.addProperty("power_consumption", powerConsumption.toString());

            json.addProperty("ticks", ticks);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(HBM.MOD_ID, getItemName(results.get(0).left().asItem()) + "_from_" + getItemName(input) + "_with_shredder");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegisterRecipes.SHREDDER.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null; // no advancement
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }

    protected static void platePressing(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pInput, ItemLike pResult) {

        for(ItemLike item : ModItemTagGenerator.SharedTagLists.PLATE_STAMPS) {

            pFinishedRecipeConsumer.accept(
                    new PressRecipeBuilder(
                            pInput,
                            item,
                            pResult
                    )
            );
        }
    }

    public record PressRecipeBuilder(ItemLike input, ItemLike stamp, ItemLike result) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {

            json.addProperty("type", "hbm:press");
            ItemLike[] ingredients = new ItemLike[]{stamp, input};

            JsonArray jsonIngredients = new JsonArray();

            for(int i = 0; i < 2; i++)
                jsonIngredients.add(Ingredient.of(ingredients[i]).toJson());

            json.add("ingredients", jsonIngredients);

            JsonObject jsonResult = new JsonObject();
            jsonResult.addProperty("count", 1);
            jsonResult.addProperty("item", getItemId(result.asItem()));

            json.add("result", jsonResult);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(HBM.MOD_ID, getItemName(result) + "_from_" + getItemName(input) + "_with_" + getItemName(stamp) + "_press");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return RegisterRecipes.PRESS.get();
        }

        @Override
        public JsonObject serializeAdvancement() {
            return null; // no advancement
        }

        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
