package com.kingtyphon.kaijucraft.networking.packets;

import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.lowdragmc.photon.client.fx.IFXEffect;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;
public class ParticleEffectPacket {
    private final Vec3 position;
    private final int entityId; // Store entity ID instead of entity object

    public ParticleEffectPacket(Vec3 position, Entity entity) {
        this.position = position;
        this.entityId = entity.getId(); // Store the entity's ID
    }

    public ParticleEffectPacket(FriendlyByteBuf buf) {
        this.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.entityId = buf.readInt(); // Read entity ID
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeDouble(position.x);
        buf.writeDouble(position.y);
        buf.writeDouble(position.z);
        buf.writeInt(entityId); // Send entity ID
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(entityId); // Retrieve entity from ID
                if (entity != null) {
                    FX fx = FXHelper.getFX(new ResourceLocation("photon:gunshot"));
                    IFXEffect effect = new EntityEffect(fx, level, entity); // Apply effect to entity
                    effect.setAllowMulti(true);
                    effect.start();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}