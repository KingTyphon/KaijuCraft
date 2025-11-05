package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.PrimigeniusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PrimigeniusRenderer extends MobRenderer<PrimigeniusEntity, PrimigeniusModel<PrimigeniusEntity>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/primigenius.png");
    private static final ResourceLocation SHINY_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/primigeniusalt.png");

//    private final PrimigeniusModel model;

    public PrimigeniusRenderer(EntityRendererProvider.Context context) {
        super(context, new PrimigeniusModel<>(context.bakeLayer(ModelLayers.PRIMIGENIUS)), .7f);
        this.model = new PrimigeniusModel(context.bakeLayer(ModelLayers.PRIMIGENIUS));
    }

    @Override
    public ResourceLocation getTextureLocation(PrimigeniusEntity pEntity) {
       return pEntity.isShiny() ? SHINY_TEXTURE : TEXTURE;

    }



    @Override
    public void render(PrimigeniusEntity entity, float yaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {

        matrixStack.scale(1.5F,1.5F,1.5F);
        super.render(entity, yaw, partialTicks, matrixStack, buffer, packedLight);
    }

}