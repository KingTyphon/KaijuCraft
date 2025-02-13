package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.KaijuPartEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class KaijuPartRender extends EntityRenderer<KaijuPartEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaijupart.png");
    private final KaijuPartModel model;

    public KaijuPartRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new KaijuPartModel(pContext.bakeLayer(ModelLayers.KAIJUPARTS));

    }

    @Override
    public void render(KaijuPartEntity entity, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

        super.render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(KaijuPartEntity entity) {
        return TEXTURE;
    }
}
