package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class Kaiju_no8Renderer extends MobRenderer<Kaiju_no8Entity, Kaiju_no8Model<Kaiju_no8Entity>> {
    private static final ResourceLocation KAIJU_NO8_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8.png");

    public Kaiju_no8Renderer(EntityRendererProvider.Context pContext) {
        super(pContext, new Kaiju_no8Model<>(pContext.bakeLayer(ModelLayers.KAIJU_NO8_LAYER)), .7f);
        this.addLayer(new EmissiveLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Kaiju_no8Entity kaijuNo8Entity) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8.png");
    }
    @Override
    public void render(Kaiju_no8Entity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight){

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
    private static class EmissiveLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
        private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8_glowmask.png");

        public EmissiveLayer(RenderLayerParent<T, M> entityRenderer) {
            super(entityRenderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            // Set up the emissive render type
            var renderType = RenderType.eyes(EMISSIVE_TEXTURE);

            // Render the model using the emissive texture
            this.getParentModel().renderToBuffer(
                    poseStack,
                    buffer.getBuffer(renderType),
                    0xF000F0, // Maximum light for glowing effect
                    OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
