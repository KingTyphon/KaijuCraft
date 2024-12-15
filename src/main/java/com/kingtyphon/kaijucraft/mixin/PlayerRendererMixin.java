package com.kingtyphon.kaijucraft.mixin;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRenderPlayer(Player player, float entityYaw, float partialTicks, CallbackInfo ci) {
        // Check if the player is transformed

        player.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(kapability ->{
        boolean isTransformed = kapability.isTransformed();
        if (isTransformed) {
                ci.cancel();
            }});


    }
}