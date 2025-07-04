package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SigSauerShortRifleModel extends GeoModel<SigSauerShortRifleItem> {
    @Override
    public ResourceLocation getModelResource(SigSauerShortRifleItem sigSauerShortRifleItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/sigsauer.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(SigSauerShortRifleItem sigSauerShortRifleItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/sigsauershortrifle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SigSauerShortRifleItem sigSauerShortRifleItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/sigsauer.json");
    }
}
