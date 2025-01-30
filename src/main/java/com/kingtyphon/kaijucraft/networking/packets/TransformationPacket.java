package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.init.EntityInit;
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

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            //Kaiju_no8Entity entity = EntityInit.KAIJU_NO8.get().create(level);

        });
        return true;
    }
}