package com.hbm.nucleartech.block.entity;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HBM.MOD_ID);

    public static final RegistryObject<BlockEntityType<BurnerPressEntity>> BURNER_PRESS_ENTITY =
            BLOCK_ENTITIES.register("burner_press_entity", () ->
                    BlockEntityType.Builder.of(BurnerPressEntity::new,
                            RegisterBlocks.BURNER_PRESS.get()).build(null));

    public static final RegistryObject<BlockEntityType<ShredderEntity>> SHREDDER_ENTITY =
            BLOCK_ENTITIES.register("shredder_entity", () ->
                    BlockEntityType.Builder.of(ShredderEntity::new,
                            RegisterBlocks.SHREDDER.get()).build(null));

    public static void register(IEventBus eventBus) {

        BLOCK_ENTITIES.register(eventBus);
    }
}
