package com.hbm.nucleartech.network.packet;

import com.hbm.nucleartech.block.entity.ShredderEntity;
import com.hbm.nucleartech.util.FloatingLong;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundShredderPacket {

    public final BlockPos pos;

    public final int leftBIdx;
    public final int rightBIdx;

    public final boolean shred;

    public final int leftDur;
    public final int leftMaxDur;
    public final int rightDur;
    public final int rightMaxDur;

    public FloatingLong storedEnergy;
    public FloatingLong powerDraw;

    public ClientboundShredderPacket(int x, int y, int z,
                                     int leftBIdx, int rightBIdx,
                                     boolean shred, int leftDur,
                                     int leftMaxDur, int rightDur,
                                     int rightMaxDur, FloatingLong storedEnergy,
                                     FloatingLong powerDraw) {

        this.pos = new BlockPos(x, y, z);

        this.leftBIdx = leftBIdx;
        this.rightBIdx = rightBIdx;

        this.shred = shred;

        this.leftDur = leftDur;
        this.leftMaxDur = leftMaxDur;
        this.rightDur = rightDur;
        this.rightMaxDur = rightMaxDur;

        this.storedEnergy = storedEnergy;
        this.powerDraw = powerDraw;
    }

    public static void encode(ClientboundShredderPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.pos.getX());
        buf.writeInt(msg.pos.getY());
        buf.writeInt(msg.pos.getZ());

        buf.writeInt(msg.leftBIdx);
        buf.writeInt(msg.rightBIdx);

        buf.writeBoolean(msg.shred);

        buf.writeInt(msg.leftDur);
        buf.writeInt(msg.leftMaxDur);
        buf.writeInt(msg.rightDur);
        buf.writeInt(msg.rightMaxDur);

        buf.writeUtf(msg.storedEnergy.toString());
        buf.writeUtf(msg.powerDraw.toString());
    }

    public static ClientboundShredderPacket decode(FriendlyByteBuf buf) {
        return new ClientboundShredderPacket(
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readInt(), buf.readBoolean(),
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readInt(), FloatingLong.create(buf.readUtf()),
                FloatingLong.create(buf.readUtf())
        );
    }

    public static void handle(ClientboundShredderPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {

                BlockEntity e = level.getBlockEntity(msg.pos);
                if(e instanceof ShredderEntity shredder) {

                    shredder.leftBIdx = msg.leftBIdx;
                    shredder.rightBIdx = msg.rightBIdx;

                    shredder.shred = msg.shred;

                    shredder.leftDur = msg.leftDur;
                    shredder.leftMaxDur = msg.leftMaxDur;
                    shredder.rightDur = msg.rightDur;
                    shredder.rightMaxDur = msg.rightMaxDur;

                    shredder.storedWattHoursClient = msg.storedEnergy;
                    shredder.currentPowerConsumption = msg.powerDraw;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
