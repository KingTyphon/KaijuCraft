package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.KaijuPartEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
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

        // Translate slightly downward (adjust value as needed)
        matrixStack.translate(0.0F, 1.5F, 0.0F);

        // Rotate 180 degrees around the X-axis
        matrixStack.mulPose(Axis.XP.rotationDegrees(180));

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(matrixStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
        renderHealthBar(entity, matrixStack, buffer, packedLight);

        super.render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);
    }
    private void renderHealthBar(KaijuPartEntity entity, PoseStack poseStack,
                                 MultiBufferSource buffer, int packedLight) {
        // Only render if close enough
        if (this.entityRenderDispatcher.distanceToSqr(entity) > 4096) return;

        poseStack.pushPose();

        // Move above the entity’s head
        poseStack.translate(0, entity.getBbHeight() + 0.5f, 0);
        // Scale down so text isn’t giant
        poseStack.scale(-0.025f, -0.025f, 0.025f);

        String text = "HP: " + entity.getMeatPoints(); // <-- from SynchedEntityData
        var font = this.getFont();
        float textWidth = font.width(text) / 2f;

        font.drawInBatch(text,
                -textWidth, 0,
                0xFFFFFF, false,
                poseStack.last().pose(),
                buffer, Font.DisplayMode.NORMAL,
                0, packedLight);

        poseStack.popPose();
    }
    @Override
    public ResourceLocation getTextureLocation(KaijuPartEntity entity) {
        return TEXTURE;
    }
}
