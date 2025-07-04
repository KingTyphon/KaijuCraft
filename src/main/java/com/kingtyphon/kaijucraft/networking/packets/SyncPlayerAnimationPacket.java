package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.networking.ModMessages;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import java.util.UUID;
import java.util.function.Supplier;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

public class SyncPlayerAnimationPacket {
    private final UUID playerUUID;
    private final String animationName;
    private final String animationType;

    public SyncPlayerAnimationPacket(UUID playerUUID, String animationName, String animationType) {
        this.playerUUID = playerUUID;
        this.animationName = animationName;
        this.animationType = animationType;
    }

    public SyncPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.animationName = buf.readUtf();
        this.animationType = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeUtf(animationName);
        buf.writeUtf(animationType);
    }

    public static void handle(SyncPlayerAnimationPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                // Server-side: Broadcast packet to all clients
                ServerPlayer sender = ctx.get().getSender();
                if (sender == null) return;
                System.out.println("Server: Broadcasting animation to all clients: " + message.animationName);

                ModMessages.sendToAll(new SyncPlayerAnimationPacket(message.playerUUID, message.animationName, message.animationType));
            } else if (ctx.get().getDirection().getReceptionSide().isClient()) {
                // Client-side: Apply animation
                Minecraft minecraft = Minecraft.getInstance();
                Player player = minecraft.level.getPlayerByUUID(message.playerUUID);
                if (player instanceof AbstractClientPlayer clientPlayer) {
                    System.out.println("Client: Applying animation: " + message.animationName + " Type: " + message.animationType);

                    applyAnimation(clientPlayer, message.animationName, message.animationType);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void applyAnimation(AbstractClientPlayer player, String animationName, String animationType) {

        var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                .getPlayerAssociatedData(player)
                .get(new ResourceLocation(MODID, animationType));

        if (animation != null) {
            if(animationName.isEmpty()){
                animation.setAnimation(null);
            } else if (animationName.equals("idle_ts")) {

                    animation.setAnimation(new KeyframeAnimationPlayer(
                            PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))
                    ));
            }else{
                    animation.setAnimation(new KeyframeAnimationPlayer(

                    PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true)));

            }
    }
}
}
