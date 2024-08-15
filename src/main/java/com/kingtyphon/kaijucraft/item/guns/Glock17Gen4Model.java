package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Glock17Gen4Model extends GeoModel<Glock17Gen4> {
    @Override
    public ResourceLocation getModelResource(Glock17Gen4 glock17Gen4) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/glock17gen4.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Glock17Gen4 glock17Gen4) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17gen4.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Glock17Gen4 glock17Gen4) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/glock17gen4.json");
    }
}
