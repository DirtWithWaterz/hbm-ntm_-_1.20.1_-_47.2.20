package com.hbm.nucleartech.recipe;

import com.hbm.nucleartech.HBM;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, HBM.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PressRecipe>> PRESS =
            RECIPE_SERIALIZERS.register("press", () -> PressRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {

        RECIPE_SERIALIZERS.register(eventBus);
    }
}
