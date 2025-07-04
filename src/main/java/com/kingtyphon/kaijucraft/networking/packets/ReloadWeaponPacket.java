package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ReloadWeaponPacket {
    public ReloadWeaponPacket() {}

    public ReloadWeaponPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(ReloadWeaponPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof Glock17Gen4) {
                Glock17Gen4 weapon = (Glock17Gen4) mainHandItem.getItem();

                // ✅ Call reload function in weapon class
                weapon.reload(player, mainHandItem);
                weapon.setReloading(true);
            }
            if (mainHandItem.getItem() instanceof SigSauerShortRifleItem) {
                SigSauerShortRifleItem weapon = (SigSauerShortRifleItem) mainHandItem.getItem();

                // ✅ Call reload function in weapon class
                weapon.reload(player, mainHandItem);
                weapon.setReloading(true);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}