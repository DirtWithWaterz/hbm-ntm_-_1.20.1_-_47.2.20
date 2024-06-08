package com.hbm.nucleartech.datagen;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final List<ItemLike> TITANIUM_SMELTABLES = List.of(RegisterBlocks.ORE_TITANIUM.get(), RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get(), RegisterItems.RAW_TITANIUM.get());
    private static final List<ItemLike> URANIUM_SMELTABLES = List.of(RegisterBlocks.ORE_URANIUM.get(), RegisterBlocks.DEEPSLATE_ORE_URANIUM.get(), RegisterItems.RAW_URANIUM.get(), RegisterItems.CRYSTAL_URANIUM.get());

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        oreSmelting(consumer, TITANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.INGOT_TITANIUM.get(), 0.7f, 200, "ingot_titanium");
        oreBlasting(consumer, TITANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.INGOT_TITANIUM.get(), 0.7f, 100, "ingot_titanium");

        oreSmelting(consumer, URANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.INGOT_URANIUM.get(), 1.0f, 200, "ingot_uranium");
        oreBlasting(consumer, URANIUM_SMELTABLES, RecipeCategory.MISC, RegisterItems.INGOT_URANIUM.get(), 1.0f, 100, "ingot_uranium");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterBlocks.BLOCK_TITANIUM.get())
                .pattern("TTT")
                .pattern("TTT")
                .pattern("TTT")
                .define('T', RegisterItems.INGOT_TITANIUM.get())
                .unlockedBy(getHasName(RegisterItems.INGOT_TITANIUM.get()), has(RegisterItems.INGOT_TITANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterBlocks.BLOCK_TITANIUM.get()) + "_from_" + getItemName(RegisterItems.INGOT_TITANIUM.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterBlocks.BLOCK_URANIUM.get())
                .pattern("UUU")
                .pattern("UUU")
                .pattern("UUU")
                .define('U', RegisterItems.INGOT_URANIUM.get())
                .unlockedBy(getHasName(RegisterItems.INGOT_URANIUM.get()), has(RegisterItems.INGOT_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterBlocks.BLOCK_URANIUM.get()) + "_from_" + getItemName(RegisterItems.INGOT_URANIUM.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterItems.INGOT_URANIUM.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', RegisterItems.NUGGET_URANIUM.get())
                .unlockedBy(getHasName(RegisterItems.INGOT_URANIUM.get()), has(RegisterItems.INGOT_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.INGOT_URANIUM.get()) + "_from_" + getItemName(RegisterItems.NUGGET_URANIUM.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RegisterItems.PILE_ROD_URANIUM.get())
                .pattern(" B ")
                .pattern("PBP")
                .pattern(" B ")
                .define('B', RegisterItems.BILLET_URANIUM.get())
                .define('P', RegisterItems.PLATE_IRON.get())
                .unlockedBy(getHasName(RegisterItems.INGOT_URANIUM.get()), has(RegisterItems.INGOT_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.PILE_ROD_URANIUM.get()) + "_from_" + getItemName(RegisterItems.BILLET_URANIUM.get()) + "_and_" + getItemName(RegisterItems.PLATE_IRON.get()));


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.INGOT_TITANIUM.get(), 9)
                .requires(RegisterBlocks.BLOCK_TITANIUM.get())
                .unlockedBy(getHasName(RegisterBlocks.BLOCK_TITANIUM.get()), has(RegisterBlocks.BLOCK_TITANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.INGOT_TITANIUM.get()) + "_from_" + getItemName(RegisterBlocks.BLOCK_TITANIUM.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.INGOT_URANIUM.get(), 9)
                .requires(RegisterBlocks.BLOCK_URANIUM.get())
                .unlockedBy(getHasName(RegisterBlocks.BLOCK_URANIUM.get()), has(RegisterBlocks.BLOCK_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.INGOT_URANIUM.get()) + "_from_" + getItemName(RegisterBlocks.BLOCK_URANIUM.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.NUGGET_URANIUM.get(), 9)
                .requires(RegisterItems.INGOT_URANIUM.get())
                .unlockedBy(getHasName(RegisterItems.INGOT_URANIUM.get()), has(RegisterItems.INGOT_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.NUGGET_URANIUM.get()) + "_from_" + getItemName(RegisterItems.INGOT_URANIUM.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.BILLET_URANIUM.get(), 3)
                .requires(RegisterItems.INGOT_URANIUM.get(), 2)
                .unlockedBy(getHasName(RegisterItems.INGOT_URANIUM.get()), has(RegisterItems.INGOT_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.BILLET_URANIUM.get()) + "_from_" + getItemName(RegisterItems.INGOT_URANIUM.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.INGOT_URANIUM.get(), 2)
                .requires(RegisterItems.BILLET_URANIUM.get(), 3)
                .unlockedBy(getHasName(RegisterItems.BILLET_URANIUM.get()), has(RegisterItems.BILLET_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.INGOT_URANIUM.get()) + "_from_" + getItemName(RegisterItems.BILLET_URANIUM.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RegisterItems.NUGGET_URANIUM.get(), 6)
                .requires(RegisterItems.BILLET_URANIUM.get())
                .unlockedBy(getHasName(RegisterItems.BILLET_URANIUM.get()), has(RegisterItems.BILLET_URANIUM.get()))
                .save(consumer, HBM.MOD_ID + ":" + getItemName(RegisterItems.NUGGET_URANIUM.get()) + "_from_" + getItemName(RegisterItems.BILLET_URANIUM.get()));
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
}
