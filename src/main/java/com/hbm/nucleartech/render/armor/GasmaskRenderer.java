package com.hbm.nucleartech.render.armor;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.GasmaskItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class GasmaskRenderer extends GeoArmorRenderer<GasmaskItem> {
    public GasmaskRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(HBM.MOD_ID, "armor/m65_mask"))); // Using DefaultedItemGeoModel like this puts our 'location' as item/armor/example armor in the assets folders.
    }
}