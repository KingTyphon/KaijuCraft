package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class OrganWeakSpotEntity extends Entity {
    // not final so we can reassign after world load
    private PhaneroplusEntity parent;
    private UUID parentUUID;
    private float health = 25.0F;

    // Constructor used by Minecraft (deserialization / entity system)
    public OrganWeakSpotEntity(EntityType<? extends OrganWeakSpotEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
        this.setCustomName(Component.literal("WEAKSPOT"));
        this.setCustomNameVisible(true);
    }

    // Convenience constructor used when attaching to a parent
    public OrganWeakSpotEntity(PhaneroplusEntity parent, Level level) {
        // NOTE: use the EntityType directly (no .get()) because your EntityInit.ORGAN_WEAK_SPOT is an EntityType
        super(EntityInit.ORGAN_WEAK_SPOT.get(), level);
        this.parent = parent;
        this.parentUUID = parent.getUUID();
        this.noPhysics = true;
        this.setInvisible(true);
        // initial position
        this.setPos(parent.getX(), parent.getY() + 1.5D, parent.getZ());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide) {
            health -= amount;
            if (health <= 0) {
                if (parent != null) {
                    parent.onOrganBurst(source, amount);
                }
                this.discard();
            }
        }
        return true;
    }
    @Override
    public void tick() {
        super.tick();

        // server only: keep the weakspot attached, or try to resolve parent by UUID
        if (!this.level().isClientSide) {
            this.setGlowingTag(true); // forces a glowing outline

            if (parent == null || !parent.isAlive()) {
                if (parentUUID != null) {
                    // try to find parent nearby by class + UUID (safer than relying on ServerLevel#getEntityUUID)
                    AABB searchBox = this.getBoundingBox().inflate(100.0D); // adjust radius as needed
                    List<PhaneroplusEntity> found = this.level().getEntitiesOfClass(
                            PhaneroplusEntity.class,
                            searchBox,
                            e -> e.getUUID().equals(parentUUID)
                    );

                    if (!found.isEmpty()) {
                        parent = found.get(0);
                    } else {
                        // parent cannot be found -> discard weakspot
                        this.discard();
                        return;
                    }
                } else {
                    this.discard();
                    return;
                }
            }

            // follow the parent
            this.setPos(parent.getX(), parent.getY() + 1.5D, parent.getZ());
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("ParentUUID")) {
            parentUUID = tag.getUUID("ParentUUID");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (parentUUID != null) {
            tag.putUUID("ParentUUID", parentUUID);
        }
    }


    @Override
    public boolean isPickable() {
        return true; // must be true for F3+B
    }

    @Override
    public boolean isPushable() {
        return false; // don’t bump into it
    }

}
