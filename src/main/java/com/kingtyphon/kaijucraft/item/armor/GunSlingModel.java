package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GunSlingModel extends GeoModel<GunSlingItem> {
    @Override
    public ResourceLocation getModelResource(GunSlingItem gunSlingItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/sigrifleonback.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(GunSlingItem gunSlingItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/sigrifleonback.png");

    }

    @Override
    public ResourceLocation getAnimationResource(GunSlingItem gunSlingItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/sigrifleonback.json");

    }
}
