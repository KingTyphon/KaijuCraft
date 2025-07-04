package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AmmoChangePacket {

    public AmmoChangePacket() {}

    public AmmoChangePacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(AmmoChangePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            ItemStack mainHandItem = player.getMainHandItem();
            if (mainHandItem.getItem() instanceof Glock17Gen4) {
                Glock17Gen4 weapon = (Glock17Gen4) mainHandItem.getItem();
                weapon.changeBulletEvent(player);
                weapon.reload(player, mainHandItem);
            }
            if (mainHandItem.getItem() instanceof SigSauerShortRifleItem) {
                SigSauerShortRifleItem weapon = (SigSauerShortRifleItem) mainHandItem.getItem();
                weapon.reload(player, mainHandItem);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
