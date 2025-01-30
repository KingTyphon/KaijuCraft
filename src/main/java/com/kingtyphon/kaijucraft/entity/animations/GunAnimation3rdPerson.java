package com.kingtyphon.kaijucraft.entity.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

public class GunAnimation3rdPerson extends BlockEntityWithoutLevelRenderer {

    public GunAnimation3rdPerson(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }
        public void renderByItem(ItemStack itemStack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            poseStack.pushPose();

            // Move the item to the middle of the screen (lower half)
            poseStack.translate(0.0, -0.4, 0.5); // Adjust translation
            poseStack.scale(1.2F, 1.2F, 1.2F);  // Scale the item

            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, context, light, overlay, poseStack, bufferSource, Minecraft.getInstance().level, 0);
            poseStack.popPose();
        } else if (context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            poseStack.pushPose();

            // Mimic bow-like pose in third-person
            poseStack.translate(0.0, 0.1, 0.2); // Adjust third-person position
            poseStack.mulPose(Axis.XP.rotationDegrees(-20.0F)); // Rotate slightly downward

            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, context, light, overlay, poseStack, bufferSource, Minecraft.getInstance().level, 0);
            poseStack.popPose();
        }
    }
}
