package com.kingtyphon.kaijucraft.mixins;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderPlayer(Player player, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        System.out.println("PlayerRendererMixin is being applied!");
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(capability -> {

            if (capability.isTransformed()) {
                Minecraft mc = Minecraft.getInstance();

                // Ensure the world is available
                if (mc.level == null) return;

                EntityType<Kaiju_no8Entity> kaijuType = EntityInit.KAIJU_NO8.get();
                Kaiju_no8Entity kaijuNo8 = new Kaiju_no8Entity(kaijuType, mc.level);

                // Copy player position
                kaijuNo8.setPos(player.getX(), player.getY(), player.getZ());

                // Set rotation
                kaijuNo8.setYRot(player.getYRot());
                kaijuNo8.setXRot(player.getXRot());

                // Cancel player rendering
                ci.cancel();

                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();

                poseStack.pushPose();

                // Render Kaiju entity instead of the player
                dispatcher.render(kaijuNo8, player.getX(), player.getY(), player.getZ(), entityYaw, partialTicks, poseStack, bufferSource, packedLight);

                poseStack.popPose();
            }
        });
    }
}