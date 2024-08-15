package com.kingtyphon.kaijucraft.item.armor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CombatArmorRenderer extends GeoArmorRenderer<CombatArmorItem> {
    public CombatArmorRenderer() {super(new CombatArmorModel());}


}