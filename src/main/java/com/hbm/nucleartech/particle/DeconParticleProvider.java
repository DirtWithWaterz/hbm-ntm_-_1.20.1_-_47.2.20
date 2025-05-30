package com.hbm.nucleartech.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class DeconParticleProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public DeconParticleProvider(SpriteSet sprites) {

        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY,
                                   double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
        return new DeconParticle(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, sprites);
    }
}
