package com.kingtyphon.kaijucraft.client.entity.client;


import com.kingtyphon.kaijucraft.entity.npc.Kikoru;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;



public class KikoruRenderer extends GeoEntityRenderer<Kikoru> {
    private static final String CHESTPLATE = "Body";
    private static final String HELMET = "Head";
    private static final String RIGHTARM = "Right Arm";
    private static final String LEFTARM = "Left Arm";
    private static final String RIGHTLEG = "Right Leg";
    private static final String LEFTLEG = "Left Leg";
    protected ItemStack mainHandItem;
    protected ItemStack offhandItem;

    public KikoruRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new KikoruModel());
        addRenderLayer(new ItemArmorGeoLayer(this) {
            @Nullable
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, LivingEntity animatable) {
                return switch (bone.getName()) {
                    case CHESTPLATE -> this.chestplateStack;
                    case HELMET -> this.helmetStack;
                    case LEFTARM -> this.chestplateStack;
                    case RIGHTARM -> this.chestplateStack;
                    case RIGHTLEG -> this.leggingsStack;
                    case LEFTLEG -> this.leggingsStack;
                    default -> null;
                };
            }

            @NotNull
            @Override
            protected EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, LivingEntity animatable) {
                return switch (bone.getName()) {
                    case CHESTPLATE -> EquipmentSlot.CHEST;
                    case HELMET -> EquipmentSlot.HEAD;
                    case LEFTARM -> EquipmentSlot.CHEST;
                    case RIGHTARM -> EquipmentSlot.CHEST;
                    case RIGHTLEG -> EquipmentSlot.LEGS;
                    case LEFTLEG -> EquipmentSlot.LEGS;

                    default -> super.getEquipmentSlotForBone(bone, stack, animatable);
                };
            }

            @NotNull
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, LivingEntity animatable, HumanoidModel baseModel) {
                return switch (bone.getName()) {
                    case CHESTPLATE -> baseModel.body;
                    case HELMET -> baseModel.head;
                    case LEFTARM -> baseModel.leftArm;
                    case RIGHTARM -> baseModel.rightArm;
                    case RIGHTLEG -> baseModel.rightLeg;
                    case LEFTLEG -> baseModel.leftLeg;
                    default -> super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
                };
            }
        });this.addRenderLayer(new BlockAndItemGeoLayer<Kikoru>(this) {
            @javax.annotation.Nullable
            protected ItemStack getStackForBone(GeoBone bone, Kikoru animatable) {
                ItemStack var10000;
                switch (bone.getName()) {
                    case "leftItem" -> var10000 = animatable.isLeftHanded() ? KikoruRenderer.this.mainHandItem : KikoruRenderer.this.offhandItem;
                    case "rightItem" -> var10000 = animatable.isLeftHanded() ? KikoruRenderer.this.offhandItem : KikoruRenderer.this.mainHandItem;
                    default -> var10000 = null;
                }

                return var10000;
            }

            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, Kikoru animatable) {
                ItemDisplayContext var10000;
                switch (bone.getName()) {
                    case "leftItem":
                    case "rightItem":
                        var10000 = ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
                        break;
                    default:
                        var10000 = ItemDisplayContext.NONE;
                }

                return var10000;
            }


        });
    }
    public void preRender(PoseStack poseStack, Kikoru animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        this.mainHandItem = animatable.getMainHandItem();
        this.offhandItem = animatable.getOffhandItem();
    }
    @Override
    public void render(Kikoru entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}