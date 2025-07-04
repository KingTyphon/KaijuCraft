package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BattleAxeModel extends GeoModel<BattleAxeItem> {
    @Override
    public ResourceLocation getModelResource(BattleAxeItem battleAxeItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/battleaxe.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(BattleAxeItem battleAxeItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/battleaxe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BattleAxeItem battleAxeItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/battleaxe.json");
    }
}
