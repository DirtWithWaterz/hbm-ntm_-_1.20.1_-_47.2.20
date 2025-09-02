package com.hbm.nucleartech.capability;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.capability.entity.LivingEntityCapability;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase.Type;
import com.hbm.nucleartech.interfaces.IWattHourStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class HbmCapabilities {

    public static final Capability<LivingEntityCapability> LIVING_ENTITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IWattHourStorage> WATT_HOUR_STORAGE = CapabilityManager.get(new CapabilityToken<>() {});



    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {

        HBM.addNetworkMessage(
                PlayerCapabilitiesSyncMessage.class,
                PlayerCapabilitiesSyncMessage::buffer,
                PlayerCapabilitiesSyncMessage::new,
                PlayerCapabilitiesSyncMessage::handler
        );
    }

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent event) {

        event.register(LivingEntityCapability.class);
    }

    @Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {

        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerCapability(PlayerEvent.PlayerLoggedInEvent event) {

            if(!event.getEntity().level().isClientSide())
                ((LivingEntityCapability) event.getEntity().getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability())).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerCapability(PlayerEvent.PlayerRespawnEvent event) {
            if (!event.getEntity().level().isClientSide())
                ((LivingEntityCapability) event.getEntity().getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability())).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerCapability(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (!event.getEntity().level().isClientSide())
                ((LivingEntityCapability) event.getEntity().getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability())).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            event.getOriginal().revive();
            LivingEntityCapability original = ((LivingEntityCapability) event.getOriginal().getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability()));
            LivingEntityCapability clone = ((LivingEntityCapability) event.getEntity().getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability()));
            if (!event.isWasDeath()) {
                clone.setValue(Type.RADIATION, original.getValue(Type.RADIATION));
                clone.setValue(Type.NEUTRON, original.getValue(Type.NEUTRON));
                clone.setValue(Type.DIGAMMA, original.getValue(Type.DIGAMMA));
                clone.setValue(Type.ASBESTOS, original.getValue(Type.ASBESTOS));
                clone.setValue(Type.BLACKLUNG, original.getValue(Type.BLACKLUNG));
                clone.setValue(Type.RADENV, original.getValue(Type.RADENV));
                clone.setValue(Type.RADBUF, original.getValue(Type.RADBUF));
                clone.setValue(Type.BOMB_TIMER, original.getValue(Type.BOMB_TIMER));
                clone.setValue(Type.CONTAGION, original.getValue(Type.CONTAGION));
                clone.setValue(Type.OIL, original.getValue(Type.OIL));
                clone.setValue(Type.FIRE, original.getValue(Type.FIRE));
                clone.setValue(Type.PHOSPHORUS, original.getValue(Type.PHOSPHORUS));
                clone.setValue(Type.BALEFIRE, original.getValue(Type.BALEFIRE));
                clone.setValue(original.getValue());
            }
        }
    }


    @Mod.EventBusSubscriber
    public static class LivingEntityCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {

            if(event.getObject() instanceof Mob)
                event.addCapability(new ResourceLocation(HBM.MOD_ID, "living_entity_capability"), new LivingEntityCapabilityProvider());
        }

        private final LivingEntityCapability capability = new LivingEntityCapability();


        public static final ResourceLocation ID = new ResourceLocation(HBM.MOD_ID, "living_entity_capability");

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {

            return cap == LIVING_ENTITY_CAPABILITY ? LazyOptional.of(() -> capability).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {

            return capability.writeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {

            capability.readNBT(nbt);
        }
    }

    @Mod.EventBusSubscriber
    private static class PlayerEntityCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {

            if(event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
                event.addCapability(new ResourceLocation(HBM.MOD_ID, "player_entity_capability"), new PlayerEntityCapabilityProvider());
        }

        private final LivingEntityCapability capability = new LivingEntityCapability();

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == LIVING_ENTITY_CAPABILITY ? LazyOptional.of(() -> capability).cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {

            return capability.writeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {

            capability.readNBT(nbt);
        }
    }

    public static class PlayerCapabilitiesSyncMessage {

        private final LivingEntityCapability data;

        public PlayerCapabilitiesSyncMessage(FriendlyByteBuf buffer) {

            this.data = new LivingEntityCapability();
            this.data.readNBT(buffer.readNbt());
        }

        public PlayerCapabilitiesSyncMessage(LivingEntityCapability data) {

            this.data = data;
        }

        public static void buffer(PlayerCapabilitiesSyncMessage message, FriendlyByteBuf buffer) {

            buffer.writeNbt(message.data.writeNBT());
        }

        public static void handler(PlayerCapabilitiesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {

            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {

                if(!context.getDirection().getReceptionSide().isServer()) {

                    LivingEntityCapability capability = ((LivingEntityCapability) Minecraft.getInstance().player.getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(new LivingEntityCapability()));
                    capability.setValue(Type.RADIATION, message.data.getValue(Type.RADIATION));
                    capability.setValue(Type.NEUTRON, message.data.getValue(Type.NEUTRON));
                    capability.setValue(Type.DIGAMMA, message.data.getValue(Type.DIGAMMA));
                    capability.setValue(Type.ASBESTOS, message.data.getValue(Type.ASBESTOS));
                    capability.setValue(Type.BLACKLUNG, message.data.getValue(Type.BLACKLUNG));
                    capability.setValue(Type.RADENV, message.data.getValue(Type.RADENV));
                    capability.setValue(Type.RADBUF, message.data.getValue(Type.RADBUF));
                    capability.setValue(Type.BOMB_TIMER, message.data.getValue(Type.BOMB_TIMER));
                    capability.setValue(Type.CONTAGION, message.data.getValue(Type.CONTAGION));
                    capability.setValue(Type.OIL, message.data.getValue(Type.OIL));
                    capability.setValue(Type.FIRE, message.data.getValue(Type.FIRE));
                    capability.setValue(Type.PHOSPHORUS, message.data.getValue(Type.PHOSPHORUS));
                    capability.setValue(Type.BALEFIRE, message.data.getValue(Type.BALEFIRE));
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static LivingEntityCapability getData(Entity entity) {

        if(entity instanceof Player player) {

            return player.getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(

                    new LivingEntityCapability()
            );
        }
        else if(entity instanceof LivingEntity mob) {

            return mob.getCapability(LIVING_ENTITY_CAPABILITY, null).orElse(

                    new LivingEntityCapability()
            );
        }
        else {

            return null;
        }
    }
}
