package com.kingtyphon.kaijucraft.item.armor;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NHCleaningRenderer extends GeoArmorRenderer<NHCleaningItem> {
    public NHCleaningRenderer() {
        super(new NHCleaningModel());
    }
}
