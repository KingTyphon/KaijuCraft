package com.kingtyphon.kaijucraft.item.guns;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CannonRenderer extends GeoItemRenderer<Cannon> {
    public CannonRenderer() {
        super(new CannonModel());
    }
}
