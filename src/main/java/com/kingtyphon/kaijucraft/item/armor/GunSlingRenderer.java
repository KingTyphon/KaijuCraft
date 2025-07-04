package com.kingtyphon.kaijucraft.item.armor;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GunSlingRenderer extends GeoArmorRenderer<GunSlingItem> {
    public GunSlingRenderer() {
        super(new GunSlingModel());
    }
}
