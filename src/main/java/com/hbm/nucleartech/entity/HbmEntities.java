package com.hbm.nucleartech.entity;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.entity.custom.NuclearCreeperEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class HbmEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HBM.MOD_ID);

    public static final RegistryObject<EntityType<NuclearCreeperEntity>> NUCLEAR_CREEPER =
            ENTITY_TYPES.register("nuclear_creeper", () -> EntityType.Builder.of(NuclearCreeperEntity::new, MobCategory.MONSTER)
                    .sized(1f, 2f).build("nuclear_creeper"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
