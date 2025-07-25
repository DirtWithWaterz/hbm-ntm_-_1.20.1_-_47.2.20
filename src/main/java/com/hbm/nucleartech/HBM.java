package com.hbm.nucleartech;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.block.custom.RadResistantBlock;
import com.hbm.nucleartech.entity.HbmEntities;
import com.hbm.nucleartech.entity.client.NuclearCreeperRenderer;
import com.hbm.nucleartech.handler.HazmatRegistry;
import com.hbm.nucleartech.handler.RadiationSystemNT;
import com.hbm.nucleartech.item.RegisterCreativeTabs;
import com.hbm.nucleartech.item.RegisterItems;
import com.hbm.nucleartech.item.custom.GeigerCounterItem;
import com.hbm.nucleartech.network.HbmPacketHandler;
import com.hbm.nucleartech.particle.RegisterParticles;
import com.hbm.nucleartech.sound.RegisterSounds;
import com.hbm.nucleartech.util.ArmorUtil;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HBM.MOD_ID)
public class HBM
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "hbm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static File configDir;
    public static File configHbmDir;

    public HBM() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        GeckoLib.initialize();

        RegisterCreativeTabs.register(modEventBus);
        RegisterItems.register(modEventBus);
        RegisterBlocks.register(modEventBus);
        RegisterParticles.register(modEventBus);
        HbmEntities.register(modEventBus);
//        ClientSetup.init(modEventBus);

        RegisterSounds.SOUNDS.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, MOD_ID),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    private static int messageID = 0;

    public static <T> void addNetworkMessage(
            Class<T> messageType,
            BiConsumer<T, FriendlyByteBuf> encoder,
            Function<FriendlyByteBuf, T> decoder,
            BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {

        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        messageID++;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        configDir = FMLPaths.CONFIGDIR.get().toFile();
        configHbmDir = new File(configDir.getAbsolutePath() + File.separatorChar + "hbmConfig");

        if(!configHbmDir.exists())
            configHbmDir.mkdir();

        HbmPacketHandler.register();
        HazmatRegistry.registerHazmats();
        ArmorUtil.register();

        GeigerCounterItem.initSoundMap();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {


    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        AdvancementManager.init(event.getServer());
        RadiationSystemNT.init(event.getServer());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(HbmEntities.NUCLEAR_CREEPER.get(), NuclearCreeperRenderer::new);
        }
    }
}
