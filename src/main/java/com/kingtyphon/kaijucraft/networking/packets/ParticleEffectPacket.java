package com.kingtyphon.kaijucraft.networking.packets;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.lowdragmc.photon.client.fx.IFXEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ParticleEffectPacket {
    private final Vec3 position;

    public ParticleEffectPacket(Vec3 position) {
        this.position = position;
    }

    public ParticleEffectPacket(FriendlyByteBuf buf) {
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                FX fx = FXHelper.getFX(new ResourceLocation("photon:gunshot"));
                IFXEffect effect = new EntityEffect(fx, level, null); // No entity, just positional effect
                effect.setOffset(position.x, position.y, position.z);
                effect.start();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}