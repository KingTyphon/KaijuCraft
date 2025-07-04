package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

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



