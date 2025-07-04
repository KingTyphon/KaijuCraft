package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BattleAxeRenderer  extends GeoItemRenderer<BattleAxeItem> {
    public BattleAxeRenderer() {
        super(new BattleAxeModel());
    }
}
