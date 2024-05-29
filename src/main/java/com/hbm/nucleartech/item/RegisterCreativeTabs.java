package com.hbm.nucleartech.item;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.RegisterBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HBM.MOD_ID);

    public static final RegistryObject<CreativeModeTab> NTM_RESOURCES_AND_PARTS = CREATIVE_TABS.register("resources_and_parts",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterItems.INGOT_BERYLLIUM.get()))
                    .title(Component.translatable("creativetab.resources_and_parts"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(RegisterItems.INGOT_TITANIUM.get());
                        output.accept(RegisterItems.INGOT_BERYLLIUM.get());
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> NTM_BLOCKS = CREATIVE_TABS.register("ntm_blocks",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(RegisterBlocks.BLOCK_TITANIUM.get()))
                    .title(Component.translatable("creativetab.ntm_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(RegisterBlocks.BLOCK_TITANIUM.get());
                        output.accept(RegisterBlocks.ORE_TITANIUM.get());
                        output.accept(RegisterBlocks.DEEPSLATE_ORE_TITANIUM.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {

        CREATIVE_TABS.register(eventBus);
    }
}
