package com.hbm.nucleartech.block.entity.client;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.entity.ShredderEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ShredderModel extends GeoModel<ShredderEntity> {

    @Override
    public ResourceLocation getModelResource(ShredderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "geo/block/shredder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShredderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "textures/block/shredder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShredderEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(HBM.MOD_ID, "animations/block/shredder.animation.json");
    }
}
