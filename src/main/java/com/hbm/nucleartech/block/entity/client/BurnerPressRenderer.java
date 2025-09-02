package com.hbm.nucleartech.block.entity.client;

import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BurnerPressRenderer extends GeoBlockRenderer<BurnerPressEntity> {

    public BurnerPressRenderer(BlockEntityRendererProvider.Context context) {
        super(new BurnerPressModel());
    }

    @Override
    public boolean shouldRenderOffScreen(BurnerPressEntity pBlockEntity) {
        return true;
    }

    @Override
    public void postRender(PoseStack poseStack, BurnerPressEntity animatable,
                           BakedGeoModel model, MultiBufferSource bufferSource,
                           VertexConsumer buffer, boolean isReRender, float partialTick,
                           int packedLight, int packedOverlay,
                           float red, float green, float blue, float alpha) {

        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        ItemStack displayStack = animatable.getDisplayStack();
        if (!displayStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0, 1.0, 0);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));

            poseStack.scale(0.45f, 0.45f, 1f);

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    displayStack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    animatable.getLevel(),
                    0
            );

            poseStack.popPose();
        }
    }
}
