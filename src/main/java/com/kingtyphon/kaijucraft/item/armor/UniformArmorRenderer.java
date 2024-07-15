package com.kingtyphon.kaijucraft.item.armor;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class UniformArmorRenderer extends GeoArmorRenderer<FormalArmorItem> {
    public UniformArmorRenderer(){
        super(new UniformArmorModel());
    }
}
