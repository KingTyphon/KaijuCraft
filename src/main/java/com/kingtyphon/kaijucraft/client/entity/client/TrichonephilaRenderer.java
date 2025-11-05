package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.entity.kaiju.TrichonephilaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TrichonephilaRenderer extends GeoEntityRenderer<TrichonephilaEntity> {
    public TrichonephilaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TrichonephilaModel());
    }
    @Override
    public void render(TrichonephilaEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight){
        pMatrixStack.scale(5F,5F,5F);

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

}
