package com.kingtyphon.kaijucraft.networking;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
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

    }

    public static <MSG> void sendToServer(MSG message){
         INSTANCE.sendToServer(message);
    }

    public static <MSG> void send(MSG message, ServerPlayer player){
         INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }
}
