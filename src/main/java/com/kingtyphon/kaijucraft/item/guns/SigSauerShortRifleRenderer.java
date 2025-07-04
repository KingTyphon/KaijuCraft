package com.kingtyphon.kaijucraft.item.guns;

import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SigSauerShortRifleRenderer extends GeoItemRenderer<SigSauerShortRifleItem> {
    public SigSauerShortRifleRenderer() {
        super(new SigSauerShortRifleModel());
//        addRenderLayer(new SlingRenderLayer(this));
    }
}
