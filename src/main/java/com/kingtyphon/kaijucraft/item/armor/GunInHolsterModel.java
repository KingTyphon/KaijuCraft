package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GunInHolsterModel extends GeoModel<GunInHolsterItem> {
    @Override
    public ResourceLocation getModelResource(GunInHolsterItem gunInHolsterItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/glock17_layer.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(GunInHolsterItem gunInHolsterItem) {

        return new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GunInHolsterItem gunInHolsterItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/glock17_layer.json");
    }
}
