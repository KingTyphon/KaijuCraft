package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CannonModel extends GeoModel<Cannon> {
    @Override
    public ResourceLocation getModelResource(Cannon cannon) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/cannon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Cannon cannon) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/cannon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Cannon cannon) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/cannon.json");
    }
}
