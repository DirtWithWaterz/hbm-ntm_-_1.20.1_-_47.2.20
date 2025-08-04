package com.hbm.nucleartech.item.client;

import com.hbm.nucleartech.item.custom.BurnerPressItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BurnerPressItemRenderer extends GeoItemRenderer<BurnerPressItem> {

    public BurnerPressItemRenderer() {
        super(new BurnerPressItemModel());
    }
}
