package com.hbm.nucleartech.block.entity.client;

import com.hbm.nucleartech.block.entity.ShredderEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ShredderRenderer extends GeoBlockRenderer<ShredderEntity> {

    public ShredderRenderer(BlockEntityRendererProvider.Context context) {
        super(new ShredderModel());
    }

    @Override
    public boolean shouldRenderOffScreen(ShredderEntity pBlockEntity) {
        return true;
    }
}
