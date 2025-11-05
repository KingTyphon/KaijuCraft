package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.item.armor.CasualArmorItem;
import com.kingtyphon.kaijucraft.item.armor.NHCleaningItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class NHCleaningModel extends GeoModel<NHCleaningItem> {
    @Override
    public ResourceLocation getModelResource(NHCleaningItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/cleaning_uniform.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NHCleaningItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/cleaning_uniform.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NHCleaningItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/cleaning_uniform.json");
    }
}