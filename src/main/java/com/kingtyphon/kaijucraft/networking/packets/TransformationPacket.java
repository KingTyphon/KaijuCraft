package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TransformationPacket {
    public TransformationPacket() {

    }

    public TransformationPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }


    public boolean handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            var player = context.get().getSender();
            if (player != null) {
                player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(cap -> {
                    cap.setTransformed(!cap.isTransformed());
                    ModMessages.send(new KaijuPacket(cap), (ServerPlayer) player);
                });

            }


        });
        return true;
    }
}
