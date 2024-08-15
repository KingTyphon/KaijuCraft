package com.kingtyphon.kaijucraft.handlers;

import com.kingtyphon.kaijucraft.gui.KaijuGui;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import com.kingtyphon.kaijucraft.keybinds.KaijuKeybinds;

@Mod.EventBusSubscriber(modid = "kaijucraft", value = Dist.CLIENT)
public class KeyInputHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (event.getKey() == KaijuKeybinds.INSTANCE.kaijuGui.getKey().getValue()) {
            Minecraft.getInstance().setScreen(new KaijuGui());
        }
    }
}
