package com.hbm.nucleartech.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegisterParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, "hbm");

    public static final RegistryObject<SimpleParticleType> DECON_PARTICLE =
            PARTICLES.register("decon_particle", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {

        PARTICLES.register(eventBus);
    }
}
