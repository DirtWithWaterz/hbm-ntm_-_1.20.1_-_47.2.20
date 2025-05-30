package com.hbm.nucleartech.network.packet;

import com.hbm.nucleartech.particle.RegisterParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSpawnDeconParticlePacket {

    public final double x, y, z;
    public final double dx, dy, dz;

    public ClientboundSpawnDeconParticlePacket(double x, double y, double z, double dx, double dy, double dz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static void encode(ClientboundSpawnDeconParticlePacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeDouble(msg.dx);
        buf.writeDouble(msg.dy);
        buf.writeDouble(msg.dz);
    }

    public static ClientboundSpawnDeconParticlePacket decode(FriendlyByteBuf buf) {
        return new ClientboundSpawnDeconParticlePacket(
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readDouble(), buf.readDouble(), buf.readDouble()
        );
    }

    public static void handle(ClientboundSpawnDeconParticlePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                level.addParticle(RegisterParticles.DECON_PARTICLE.get(),
                        msg.x, msg.y, msg.z,
                        msg.dx, msg.dy, msg.dz);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
