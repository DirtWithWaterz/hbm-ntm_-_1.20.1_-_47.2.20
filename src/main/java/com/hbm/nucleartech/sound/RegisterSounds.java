package com.hbm.nucleartech.sound;

import com.hbm.nucleartech.HBM;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegisterSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, HBM.MOD_ID);

    public static final RegistryObject<SoundEvent> TECH_BOOP = SOUNDS.register("item.tech_boop",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.tech_boop")));
    public static final RegistryObject<SoundEvent> GEIGER_1 = SOUNDS.register("item.geiger1",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger1")));
    public static final RegistryObject<SoundEvent> GEIGER_2 = SOUNDS.register("item.geiger2",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger2")));
    public static final RegistryObject<SoundEvent> GEIGER_3 = SOUNDS.register("item.geiger3",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger3")));
    public static final RegistryObject<SoundEvent> GEIGER_4 = SOUNDS.register("item.geiger4",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger4")));
    public static final RegistryObject<SoundEvent> GEIGER_5 = SOUNDS.register("item.geiger5",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger5")));
    public static final RegistryObject<SoundEvent> GEIGER_6 = SOUNDS.register("item.geiger6",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(HBM.MOD_ID, "item.geiger6")));
}
