package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SkellyKaijuArmorModel extends GeoModel<SkellyKaijuArmorItem> {
    @Override
    public ResourceLocation getModelResource(SkellyKaijuArmorItem skellyKaijuArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/sculkkaiju_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SkellyKaijuArmorItem skellyKaijuArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/sculkkaiju_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SkellyKaijuArmorItem skellyKaijuArmorItem) {
       return new ResourceLocation(KaijuCraft.MODID, "animations/sculkkaiju_armor.json");

    }
}
