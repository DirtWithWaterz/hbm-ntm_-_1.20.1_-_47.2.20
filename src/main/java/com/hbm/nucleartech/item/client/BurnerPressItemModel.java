package com.hbm.nucleartech.item.client;

import com.hbm.nucleartech.HBM;
import com.hbm.nucleartech.item.custom.BurnerPressItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BurnerPressItemModel extends GeoModel<BurnerPressItem> {

    @Override
    public ResourceLocation getModelResource(BurnerPressItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "geo/item/burner_press.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BurnerPressItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "textures/block/burner_press.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BurnerPressItem animatable) {
        return new ResourceLocation(HBM.MOD_ID, "animations/item/burner_press.animation.json");
    }

    @Override
    public void setCustomAnimations(BurnerPressItem animatable, long instanceId, AnimationState<BurnerPressItem> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        float degToRad = (float)(Math.PI / 180.0);

        CoreGeoBone root = getAnimationProcessor().getBone("root"); // change name to your root bone name (often "root")

        if (root != null) {

            var context = animationState.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);

            if (context == ItemDisplayContext.GUI) {
                // Inventory view

                root.setScaleX(0.3f);
                root.setScaleY(0.3f);
                root.setScaleZ(0.3f);

                root.setPosY(-6.5f);

                root.setRotX(39.2315f * degToRad);
                root.setRotY(-37.7612f * degToRad);
                root.setRotZ(-26.5651f * degToRad);
            }
            else if (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {

                root.setScaleX(0.3f);
                root.setScaleY(0.3f);
                root.setScaleZ(0.3f);

                root.setPosY(-6.5f);

                root.setRotX(0 * degToRad);
                root.setRotY(-45 * degToRad);
                root.setRotZ(0 * degToRad);
            }
            else if(context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {

                root.setScaleX(0.15f);
                root.setScaleY(0.15f);
                root.setScaleZ(0.15f);

                root.setPosY(-1f);

                root.setRotX(0 * degToRad);
                root.setRotY(-45 * degToRad);
                root.setRotZ(0 * degToRad);
            }
            else if(context == ItemDisplayContext.GROUND) {

                root.setScaleX(0.15f);
                root.setScaleY(0.15f);
                root.setScaleZ(0.15f);

                root.setPosY(0f);

                root.setRotX(0 * degToRad);
                root.setRotY(0 * degToRad);
                root.setRotZ(0 * degToRad);
            }
        }
    }
}
