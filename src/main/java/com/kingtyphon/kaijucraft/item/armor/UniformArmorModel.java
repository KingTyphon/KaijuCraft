package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class UniformArmorModel extends GeoModel<FormalArmorItem> {
    @Override
    public ResourceLocation getModelResource(FormalArmorItem formalArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/mdefenseformal_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FormalArmorItem formalArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/mdefenseformal_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FormalArmorItem formalArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/mdefenseformal_armor.json");
    }
}
