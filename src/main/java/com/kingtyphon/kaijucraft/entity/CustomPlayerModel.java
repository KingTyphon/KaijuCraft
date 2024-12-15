package com.kingtyphon.kaijucraft.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CustomPlayerModel extends GeoModel<CustomPlayerWrapper> {
    @Override
    public ResourceLocation getModelResource(CustomPlayerWrapper customPlayerWrapper) {
        return new ResourceLocation("modid", "geo/kaiju8.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CustomPlayerWrapper customPlayerWrapper) {
        return new ResourceLocation("modid", "textures/entity/kaiju8.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CustomPlayerWrapper customPlayerWrapper) {
        return new ResourceLocation("modid", "animations/kaiju8.animation.json");
    }
}
