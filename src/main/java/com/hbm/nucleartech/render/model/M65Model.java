package com.hbm.nucleartech.render.model;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.base.GasmaskItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class M65Model extends GeoModel<GasmaskItem> {


    @Override
    public ResourceLocation getModelResource(GasmaskItem gasmaskItem) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "geo/item/armor/m65_mask.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GasmaskItem gasmaskItem) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "textures/item/armor/m65_mask.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GasmaskItem gasmaskItem) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "animations/item/armor/m65_mask.animation.json");
    }
}