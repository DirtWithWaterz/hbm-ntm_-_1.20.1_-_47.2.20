package com.hbm.nucleartech.render.model;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.HazmatHeadItem;
import com.hbm.nucleartech.item.custom.base.GasmaskItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HazmatModel extends GeoModel<HazmatHeadItem> {


    @Override
    public ResourceLocation getModelResource(HazmatHeadItem item) {
        return new ResourceLocation(HBM.MOD_ID, "geo/item/armor/hazmat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HazmatHeadItem item) {
        return new ResourceLocation(HBM.MOD_ID, "textures/item/armor/hazmat.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HazmatHeadItem item) {
        return new ResourceLocation(HBM.MOD_ID, "animations/item/armor/hazmat.animation.json");
    }
}