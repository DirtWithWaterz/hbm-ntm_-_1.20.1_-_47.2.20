package com.hbm.nucleartech.item.special;

import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomLoreArmorItem extends ArmorItem {

    Rarity rarity;

    public CustomLoreArmorItem(ArmorMaterial material, Type type, Properties pProperties) {

        super(material, type, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if(this == RegisterItems.REACHER.get()) {

            pTooltipComponents.add(Component.literal("Holding this in your main or off hand reduces radiation coming from items to their square-root."));
            pTooltipComponents.add(Component.literal("It's also useful for handling both very hot and very cold substances."));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }
}
