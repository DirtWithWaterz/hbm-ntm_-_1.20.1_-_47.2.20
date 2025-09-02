package com.hbm.nucleartech.compat.jei;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.compat.jei.categories.PressCategory;
import com.hbm.nucleartech.compat.jei.categories.ShredderCategory;
import com.hbm.nucleartech.recipe.PressRecipe;
import com.hbm.nucleartech.recipe.ShredderRecipe;
import com.hbm.nucleartech.screen.BurnerPressScreen;
import com.hbm.nucleartech.screen.ShredderScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEINuclearTechPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(HBM.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {

        registration.addRecipeCategories(
                new PressCategory(registration.getJeiHelpers().getGuiHelper()),
                new ShredderCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        List<PressRecipe> burnerPressRecipes = recipeManager.getAllRecipesFor(PressRecipe.Type.INSTANCE);
        registration.addRecipes(PressCategory.BURNER_PRESS_TYPE, burnerPressRecipes);

        List<ShredderRecipe> shredderRecipes = recipeManager.getAllRecipesFor(ShredderRecipe.Type.INSTANCE);
        registration.addRecipes(ShredderCategory.SHREDDER_TYPE, shredderRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {

        registration.addRecipeClickArea(BurnerPressScreen.class, 80, 35, 16, 16,
                PressCategory.BURNER_PRESS_TYPE);

        registration.addRecipeClickArea(ShredderScreen.class, 64, 90-28, 33, 12,
                ShredderCategory.SHREDDER_TYPE);
    }
}
