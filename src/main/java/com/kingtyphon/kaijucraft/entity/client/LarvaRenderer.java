package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class LarvaRenderer extends GeoEntityRenderer<LarvaEntity> {
    public LarvaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LarvaModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
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
}
