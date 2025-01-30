package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.entity.animations.GunAnimation3rdPerson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;

public class CustomRenderRegistry {
    public static BlockEntityWithoutLevelRenderer GUN_ANIMATION_RENDERER;

    public static void init() {
        // Initialize the custom renderer with the necessary dependencies
        GUN_ANIMATION_RENDERER = new GunAnimation3rdPerson(
                Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                Minecraft.getInstance().getEntityModels()
        );
    }
}
