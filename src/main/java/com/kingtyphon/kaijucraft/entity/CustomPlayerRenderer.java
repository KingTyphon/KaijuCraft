package com.kingtyphon.kaijucraft.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CustomPlayerRenderer extends GeoEntityRenderer<CustomPlayerWrapper> {
    public CustomPlayerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CustomPlayerModel());
    }

    protected float getDeathMaxRotation(Player player) {
        return 0.0F; // Prevents rotation when the player "dies."
    }
}
