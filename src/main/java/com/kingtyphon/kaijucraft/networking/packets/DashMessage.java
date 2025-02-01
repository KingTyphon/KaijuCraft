package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.capabilities.DashOnKeyPressedProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DashMessage {
    int type;
    int pressedms;

    public DashMessage(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public DashMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void toBytes(DashMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(DashMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
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

        if (!world.isClientSide()) { // Ensure it's running on the server
            if (type == 0) {
                DashOnKeyPressedProcedure.execute(world, entity);
            }
        }
    }
}