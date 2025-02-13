package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;

import software.bernie.geckolib.model.GeoModel;

public class GasMaskModel extends GeoModel<GasMaskItem> {
    @Override
    public ResourceLocation getModelResource(GasMaskItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/gas_mask.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GasMaskItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/gas_mask.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GasMaskItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/gas_mask.json");
    }
}