package com.kingtyphon.kaijucraft.handlers;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.client.gui.KaijuGui;
import com.kingtyphon.kaijucraft.item.armor.CombatArmorItem;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import com.kingtyphon.kaijucraft.keybinds.KaijuKeybinds;

@Mod.EventBusSubscriber(modid = "kaijucraft", value = Dist.CLIENT)
public class KeyInputHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static void sendAnimationToServer(Player player, String animationName, String animationType) {
        if (player instanceof AbstractClientPlayer) {
            ModMessages.sendToServer(new SyncPlayerAnimationPacket(player.getUUID(), animationName, animationType));
        }
    }
    private static void playAnimation(Player player, String animationName){
        if(player.level().isClientSide){
            sendAnimationToServer(player, animationName, "animation" );
        }
    }
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        if (Minecraft.getInstance().screen != null) return; // Ignore when GUI is open

        LocalPlayer player = Minecraft.getInstance().player;
        if (event.getAction() == GLFW.GLFW_PRESS) {
            ItemStack mainHandItem = player.getMainHandItem();
        if(event.getKey() == KaijuKeybinds.INSTANCE.reload.getKey().getValue() && (mainHandItem.getItem() instanceof Glock17Gen4 || mainHandItem.getItem() instanceof SigSauerShortRifleItem)){
            ModMessages.sendToServer(new ReloadWeaponPacket());
        }
        if (event.getKey() == KaijuKeybinds.INSTANCE.kaijuGui.getKey().getValue() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof CombatArmorItem && player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof CombatArmorItem &&
                player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof CombatArmorItem) {
            Minecraft.getInstance().setScreen(new KaijuGui());
        }
            if (event.getKey() == KaijuKeybinds.INSTANCE.transform.getKey().getValue()) {
                ModMessages.sendToServer(new TransformationPacket());
            }
        if(event.getKey() == KaijuKeybinds.INSTANCE.emote.getKey().getValue()){

        }
        if(event.getKey() == KaijuKeybinds.INSTANCE.ammoChange.getKey().getValue()){
            ModMessages.sendToServer(new AmmoChangePacket());
            if(KaijuHelper.hasGlockInHand(player)){
                Glock17Gen4 gunInHand = (Glock17Gen4) player.getMainHandItem().getItem();
                gunInHand.reload(player, gunInHand.getDefaultInstance());
                gunInHand.setReloading(true);
            }
        }
        }
        if(event.getKey() == KaijuKeybinds.INSTANCE.gasmask.getKey().getValue()){
            ModMessages.sendToServer(new GasMaskTogglePacket());
            if(KaijuHelper.isWearingSpecialArmor(player)){

            }
        }
    }
}
