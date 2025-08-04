package com.hbm.nucleartech.block.entity.client;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BurnerPressModel extends GeoModel<BurnerPressEntity> {

    @Override
    public ResourceLocation getModelResource(BurnerPressEntity animatable) {
        return new ResourceLocation(HBM.MOD_ID, "geo/block/burner_press.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BurnerPressEntity animatable) {
        return new ResourceLocation(HBM.MOD_ID, "textures/block/burner_press.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BurnerPressEntity animatable) {
        return new ResourceLocation(HBM.MOD_ID, "animations/block/burner_press.animation.json");
    }
}
