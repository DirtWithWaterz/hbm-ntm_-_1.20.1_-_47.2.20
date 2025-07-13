package com.hbm.nucleartech.item.custom;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.handler.RadiationSystemChunksNT;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.sound.RegisterSounds;
import com.hbm.nucleartech.util.ContaminationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeigerCounterItem extends Item {

    public GeigerCounterItem(Properties pProperties) {
        super(pProperties);
    }

    public static final Map<Integer, SoundEvent> soundMap = new HashMap<>();

    public static void initSoundMap() {
        soundMap.put(1, RegisterSounds.GEIGER_1.get());
        soundMap.put(2, RegisterSounds.GEIGER_2.get());
        soundMap.put(3, RegisterSounds.GEIGER_3.get());
        soundMap.put(4, RegisterSounds.GEIGER_4.get());
        soundMap.put(5, RegisterSounds.GEIGER_5.get());
        soundMap.put(6, RegisterSounds.GEIGER_6.get());
        soundMap.put(7, RegisterSounds.GEIGER_7.get());
        soundMap.put(8, RegisterSounds.GEIGER_8.get());
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

//        System.out.println("[Debug] Inventory ticking");

        if(!(pEntity instanceof LivingEntity) || pLevel.isClientSide)
            return;



//        System.out.println("[Debug] Server side");

        float x = getAvgRad((LivingEntity) pEntity);

        if(pLevel.getGameTime() % 5 == 0) {

//            System.err.println("[Debug] Geiger check");

            if(x > 1e-5) {

                System.err.println("[Debug] Rad exists, playing sound...");

                List<Integer> list = new ArrayList<>();

                if(x < 1) list.add(0);
                if(x < 5) list.add(0);
                if(x < 10) list.add(1);
                if(x > 5 && x < 15) list.add(2);
                if(x > 10 && x < 20) list.add(3);
                if(x > 15 && x < 25) list.add(4);
                if(x > 20 && x < 30) list.add(5);
                if(x > 25 && x < 55) list.add(6);
                if(x > 40 && x < 80) list.add(7);
                if(x > 60) list.add(8);

                int r = list.get(pLevel.random.nextInt(list.size()));

                if(r > 0) {

                    SoundEvent sound = soundMap.get(r);
                    if (sound == null) {
                        System.err.println("[Error] Missing geiger sound for level " + r);
                    } else {
                        pLevel.playSound(null, pEntity.getOnPos().offset(0,1,0), sound, SoundSource.PLAYERS, 1, 1);
                    }
                }
//                    pLevel.playSound(null, pEntity.getOnPos().offset(0,1,0), soundMap.get(r), SoundSource.PLAYERS, 1, 1);
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

    public static float getAvgRad(LivingEntity pEntity) {

        return (HbmCapabilities.getData(pEntity).getValue(IEntityCapabilityBase.Type.RADENV) +
                HbmCapabilities.getData(pEntity).getValue(IEntityCapabilityBase.Type.RADIATION)) / 2f;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(!pLevel.isClientSide()) {

            pLevel.playSound(null, pPlayer.getOnPos().offset(0,1,0), RegisterSounds.TECH_BOOP.get(), SoundSource.PLAYERS, 1f, 1f);
            ContaminationUtil.printGeigerData(pPlayer);
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public class CustomHudOverlay {

        private static final ResourceLocation RAD_COUNTER = new ResourceLocation(HBM.MOD_ID, "textures/misc/overlay_misc.png");

        private static long lastRadSurvey;
        private static float prevRadResult;
        private static float lastRadResult;

        @SubscribeEvent
        public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
            Minecraft mc = Minecraft.getInstance();

            if (mc.player == null || mc.options.hideGui) return;

            // Check for your custom item (example: check for a compass)
            boolean hasItem = false;
            for (ItemStack stack : mc.player.getInventory().items) {
                if (stack.getItem() == RegisterItems.GEIGER_COUNTER.get()) { // Replace with your item
                    hasItem = true;
                    break;
                }
            }

            if (hasItem) {

                GuiGraphics guiGraphics = event.getGuiGraphics();

                if (mc.player == null || mc.options.hideGui) return;

                // Simulated radiation input (replace with your actual rad data)
                float radInput = getAvgRad(mc.player); // Example value

                float radiation = lastRadResult - prevRadResult;

                if(System.currentTimeMillis() >= lastRadSurvey + 1000) {
                    lastRadSurvey = System.currentTimeMillis();
                    prevRadResult = lastRadResult;
                    lastRadResult = radInput;
                }

                GuiGraphics gui = event.getGuiGraphics();

                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();

                int posX = 3; // Replace with configurable position if needed
                int posY = screenHeight - 20;

                int maxRad = 1000;
                int barLength = (int)(74 * Math.min(radInput / maxRad, 1.0f));

                // Render the background bar
                gui.blit(RAD_COUNTER, posX, posY, 0, 0, 94, 18);

                // Render the foreground bar (progress)
                gui.blit(RAD_COUNTER, posX + 1, posY + 1, 1, 19, barLength, 16);

                // Render the radiation indicator icon
                if (radiation >= 25) {
                    gui.blit(RAD_COUNTER, posX + 94 + 2, posY, 36, 36, 18, 18);
                } else if (radiation >= 10) {
                    gui.blit(RAD_COUNTER, posX + 94 + 2, posY, 18, 36, 18, 18);
                } else if (radiation >= 2.5) {
                    gui.blit(RAD_COUNTER, posX + 94 + 2, posY, 0, 36, 18, 18);
                }

                // Draw the text
                if (radiation > 1000) {
                    gui.drawString(mc.font, Component.literal(">1000 RAD/s"), posX, posY - 10, 0xFF0000);
                } else if (radiation >= 1) {
                    gui.drawString(mc.font, Component.literal(((int) Math.round(radiation)) + " RAD/s"), posX, posY - 10, 0xFFFF00);
                } else if (radiation > 0) {
                    gui.drawString(mc.font, Component.literal("<1 RAD/s"), posX, posY - 10, 0x00FF00);
                }
            }
        }
    }
}
