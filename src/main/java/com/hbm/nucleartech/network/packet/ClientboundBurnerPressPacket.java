package com.hbm.nucleartech.network.packet;

import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundBurnerPressPacket {

    public final BlockPos pos;
    public final boolean stamping;

    public final int progress;
    public final int maxProgress;
    public final int heat;
    public final int maxHeat;
    public final int fuel;
    public final int maxFuel;
    public final float speedMod;

    public final boolean stamped;

    public final ItemStack displayStack;

    public ClientboundBurnerPressPacket(int x, int y, int z,
                                        boolean stamping, int progress,
                                        int maxProgress, int heat,
                                        int maxHeat, int fuel,
                                        int maxFuel, float speedMod,
                                        boolean stamped, ItemStack displayStack) {

        this.pos = new BlockPos(x, y, z);
        this.stamping = stamping;

        this.progress = progress;
        this.maxProgress = maxProgress;
        this.heat = heat;
        this.maxHeat = maxHeat;
        this.fuel = fuel;
        this.maxFuel = maxFuel;
        this.speedMod = speedMod;

        this.stamped = stamped;

        this.displayStack = displayStack;
    }

    public static void encode(ClientboundBurnerPressPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.pos.getX());
        buf.writeInt(msg.pos.getY());
        buf.writeInt(msg.pos.getZ());
        buf.writeBoolean(msg.stamping);

        buf.writeInt(msg.progress);
        buf.writeInt(msg.maxProgress);
        buf.writeInt(msg.heat);
        buf.writeInt(msg.maxHeat);
        buf.writeInt(msg.fuel);
        buf.writeInt(msg.maxFuel);
        buf.writeFloat(msg.speedMod);

        buf.writeBoolean(msg.stamped);

        buf.writeItemStack(msg.displayStack, false);
    }

    public static ClientboundBurnerPressPacket decode(FriendlyByteBuf buf) {
        return new ClientboundBurnerPressPacket(
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readBoolean(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readInt(), buf.readInt(),
                buf.readInt(), buf.readFloat(), buf.readBoolean(),
                buf.readItem()
        );
    }

    public static void handle(ClientboundBurnerPressPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {

                BlockEntity e = level.getBlockEntity(msg.pos);
                if(e instanceof BurnerPressEntity burnerPress) {

                    burnerPress.stamping = msg.stamping;

                    burnerPress.progress = msg.progress;
                    burnerPress.maxProgress = msg.maxProgress;
                    burnerPress.heat = msg.heat;
                    burnerPress.maxHeat = msg.maxHeat;
                    burnerPress.fuel = msg.fuel;
                    burnerPress.maxFuel = msg.maxFuel;
                    burnerPress.speedMod = msg.speedMod;

                    burnerPress.stamped = msg.stamped;

                    burnerPress.setDisplayStack(msg.displayStack);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
