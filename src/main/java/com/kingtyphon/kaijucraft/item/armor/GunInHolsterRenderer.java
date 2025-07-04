package com.kingtyphon.kaijucraft.item.armor;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GunInHolsterRenderer extends GeoArmorRenderer<GunInHolsterItem> {


    public GunInHolsterRenderer() {
        super(new GunInHolsterModel());
    }
}
