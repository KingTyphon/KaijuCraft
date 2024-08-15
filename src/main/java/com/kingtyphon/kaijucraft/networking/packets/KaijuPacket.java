package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class KaijuPacket {
    private int xp;
    private int maxXp;
    private float maxEnergy;
    private float energy;
    private int level;

    private CompoundTag nbtData;

    public KaijuPacket() {

    }
    public KaijuPacket(CompoundTag nbtData) {
        this.nbtData = nbtData;
    }
    // Constructor to initialize with capability
    public KaijuPacket(IKaijuCapability capability) {
        this.level = capability.getLevel();
        this.energy = capability.getEnergy();
        this.maxEnergy = capability.getMaxEnergy();
        this.xp = capability.getXP();
        this.maxXp = capability.getMaxXp();

    }

    // Deserializing from buffer
    public KaijuPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt();
        this.energy = buf.readFloat();
        this.maxEnergy = buf.readFloat();
        this.xp = buf.readInt();
        this.maxXp = buf.readInt();
        this.nbtData = buf.readNbt();
    }

    // Serializing to buffer
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeFloat(this.energy);
        buf.writeFloat(this.maxEnergy);
        buf.writeInt(this.xp);
        buf.writeInt(this.maxXp);
        buf.writeNbt(this.nbtData);
    }

    // Static method to handle the packet when received
    public static void handle(KaijuPacket packet, Supplier<NetworkEvent.Context> ctx) {

        ctx.get().enqueueWork(() -> {

            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                IKaijuCapability cap = player.getCapability(KaijuProvider.KAIJU_CAPABILITY).orElseThrow(() -> new IllegalStateException("Slayer Capability not found!"));
                    cap.deserializeNBT(packet.nbtData);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
