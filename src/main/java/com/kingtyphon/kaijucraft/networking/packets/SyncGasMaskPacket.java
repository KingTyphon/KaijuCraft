package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class SyncGasMaskPacket {
    private final int playerId;
    private final boolean removed;

    public SyncGasMaskPacket(int playerId, boolean removed) {
        this.playerId = playerId;
        this.removed = removed;
    }

    public SyncGasMaskPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readInt();
        this.removed = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(playerId);
        buf.writeBoolean(removed);
    }
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
    public static void handle(SyncGasMaskPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return;

            Player player = (Player) world.getEntity(msg.playerId);
            if (player == null) return;

            if (msg.removed) {
                player.getInventory().armor.set(3, ItemStack.EMPTY);
            } else {
                player.getInventory().armor.set(3, new ItemStack(ItemInit.GAS_MASK.get()));
            }

            // ✅ Play animation for the player
            playAnimation(player, "equip_mask");
        });
        ctx.get().setPacketHandled(true);
    }
}