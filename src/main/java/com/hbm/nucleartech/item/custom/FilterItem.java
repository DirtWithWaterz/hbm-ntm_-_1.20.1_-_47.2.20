package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.handler.ArmorModHandler;
import com.hbm.nucleartech.interfaces.IGasMask;
import com.hbm.nucleartech.sound.RegisterSounds;
import com.hbm.nucleartech.util.ArmorUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FilterItem extends Item {

    public FilterItem(Properties pProperties, int durability) {
        super(pProperties.durability(durability));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        ItemStack helmet = pPlayer.getInventory().armor.get(EquipmentSlot.HEAD.getIndex());
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if(helmet.isEmpty())
            return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));

        if(!(helmet.getItem() instanceof IGasMask)) {

            if(ArmorModHandler.hasMods(helmet)) {

                ItemStack[] mods = ArmorModHandler.pryMods(helmet);

                if(mods[ArmorModHandler.helmet_only.ordinal()] != null) {

                    ItemStack mask = mods[ArmorModHandler.helmet_only.ordinal()];

                    ItemStack ret = installFilterOn(mask, stack, pLevel, pPlayer);
                    ArmorModHandler.applyMod(helmet, mask);

                    return InteractionResultHolder.success(ret);
                }
            }
        }

        return InteractionResultHolder.success(installFilterOn(helmet, stack, pLevel, pPlayer));
    }

    private ItemStack installFilterOn(ItemStack helmet, ItemStack filter, Level level, Player player) {

        if(!(helmet.getItem() instanceof IGasMask))
            return filter;

        IGasMask mask = (IGasMask) helmet.getItem();
        if(!mask.isFilterApplicable(helmet, filter))
            return filter;

        ItemStack copy = filter.copy();
        ItemStack current = ArmorUtil.getGasMaskFilter(helmet);

        if(current != null)
            filter = current;
        else
            filter.shrink(1);

        ArmorUtil.installGasMaskFilter(helmet, copy);

        level.playSound(player, player.getOnPos().offset(0,2,0), RegisterSounds.GAS_MASK_SCREW.get(), SoundSource.PLAYERS, 1f, 1f);

        return filter;
    }
}
