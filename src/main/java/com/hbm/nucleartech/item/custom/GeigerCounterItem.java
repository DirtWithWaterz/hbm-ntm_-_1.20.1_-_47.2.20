package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.sound.RegisterSounds;
import com.hbm.nucleartech.util.ContaminationUtil;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GeigerCounterItem extends Item {

    public GeigerCounterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(!pLevel.isClientSide()) {

            pLevel.playSound(null, pPlayer.getOnPos().offset(0,1,0), RegisterSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1f, 1f);
            ContaminationUtil.printGeigerData(pPlayer);
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
