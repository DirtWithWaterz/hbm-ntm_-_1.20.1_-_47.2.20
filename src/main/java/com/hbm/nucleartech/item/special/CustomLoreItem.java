package com.hbm.nucleartech.item.special;

import com.hbm.nucleartech.item.RegisterItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomLoreItem extends Item {

    Rarity rarity;

    public CustomLoreItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        if(this == RegisterItems.REACHER.get()) {

            pTooltipComponents.add(Component.literal("Holding this in your main or off hand reduces radiation coming from items to their square-root."));
            pTooltipComponents.add(Component.literal("It's also useful for handling both very hot and very cold substances."));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
