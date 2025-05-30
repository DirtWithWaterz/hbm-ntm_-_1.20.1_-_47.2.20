package com.hbm.nucleartech.event;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.entity.HbmEntities;
import com.hbm.nucleartech.entity.custom.NuclearCreeperEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = HBM.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HbmEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HbmEntities.NUCLEAR_CREEPER.get(), NuclearCreeperEntity.createAttributes().build());
    }
}
