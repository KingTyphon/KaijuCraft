package com.kingtyphon.kaijucraft.item.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class GasMaskRenderer extends GeoArmorRenderer<GasMaskItem> {

    public GasMaskRenderer() {
        super((new GasMaskModel()));
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
}}
}
