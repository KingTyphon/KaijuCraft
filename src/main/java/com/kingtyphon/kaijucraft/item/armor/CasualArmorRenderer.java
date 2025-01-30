package com.kingtyphon.kaijucraft.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CasualArmorRenderer extends GeoArmorRenderer<CasualArmorItem> {
    public CasualArmorRenderer(){super(new CasualArmorModel());
    }
    public void prepForRender(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        // Ensure this is properly implemented
        super.prepForRender(livingEntity, itemStack, equipmentSlot, original);

        // Hide all parts initially
        getGeoModel().getBone("head").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("body").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("rightArm").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("leftArm").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("rightLeg").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("leftLeg").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("rightBoot").ifPresent(bone -> bone.setHidden(true));
        getGeoModel().getBone("leftBoot").ifPresent(bone -> bone.setHidden(true));

        // Show only the part that matches the equipped slot
        switch (equipmentSlot) {
            case HEAD -> getGeoModel().getBone("head").ifPresent(bone -> bone.setHidden(false));
            case CHEST -> {
                getGeoModel().getBone("body").ifPresent(bone -> bone.setHidden(false));
                getGeoModel().getBone("rightArm").ifPresent(bone -> bone.setHidden(false));
                getGeoModel().getBone("leftArm").ifPresent(bone -> bone.setHidden(false));
            }
            case LEGS -> {
                getGeoModel().getBone("rightLeg").ifPresent(bone -> bone.setHidden(false));
                getGeoModel().getBone("leftLeg").ifPresent(bone -> bone.setHidden(false));
            }
            case FEET -> {
                getGeoModel().getBone("rightBoot").ifPresent(bone -> bone.setHidden(false));
                getGeoModel().getBone("leftBoot").ifPresent(bone -> bone.setHidden(false));
            }
        }
    }
}