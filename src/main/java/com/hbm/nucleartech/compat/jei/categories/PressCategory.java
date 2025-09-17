package com.hbm.nucleartech.compat.jei.categories;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.recipe.PressRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PressCategory implements IRecipeCategory<PressRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "press");
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID,
            "textures/gui/burner_press_gui.png");

    public static final RecipeType<PressRecipe> BURNER_PRESS_TYPE =
            new RecipeType<>(UID, PressRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated stampAnim;

    private static final int XO = 75;
    private static final int YO = 12;

    public PressCategory(IGuiHelper helper) {

        this.background = helper.createDrawable(TEXTURE, XO, YO, 90, 62);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegisterBlocks.BURNER_PRESS.get()));

        IDrawableStatic stampStatic = helper.createDrawable(TEXTURE, 195, 0, 16, 16);

        this.stampAnim = helper.createAnimatedDrawable(stampStatic, 40, IDrawableAnimated.StartDirection.TOP, false);
    }

    @Override
    public String toString() {
        return "PressCategory{" +
                "background=" + background +
                ", icon=" + icon +
                ", stampAnim=" + stampAnim +
                '}';
    }

    @Override
    public void draw(PressRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {

        stampAnim.draw(guiGraphics, 80-XO, 35-YO);
    }

    @Override
    public RecipeType<PressRecipe> getRecipeType() {
        return BURNER_PRESS_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.hbm.press");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PressRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 80-XO, 17-YO).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 80-XO, 53-YO).addIngredients(recipe.getIngredients().get(1));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 140-XO, 35-YO).addItemStack(recipe.getResultItem(null));
    }
}
