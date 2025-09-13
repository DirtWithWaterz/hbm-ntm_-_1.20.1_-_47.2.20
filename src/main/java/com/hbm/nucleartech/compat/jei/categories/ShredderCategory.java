package com.hbm.nucleartech.compat.jei.categories;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.block.entity.ShredderEntity;
import com.hbm.nucleartech.datagen.ModRecipeProvider.MetaData;
import com.hbm.nucleartech.recipe.ShredderRecipe;
import com.hbm.nucleartech.screen.ShredderScreen;
import com.hbm.nucleartech.util.FloatingLong;
import com.hbm.nucleartech.util.RegisterTags;
import it.unimi.dsi.fastutil.Pair;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hbm.nucleartech.capability.energy.WattHourStorage.translateWattHours;

public class ShredderCategory implements IRecipeCategory<ShredderRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(HBM.MOD_ID, "shredder");
    public static final ResourceLocation TEXTURE = new ResourceLocation(HBM.MOD_ID,
            "textures/gui/shredder_nei_gui.png");

    public static final RecipeType<ShredderRecipe> SHREDDER_TYPE =
            new RecipeType<>(UID, ShredderRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    private final IGuiHelper helper;

    private static final int XO = 0;
    private static final int YO = 0;

    // static drawables created once
    private final IDrawableStatic shredStatic;
    private final IDrawableStatic energyStatic;

    // cache per-recipe animated drawables keyed by recipe id
    private final Map<ResourceLocation, Pair<IDrawableAnimated, IDrawableAnimated>> animatedCache = new HashMap<>();
    private final Map<ResourceLocation, FloatingLong> energyCache = new HashMap<>();

    public ShredderCategory(IGuiHelper helper) {

        this.helper = helper;
        this.background = helper.createDrawable(TEXTURE, XO, YO, 176, 86);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(RegisterBlocks.SHREDDER.get()));
        this.shredStatic = helper.createDrawable(TEXTURE, 101, 119, 22, 16);
        this.energyStatic = helper.createDrawable(TEXTURE, 36, 86, 16, 52);
    }

    @Override
    public String toString() {
        return "ShredderCategory{" +
                "background=" + background +
                ", icon=" + icon +
                ", helper=" + helper +
                ", shredStatic=" + shredStatic +
                ", energyStatic=" + energyStatic +
                ", animatedCache=" + animatedCache +
                ", energyCache=" + energyCache +
                '}';
    }

    // figure out why you can't get the power bar and progress bar to function on a per-recipe basis.

    @Override
    public void draw(ShredderRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics,
                     double mouseX, double mouseY) {

        ResourceLocation id = recipe.getId();
        Pair<IDrawableAnimated, IDrawableAnimated> pair = animatedCache.get(id);
        if (pair != null) {

            IDrawableAnimated progressArrow = pair.left();
            IDrawableAnimated energyBar = pair.right();
            progressArrow.draw(guiGraphics, 86 - XO, 35 - YO);
            energyBar.draw(guiGraphics, 8 - XO, 17 - YO);
        }
        FloatingLong energyConsumption = energyCache.get(id);
        if(energyConsumption != null) {

            if(ShredderScreen.isMouseOverGuiArea((int)mouseX, (int)mouseY, 8, 17, XO, YO, 16, 52))
                guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.translatable("ui.hbm.power_draw", formatWatts(energyConsumption)), (int)mouseX, (int)mouseY);
        }
    }

    public String formatWatts(FloatingLong wattHours) {

        var value = translateWattHours(wattHours);

        if (value.compareTo(FloatingLong.create(1000)) < 0)
            return value.floatValue() + "W";

        final String[] units = {"W", "kW", "MW", "GW", "TW", "PW", "EW", "ZW", "YW", "RW", "QW", "?W"};

        int unitIndex = 0;
        while (value.compareTo(FloatingLong.create(1000)) >= 0 && unitIndex < units.length - 1) {

            value = value.divide(1000);
            unitIndex++;
        }

        // keep 1 decimal if needed
        String formatted = String.format("%.1f", value.floatValue());

        return formatted + units[unitIndex];
    }

    @Override
    public RecipeType<ShredderRecipe> getRecipeType() {

        return SHREDDER_TYPE;
    }

    @Override
    public Component getTitle() {

        return Component.translatable("block.hbm.shredder");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ShredderRecipe recipe, IFocusGroup focuses) {

        // layout slots as before...
        builder.addSlot(RecipeIngredientRole.INPUT, 44 - XO, 35 - YO).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 89 - XO, 17 - YO).addItemStacks(
                getItemsFromTag(RegisterTags.Items.SHREDDER_BLADES));
        builder.addSlot(RecipeIngredientRole.INPUT, 89 - XO, 53 - YO).addItemStacks(
                getItemsFromTag(RegisterTags.Items.SHREDDER_BLADES));

        List<ItemStack> results = new ArrayList<>();
        for (Pair<Item, MetaData> result : recipe.getResults())
            results.add(new ItemStack(result.left(), result.right().getMaxCount()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 134 - XO, 35 - YO).addItemStacks(results);

        // build per-recipe animations and cache them:
        ResourceLocation id = recipe.getId();
        if (!animatedCache.containsKey(id)) {

            // safe guards
            int ticksPerCycle = Math.max(1, recipe.getTicks());
            FloatingLong energyConsumption = recipe.getPowerConsumption() == null ?
                    FloatingLong.create(1) : recipe.getPowerConsumption();

            // create animated drawables
            IDrawableAnimated progressArrow = helper.createAnimatedDrawable(

                    shredStatic, ticksPerCycle, IDrawableAnimated.StartDirection.LEFT, false
            );

            final FloatingLong maxEnergy = FloatingLong.create(5.0E3);
            int energyTicks;
            if (energyConsumption.compareTo(FloatingLong.ZERO) <= 0) {

                energyTicks = 1;
            } else {

                energyTicks = Math.max(1, maxEnergy.divide(energyConsumption).intValue());
            }
            IDrawableAnimated energyBar = helper.createAnimatedDrawable(

                    energyStatic, energyTicks, IDrawableAnimated.StartDirection.TOP, true
            );

            animatedCache.put(id, Pair.of(progressArrow, energyBar));
            energyCache.put(id, recipe.getPowerConsumption());
        }
    }

    public static List<ItemStack> getItemsFromTag(TagKey<Item> tagKey) {

        List<ItemStack> items = new ArrayList<>();

        for (Item holder : ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList()) {

            items.add(holder.getDefaultInstance());
        }

        return items;
    }
}
