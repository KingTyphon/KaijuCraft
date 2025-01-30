package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ChainsawPacket {
    private final boolean isOn;
    private final double x, y, z;

    public ChainsawPacket(boolean isOn, double x, double y, double z) {
        this.isOn = isOn;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ChainsawPacket(FriendlyByteBuf buf) {
        this.isOn = buf.readBoolean();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isOn);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    if (isOn) {
                        player.level().playLocalSound(x, y, z, KaijuSounds.CHAINSAW_ON.get(),
                                SoundSource.PLAYERS, 1.0F, 1.0F, false);
                    }
                }
            });
        });
        context.get().setPacketHandled(true);
    }
}