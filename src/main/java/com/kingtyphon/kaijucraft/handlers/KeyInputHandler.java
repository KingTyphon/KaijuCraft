package com.kingtyphon.kaijucraft.handlers;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.gui.KaijuGui;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.armor.CombatArmorItem;
import com.kingtyphon.kaijucraft.item.armor.GasMaskItem;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.AmmoChangePacket;
import com.kingtyphon.kaijucraft.networking.packets.GasMaskTogglePacket;
import com.kingtyphon.kaijucraft.networking.packets.ReloadWeaponPacket;
import com.kingtyphon.kaijucraft.networking.packets.SyncPlayerAnimationPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
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
            player.getCapability(KaijuProvider.KAIJU_CAPABILITY)
                    .ifPresent(cap -> cap.setTransformed(!cap.isTransformed()));
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
//                ItemStack headSlotItem = player.getInventory().armor.get(3); // Slot 3 is the head slot
//                ItemStack gasMaskItem = new ItemStack(ItemInit.GAS_MASK.get()); // Replace with your actual item
//
//                if (headSlotItem.is(gasMaskItem.getItem())) {
//                    // Try to move the gas mask back to the inventory
//                    if (player.getInventory().add(headSlotItem)) {
//                        // Successfully moved to inventory, now clear the head slot
//                        playAnimation(player, "equip_mask");
//                        player.getInventory().armor.set(3, ItemStack.EMPTY);
//                        player.inventoryMenu.broadcastChanges(); // Sync inventory
//                    }}else{
//                int slotIndex = -1;
//                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
//                    ItemStack stack = player.getInventory().getItem(i);
//                    if (stack.is(gasMaskItem.getItem())) {
//                        slotIndex = i;
//                        break;
//                    }
//                }
//                if (slotIndex != -1) {
//                    // Remove the item from inventory
//                    ItemStack removedItem = player.getInventory().removeItem(slotIndex, 1);
//
//                    // Move it to the player's head slot
//                    player.getInventory().armor.set(3, removedItem); // Slot 3 is the head slot
//                    playAnimation(player, "equip_mask");
//                    // Sync inventory changes
//                    player.inventoryMenu.broadcastChanges();
//                }
//                }
//                }
            }
        }
    }
}
