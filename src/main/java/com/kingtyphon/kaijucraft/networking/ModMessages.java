package com.kingtyphon.kaijucraft.networking;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.networking.packets.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int pocketId = 0;
     private static int id(){
         return pocketId++;
     }
    public static void register(){
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(KaijuCraft.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions($ -> true)
                .serverAcceptedVersions($ -> true)
                .simpleChannel();

        INSTANCE = net;
        net.messageBuilder(KaijuPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(KaijuPacket::new)
                .encoder(KaijuPacket::toBytes)
                .consumerMainThread(KaijuPacket::handle)
                .add();
        net.messageBuilder(DashMessage.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DashMessage::new)
                .encoder(DashMessage::toBytes)
                .consumerMainThread(DashMessage::handler)
                .add();
        net.messageBuilder(MobilityKeybindPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MobilityKeybindPacket::new)
                .encoder(MobilityKeybindPacket::toBytes)
                .consumerMainThread(MobilityKeybindPacket::handler)
                .add();
        net.messageBuilder(AutoSprintPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(AutoSprintPacket::new)
                .encoder(AutoSprintPacket::toBytes)
                .consumerMainThread(AutoSprintPacket::handler)
                .add();
        net.messageBuilder(DNCTPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DNCTPacket::new)
                .encoder(DNCTPacket::toBytes)
                .consumerMainThread(DNCTPacket::handler)
                .add();
        // You can continue registering other packets like this


    }

    public static <MSG> void sendToServer(MSG message) {
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void send(MSG message, ServerPlayer player){
         INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }
}
