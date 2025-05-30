package com.hbm.nucleartech.particle;

import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;

public class DeconParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected DeconParticle(ClientLevel world, double x, double y, double z,
                            double dx, double dy, double dz, SpriteSet sprites) {
        super(world, x, y, z, dx, dy, dz);
        this.setSize(1F, 1F);
        this.lifetime = world.random.nextInt(15)+1;
        this.gravity = 0.01F;
        this.setSpriteFromAge(sprites);
        this.xd = dx;
        this.yd = dy;
        this.zd = dz;
        this.sprites = sprites;
        this.scale(1.5F);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites); // this.sprite is a TextureAtlasSprite which is not the required type.
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; // ParticleRenderType.PARTICLE_SHEET does not exist.
    }
}
