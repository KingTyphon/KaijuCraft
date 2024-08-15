package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CombatArmorModel extends GeoModel<CombatArmorItem> {
    @Override
    public ResourceLocation getModelResource(CombatArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/combat_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CombatArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/armor/combat_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CombatArmorItem casualArmorItem) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/combat_armor.json");
    }
}