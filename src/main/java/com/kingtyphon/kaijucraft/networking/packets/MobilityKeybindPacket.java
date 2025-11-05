package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.common.capabilities.MobilityKeybindOnKeyPressedProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MobilityKeybindPacket {
    int type;
    int pressedms;

    public MobilityKeybindPacket(int type, int pressedms) {
        this.type = type;
        this.pressedms = pressedms;
    }

    public MobilityKeybindPacket(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void toBytes(MobilityKeybindPacket message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(MobilityKeybindPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
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

        if (world.hasChunkAt(entity.blockPosition())) {
            if (type == 0) {
                MobilityKeybindOnKeyPressedProcedure.execute(world, x, y, z, entity);
            }
        }
    }
}
