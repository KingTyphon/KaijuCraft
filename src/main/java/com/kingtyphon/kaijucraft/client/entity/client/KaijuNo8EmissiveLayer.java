package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class KaijuNo8EmissiveLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    private static final ResourceLocation EMISSIVE_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8_glowmask.png");

    public KaijuNo8EmissiveLayer(GeoRenderer<T> entityRenderer) {
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
