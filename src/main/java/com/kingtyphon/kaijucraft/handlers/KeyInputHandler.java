package com.kingtyphon.kaijucraft.handlers;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.gui.KaijuGui;
import com.kingtyphon.kaijucraft.item.armor.CombatArmorItem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
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
        if (Minecraft.getInstance().player == null) {
            return;
        }
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.getKey() == KaijuKeybinds.INSTANCE.kaijuGui.getKey().getValue() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof CombatArmorItem && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof CombatArmorItem &&
                player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof CombatArmorItem) {
            Minecraft.getInstance().setScreen(new KaijuGui());
        }
        if (event.getKey() == KaijuKeybinds.INSTANCE.transform.getKey().getValue()) {
            Minecraft.getInstance().player.getCapability(KaijuProvider.KAIJU_CAPABILITY)
                    .ifPresent(cap -> cap.setTransformed(!cap.isTransformed()));
        }
    }
}
