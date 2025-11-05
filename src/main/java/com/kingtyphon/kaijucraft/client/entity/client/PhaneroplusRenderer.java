package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.kingtyphon.kaijucraft.entity.kaiju.PhaneroplusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PhaneroplusRenderer extends GeoEntityRenderer<PhaneroplusEntity> {
    public PhaneroplusRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PhaneroplusModel());
    }
    @Override
    public void render(PhaneroplusEntity entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        float scale = entity.getEntityScale();
        poseStack.scale(scale, scale, scale);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
