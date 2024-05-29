package com.hbm.nucleartech.hazard.type;

import com.hbm.nucleartech.hazard.HazardModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;


public abstract class HazardTypeBase {

    /**
     * Does the thing. Called by HazardEntry.applyHazard
     * @param target the holder
     * @param level the final level after calculating all the modifiers
     * @param the stack that is being updated
     */
    public abstract void onUpdate(LivingEntity target, float level, ItemStack stack);

    /**
     * Updates the hazard for dropped items. Used for things like explosive and hydroactive items.
     * @param item
     * @param level
     */
    public abstract void updateEntity(ItemEntity item, float level);


    /**
     * Adds item tooltip info. Called by Item.addInformation
     * @param player
     * @param list
     * @param level the base level, mods are passed separately
     * @param stack
     * @param modifiers
     */
    @OnlyIn(Dist.CLIENT)
    public abstract void addHazardInformation(Player player, List<String> list, float level, ItemStack stack, List<HazardModifier> modifiers);

}
