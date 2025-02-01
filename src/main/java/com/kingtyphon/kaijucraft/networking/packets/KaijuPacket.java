package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
public class KaijuPacket {
    private int xp;
    private int maxXp;
    private float maxEnergy;
    private float energy;
    private int level;
    private int SP;
    private int mind;
    private int range;
    private int melee;
    private boolean percentageRandomizerFlag;


    private CompoundTag nbtData;

    public KaijuPacket() {}

    public KaijuPacket(CompoundTag nbtData) {
        this.nbtData = nbtData;
    }

    public KaijuPacket(IKaijuCapability capability) {
        this.level = capability.getLevel();
        this.energy = capability.getEnergy();
        this.maxEnergy = capability.getMaxEnergy();
        this.xp = capability.getXP();
        this.maxXp = capability.getMaxXp();
        this.nbtData = capability.serializeNBT();
        this.SP = capability.getSP();
        this.mind = capability.getMind();
        this.range = capability.getRange();
        this.melee = capability.getMelee();
        this.percentageRandomizerFlag = capability.percentageRandomizerCheck();
    }

    public KaijuPacket(FriendlyByteBuf buf) {
        this.level = buf.readInt();
        this.energy = buf.readFloat();
        this.maxEnergy = buf.readFloat();
        this.xp = buf.readInt();
        this.maxXp = buf.readInt();
        this.nbtData = buf.readNbt();
        this.SP = buf.readInt();
        this.mind = buf.readInt();
        this.range = buf.readInt();
        this.melee = buf.readInt();
        this.percentageRandomizerFlag = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeFloat(this.energy);
        buf.writeFloat(this.maxEnergy);
        buf.writeInt(this.xp);
        buf.writeInt(this.maxXp);
        buf.writeNbt(this.nbtData);
        buf.writeInt(this.SP);
        buf.writeInt(this.mind);
        buf.writeInt(this.range);
        buf.writeInt(this.melee);
        buf.writeBoolean(this.percentageRandomizerFlag);
    }

    public static void handle(KaijuPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            NetworkEvent.Context context = ctx.get();

            // Server-side handling
            if (context.getDirection().getReceptionSide().isServer()) {
                ServerPlayer serverPlayer = context.getSender();
                if (serverPlayer != null) {
                    IKaijuCapability cap = serverPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).orElseThrow(() -> new IllegalStateException("Kaiju Capability not found!"));
                    cap.deserializeNBT(packet.nbtData);
                }
            }
            // Client-side handling
            else if (context.getDirection().getReceptionSide().isClient()) {
                Player clientPlayer = Minecraft.getInstance().player;
                if (clientPlayer != null) {
                    IKaijuCapability cap = clientPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).orElseThrow(() -> new IllegalStateException("Kaiju Capability not found!"));
                    cap.deserializeNBT(packet.nbtData);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}