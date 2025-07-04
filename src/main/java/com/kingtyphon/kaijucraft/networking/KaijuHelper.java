package com.kingtyphon.kaijucraft.networking;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.init.ItemInit;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class KaijuHelper {
    private int level;
    public static ServerPlayer getPlayer(NetworkEvent.Context context){
        return context.getSender();
    }

    public static boolean isWearingSpecialArmor(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET); // Change slot if needed
        return !chest.isEmpty() && chest.getItem() == ItemInit.MCOMBAT_CHESTPLATE.get() && !legs.isEmpty() && legs.getItem() == ItemInit.MCOMBAT_LEGGINGS.get() && !feet.isEmpty() && feet.getItem() == ItemInit.MCOMBAT_BOOTS.get();
    }
    public static boolean isHigherLevel(Player higherLevelPlayer, Player lowerLevelPlayer){
        AtomicInteger higherLevel = new AtomicInteger();
        AtomicInteger lowerLevel= new AtomicInteger();
        higherLevelPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY,null).ifPresent(cap -> {
            higherLevel.set(cap.getLevel());
        });
        lowerLevelPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY,null).ifPresent(cap -> {
            lowerLevel.set(cap.getLevel());
        });
        return higherLevel.get() > lowerLevel.get();
    }
    public static boolean hasGlockInHand(Player player) {
        return player.getMainHandItem().is(ItemInit.GLOCK17GEN4.get());
    }
}
