package com.hbm.nucleartech.render.model;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.HazmatHeadRedItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HazmatRedModel extends GeoModel<HazmatHeadRedItem> {


    @Override
    public ResourceLocation getModelResource(HazmatHeadRedItem item) {
        return new ResourceLocation(HBM.MOD_ID, "geo/item/armor/hazmat_red.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HazmatHeadRedItem item) {
        return new ResourceLocation(HBM.MOD_ID, "textures/item/armor/hazmat_red.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HazmatHeadRedItem item) {
        return new ResourceLocation(HBM.MOD_ID, "animations/item/armor/hazmat_red.animation.json");
    }
}