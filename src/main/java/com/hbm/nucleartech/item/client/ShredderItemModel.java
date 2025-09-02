package com.hbm.nucleartech.item.client;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.ShredderItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ShredderItemModel extends GeoModel<ShredderItem> {

    @Override
    public ResourceLocation getModelResource(ShredderItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "geo/item/shredder.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShredderItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "textures/block/shredder.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShredderItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "animations/item/shredder.animation.json");
    }
    //// the last thing you have to do before you can update for the public to 1.6.6 <3 (actually you should figure out the better energy too (whoops))
    @Override
    public void setCustomAnimations(ShredderItem animatable, long instanceId, AnimationState<ShredderItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        float degToRad = (float)(Math.PI / 180.0);

        CoreGeoBone root = getAnimationProcessor().getBone("root");

        if (root != null) {

            var context = animationState.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);

            if (context == ItemDisplayContext.GUI) {
                // Inventory view

                root.setScaleX(0.625f);
                root.setScaleY(0.625f);
                root.setScaleZ(0.625f);

                root.setPosY(-4.44f);
                root.setPosZ(0);

                root.setRotX(39.2315f * degToRad);
                root.setRotY(-37.7612f * degToRad);
                root.setRotZ(-26.5651f * degToRad);
            }
            else if (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {

                root.setScaleX(0.4f);
                root.setScaleY(0.4f);
                root.setScaleZ(0.4f);

                root.setPosY(-3.33f);
                root.setPosZ(0);

                root.setRotX(0 * degToRad);
                root.setRotY(45 * degToRad);
                root.setRotZ(0 * degToRad);
            }
            else if(context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {

                root.setScaleX(0.375f);
                root.setScaleY(0.375f);
                root.setScaleZ(0.375f);

                root.setPosY(1.75f);
                root.setPosZ(-3f);

                root.setRotX(79.2714f * degToRad);
                root.setRotY(10.5453f * degToRad);
                root.setRotZ(44.007f * degToRad);
            }
            else if(context == ItemDisplayContext.GROUND) {

                root.setScaleX(0.25f);
                root.setScaleY(0.25f);
                root.setScaleZ(0.25f);

                root.setPosY(-3f);
                root.setPosZ(0);

                root.setRotX(0 * degToRad);
                root.setRotY(0 * degToRad);
                root.setRotZ(0 * degToRad);
            }
        }
    }
}
