package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.capabilities.DNCTPressedProcedure;
import com.kingtyphon.kaijucraft.capabilities.DNCTReleasedProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DNCTPacket{
    int type;
    int pressedms;

    public DNCTPacket(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public DNCTPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void toBytes(DNCTPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(DNCTPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = (NetworkEvent.Context)contextSupplier.get();
        context.enqueueWork(() -> {
            pressAction(context.getSender(), message.type, message.pressedms);
        });
        context.setPacketHandled(true);
    }
    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (!world.isClientSide()) { // Ensure it runs on the server
            if (type == 0) {
                DNCTPressedProcedure.execute(entity);
            } else if (type == 1) {
                DNCTReleasedProcedure.execute(entity);
            }
        }
    }
}
