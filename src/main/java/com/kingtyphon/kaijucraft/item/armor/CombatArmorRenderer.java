package com.kingtyphon.kaijucraft.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CombatArmorRenderer extends GeoArmorRenderer<CombatArmorItem> {
    public CombatArmorRenderer() {super(new CombatArmorModel());}

    public void prepForRender(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        // Ensure this is properly implemented
        super.prepForRender(livingEntity, itemStack, equipmentSlot, original);
    }

}