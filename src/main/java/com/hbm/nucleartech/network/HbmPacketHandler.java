package com.hbm.nucleartech.network;

import com.hbm.nucleartech.HBM; // Replace with your actual mod class
import com.hbm.nucleartech.network.packet.ClientboundSpawnDeconParticlePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkDirection;

import java.util.Optional;

public class HbmPacketHandler {

    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HBM.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(packetId++,
                ClientboundSpawnDeconParticlePacket.class,
                ClientboundSpawnDeconParticlePacket::encode,
                ClientboundSpawnDeconParticlePacket::decode,
                ClientboundSpawnDeconParticlePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }
}
