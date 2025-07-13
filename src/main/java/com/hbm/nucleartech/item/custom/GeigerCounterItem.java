package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase;
import com.hbm.nucleartech.sound.RegisterSounds;
import com.hbm.nucleartech.util.ContaminationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeigerCounterItem extends Item {

    public GeigerCounterItem(Properties pProperties) {
        super(pProperties);
    }

    static final Map<Integer, SoundEvent> soundMap = Map.of(
            1, RegisterSounds.GEIGER_1.get(),
            2, RegisterSounds.GEIGER_2.get(),
            3, RegisterSounds.GEIGER_3.get(),
            4, RegisterSounds.GEIGER_4.get(),
            5, RegisterSounds.GEIGER_5.get(),
            6, RegisterSounds.GEIGER_6.get()
    );

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

//        System.out.println("[Debug] Inventory ticking");

        if(!(pEntity instanceof LivingEntity) || pLevel.isClientSide)
            return;

//        System.out.println("[Debug] Server side");

        float x = HbmCapabilities.getData(pEntity).getValue(IEntityCapabilityBase.Type.RADENV);

        if(x == 0)
            x = HbmCapabilities.getData(pEntity).getValue(IEntityCapabilityBase.Type.RADIATION);

        if(pLevel.getGameTime() % 5 == 0) {

//            System.err.println("[Debug] Geiger check");

            if(x > 1e-5) {

//                System.err.println("[Debug] Rad exists, playing sound...");

                List<Integer> list = new ArrayList<>();

                if(x < 1) list.add(0);
                if(x < 5) list.add(0);
                if(x < 10) list.add(1);
                if(x > 5 && x < 15) list.add(2);
                if(x > 10 && x < 20) list.add(3);
                if(x > 15 && x < 25) list.add(4);
                if(x > 20 && x < 30) list.add(5);
                if(x > 25) list.add(6);

                int r = list.get(pLevel.random.nextInt(list.size()));

                if(r > 0)
                    pLevel.playSound(null, pEntity.getOnPos().offset(0,1,0), soundMap.get(r), SoundSource.PLAYERS, 1, 1);
            }
            else if(pLevel.random.nextInt(50) == 0)
                pLevel.playSound(null, pEntity.getOnPos().offset(0,1,0), soundMap.get(1+pLevel.random.nextInt(1)), SoundSource.PLAYERS, 1, 1);
        }
    }

    public static void setFloat(ItemStack stack, float i, String name) {

        stack.getOrCreateTag().putFloat(name, i);
    }

    public static float getFloat(ItemStack stack, String name) {

        if(stack.hasTag())
            if(stack.getTag().contains(name))
                return stack.getTag().getFloat(name);

        return 0;
    }

    public static int check(Level level, int x, int y, int z) {

        return (int)Math.ceil(RadiationSystemChunksNT.getRadForCoord(level, new BlockPos(x,y,z)));
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
