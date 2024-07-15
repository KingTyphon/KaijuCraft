package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CasualArmorModel extends GeoModel<CasualArmorItem> {
    @Override
    public ResourceLocation getModelResource(CasualArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/mdefensecasual_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CasualArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/mdefensecasual_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CasualArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/mdefensecasual_armor.json");
    }
}