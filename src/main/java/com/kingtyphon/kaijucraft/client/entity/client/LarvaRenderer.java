package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class LarvaRenderer extends GeoEntityRenderer<LarvaEntity> {
    public LarvaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LarvaModel());

        addRenderLayer(new LarvaEmissiveLayer<>(this));
    }
    @Override
    public ResourceLocation getTextureLocation(LarvaEntity animatable){
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/larva.png");
    }
    @Override
    public void render(LarvaEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight){
        poseStack.scale(0.5F,0.5F,0.5F);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    public class LarvaEmissiveLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
        private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/larva_glowmask.png");

        public LarvaEmissiveLayer(GeoRenderer<T> entityRenderer) {
            super(entityRenderer);
        }

        @Override
        public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            RenderType emissiveRenderType = RenderType.eyes(EMISSIVE_TEXTURE);

            getRenderer().reRender(
                    bakedModel,  // Pass the baked model
                    poseStack,
                    bufferSource,
                    animatable,
                    emissiveRenderType,
                    bufferSource.getBuffer(emissiveRenderType),
                    partialTick, // Ensure correct parameter order
                    packedLight,
                    packedOverlay,
                    1.0F, 1.0F, 1.0F, 1.0F
            );
        }
    }

}
