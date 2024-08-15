package com.kingtyphon.kaijucraft.item.guns;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Glock17Gen4Renderer extends GeoItemRenderer<Glock17Gen4> {
    public Glock17Gen4Renderer(){
        super(new Glock17Gen4Model());
    }
}
