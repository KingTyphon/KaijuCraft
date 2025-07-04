package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.client.Kaiju_no8Renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;


public class SkellyKaijuArmorRenderer extends GeoArmorRenderer<SkellyKaijuArmorItem> {
    public SkellyKaijuArmorRenderer() {
        super(new SkellyKaijuArmorModel());
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
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
