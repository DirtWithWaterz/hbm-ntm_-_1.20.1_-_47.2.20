package com.hbm.nucleartech.handler;


import com.hbm.nucleartech.AdvancementManager;
import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.lib.ModDamageSource;
import com.hbm.nucleartech.network.HbmLivingProps;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;


@Mod.EventBusSubscriber(modid = HBM.MOD_ID)
public class RadiationSystemNT {

    static MinecraftServer server;

    public static void init(MinecraftServer pServer) {

        server = pServer;

    }

    private static void updateEntityContamination(Level world, boolean updateData) {

        if(world != null && !world.isClientSide /* && GeneralConfig.enableRads */ ) {



//            List<Object> oList = new ArrayList<Object>(world.getEntities(null, null));
//            System.out.println(oList.get(0).toString());

            for(Entity e : new ArrayList<>(world.players())) {

                if(e instanceof LivingEntity){

                    LivingEntity entity = (LivingEntity) e;

                    if(entity instanceof ServerPlayer) {

                        ServerPlayer player = (ServerPlayer) entity;
                        if(player.isCreative() || player.isSpectator())
                            continue;

                        double eRad = (double) HbmLivingProps.getRadiation(player);

                        if(eRad > 2500000)
                            HbmLivingProps.setRadiation(player, 2500000);

                        if(eRad >= 1000) {

                            player.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("hbm:radiation")))), 1000f);

                            // Grant achievement, "Ouch, Radiation!"


                        } else if(eRad >= 800) {

                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 2, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 3 * 20, 1, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 5 * 20, 3, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 5 * 20, 3, true, false));
                        } else if(eRad >= 600) {

                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10 * 20, 2, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20, 2, true, false));
                            if(world.random.nextInt(500) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.POISON, 3 * 20, 1, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 5 * 20, 3, true, false));
                            if(world.random.nextInt(400) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 6 * 20, 2, true, false));
                        } else if(eRad >= 400) {

                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 30, 0, true, false));
                            if(world.random.nextInt(500) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5 * 20, 0, true, false));
                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 1, true, false));
                            if(world.random.nextInt(500) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 3 * 20, 2, true, false));
                            if(world.random.nextInt(600) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4 * 20, 1, true, false));
                        } else if(eRad >= 200) {

                            if(world.random.nextInt(300) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 5 * 20, 0, true, false));
                            if(world.random.nextInt(500) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 5 * 20, 0, true, false));
                            if(world.random.nextInt(700) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 3 * 20, 2, true, false));
                            if(world.random.nextInt(800) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4 * 20, 0, true, false));
                        } else if(eRad >= 100) {

                            if(world.random.nextInt(800) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 2 * 20, 0, true, false));
                            if(world.random.nextInt(1000) == 0)
                                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1 * 20, 0, true, false));

                            // Grant achievement, "Yay, Radiation!"
                            AdvancementManager.grant(player, "story/achradpoison");
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldUpdate(TickEvent.LevelTickEvent e) {

        boolean allowUpdate = (e.phase == TickEvent.Phase.START);

        // Make entities stinky
        updateEntityContamination(e.level, allowUpdate);
    }
}
