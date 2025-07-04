package com.kingtyphon.kaijucraft.event;


import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.armor.*;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;


public class SlingRenderLayer extends RenderLayer<Player, EntityModel<Player>> {
    private final GeoArmorRenderer<GunSlingItem> renderer;
    private final GeoArmorRenderer<GunInHolsterItem> rendererGun;
    private final HumanoidModel<Player> model;
    ItemStack gunSlingItemStack = new ItemStack(ItemInit.GUNSLING.get());
    ItemStack gunItemStack = new ItemStack(ItemInit.GUNINHOLSTER.get());


    public SlingRenderLayer(RenderLayerParent<Player, EntityModel<Player>> entityRenderer) {
        super(entityRenderer);
        if (entityRenderer.getModel() instanceof HumanoidModel<Player> humanoidModel) {
            this.model = humanoidModel;
        } else {
            throw new IllegalArgumentException("Parent model is not a HumanoidModel");
        }
        this.renderer = new GunSlingRenderer();
        this.rendererGun = new GunInHolsterRenderer();

    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (shouldRenderHolster(pLivingEntity)) {
            pPoseStack.pushPose();
            ResourceLocation texture = new ResourceLocation(KaijuCraft.MODID, "textures/item/sigrifleonback.png");
            RenderType renderType = renderer.getRenderType(renderer.getAnimatable(), texture, pBuffer, pPartialTick);
            VertexConsumer buffer = pBuffer.getBuffer(renderType);
            renderer.prepForRender(pLivingEntity,gunSlingItemStack, EquipmentSlot.CHEST, model);
            renderer.renderToBuffer(pPoseStack, buffer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            pPoseStack.popPose();
        }
        if (shouldRenderGun(pLivingEntity) && isWearingSpecialArmor(pLivingEntity)) {
            pPoseStack.pushPose();
            ResourceLocation texture = new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17.png");
            RenderType renderType = rendererGun.getRenderType(rendererGun.getAnimatable(), texture, pBuffer, pPartialTick);
            VertexConsumer buffer = pBuffer.getBuffer(renderType);
            rendererGun.prepForRender(pLivingEntity,gunItemStack, EquipmentSlot.CHEST, model);
            rendererGun.renderToBuffer(pPoseStack, buffer, pPackedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            pPoseStack.popPose();
        }
    }
    public static boolean isWearingSpecialArmor(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET); // Change slot if needed
        return !chest.isEmpty() && chest.getItem() == ItemInit.MCOMBAT_CHESTPLATE.get() && !legs.isEmpty() && legs.getItem() == ItemInit.MCOMBAT_LEGGINGS.get() && !feet.isEmpty() && feet.getItem() == ItemInit.MCOMBAT_BOOTS.get();
    }
    private boolean shouldRenderGun(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == ItemInit.GLOCK17GEN4.get() && !(player.getMainHandItem().getItem() == ItemInit.GLOCK17GEN4.get())) {
                return true;
            }
        }
        return false;
    }
    private boolean shouldRenderHolster(Player player) {
        for (ItemStack itemStack : player.getInventory().items) {
            if (itemStack.getItem() == ItemInit.SIGSAUERSHORTRIFLE.get() && !(player.getMainHandItem().getItem() == ItemInit.SIGSAUERSHORTRIFLE.get())) {
                return true;
            }
        }
        return false;
    }
}