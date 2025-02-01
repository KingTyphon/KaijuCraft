package com.kingtyphon.kaijucraft.keybinds;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.AutoSprintPacket;
import com.kingtyphon.kaijucraft.networking.packets.DNCTPacket;
import com.kingtyphon.kaijucraft.networking.packets.DashMessage;
import com.kingtyphon.kaijucraft.networking.packets.MobilityKeybindPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.settings.KeyConflictContext;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class KaijuKeybinds {
    public static final KaijuKeybinds INSTANCE = new KaijuKeybinds();

    private KaijuKeybinds() {}

    private static final String CATEGORY = "key.categories." + KaijuCraft.MODID;

    // Existing Kaiju Keybinds
    public final KeyMapping kaijuGui = new KeyMapping(
            "key." + KaijuCraft.MODID + ".gui_key",
            KeyConflictContext.GUI,
            InputConstants.getKey(InputConstants.KEY_F, 1),
            CATEGORY
    );

    public final KeyMapping transform = new KeyMapping(
            "key." + KaijuCraft.MODID + ".transform_key",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_P, 2),
            CATEGORY
    );

    // New Wallrunner Keybinds Integrated
    public final KeyMapping mobilityKeybind = new KeyMapping(
            "key.kaijucraft.mobility_keybind",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_SPACE, 3),
            CATEGORY
    );

    public final KeyMapping dash = new KeyMapping(
            "key.kaijucraft.dash",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_C, 4),
            CATEGORY
    );

    public final KeyMapping autoSprint = new KeyMapping(
            "key.kaijucraft.auto_sprint",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_LCONTROL, 5),
            CATEGORY
    );

    public final KeyMapping donotchangethis = new KeyMapping(
            "key.kaijucraft.donotchangethis",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_W, 6),
            CATEGORY
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(INSTANCE.kaijuGui);
        event.register(INSTANCE.transform);
        event.register(INSTANCE.mobilityKeybind);
        event.register(INSTANCE.dash);
        event.register(INSTANCE.autoSprint);
        event.register(INSTANCE.donotchangethis);
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class KeyEventListener {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                if (INSTANCE.mobilityKeybind.isDown()) {
                    ModMessages.sendToServer(new MobilityKeybindPacket(0, 0));
                    MobilityKeybindPacket.pressAction(mc.player, 0, 0);
                }

                if (INSTANCE.dash.isDown()) {
                    ModMessages.sendToServer(new DashMessage(0, 0));
                    DashMessage.pressAction(mc.player, 0, 0);
                }

                if (INSTANCE.autoSprint.isDown()) {
                    ModMessages.sendToServer(new AutoSprintPacket(0, 0));
                    AutoSprintPacket.pressAction(mc.player, 0, 0);
                }

                if (INSTANCE.donotchangethis.isDown()) {
                    ModMessages.sendToServer(new DNCTPacket(0, 0));
                    DNCTPacket.pressAction(mc.player, 0, 0);
                }
            }
        }
    }
}