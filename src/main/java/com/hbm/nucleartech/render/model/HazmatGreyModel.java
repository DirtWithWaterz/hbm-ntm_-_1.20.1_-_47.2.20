package com.hbm.nucleartech.render.model;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.HazmatHeadGreyItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HazmatGreyModel extends GeoModel<HazmatHeadGreyItem> {


    @Override
    public ResourceLocation getModelResource(HazmatHeadGreyItem item) {
        return new ResourceLocation(HBM.MOD_ID, "geo/item/armor/hazmat_grey.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HazmatHeadGreyItem item) {
        return new ResourceLocation(HBM.MOD_ID, "textures/item/armor/hazmat_grey.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HazmatHeadGreyItem item) {
        return new ResourceLocation(HBM.MOD_ID, "animations/item/armor/hazmat_grey.animation.json");
    }
}