package com.hbm.nucleartech.item.client;

import com.hbm.nucleartech.item.custom.ShredderItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ShredderItemRenderer extends GeoItemRenderer<ShredderItem> {

    public ShredderItemRenderer() {
        super(new ShredderItemModel());
    }
}
