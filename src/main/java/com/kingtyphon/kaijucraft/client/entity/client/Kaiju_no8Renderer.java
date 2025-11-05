package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Kaiju_no8Renderer extends GeoEntityRenderer<Kaiju_no8Entity> {
    private static final ResourceLocation KAIJU_NO8_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8.png");

    public Kaiju_no8Renderer(EntityRendererProvider.Context pContext) {
        super(pContext, new Kaiju_no8Model());
        addRenderLayer(new KaijuNo8EmissiveLayer<>(this));

    }

//    @Override
//    public ResourceLocation getTextureLocation(Kaiju_no8Entity kaijuNo8Entity) {
//        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8.png");
//    }
    @Override
    public void render(Kaiju_no8Entity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight){

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }}



