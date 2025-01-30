package com.kingtyphon.kaijucraft.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class RenderModelForPlayer<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<T, M> {
    private final ResourceLocation texture;
    private final M mainModel;

    public RenderModelForPlayer(EntityRendererProvider.Context context, M model, String textureName, float shadowSize) {
        super(context, model, shadowSize);
        if (textureName != null) {
            this.texture = new ResourceLocation(textureName + ".png");
        } else {
            this.texture = null;
        }
        if (textureName != null) {
            this.mainModel = model;
        } else {
            this.mainModel = null;
        }
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Example transformation (you can customize this further)
        poseStack.translate(0.0D, 1.5D, 0.0D);
        poseStack.scale(1.0F, -1.0F, 1.0F); // Invert Y for proper orientation

        boolean isRiding = entity.isPassenger();
        this.getModel().riding = isRiding;
        this.getModel().young = entity.isBaby();

        // Render the entity model
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }
}
