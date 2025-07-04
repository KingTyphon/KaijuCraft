package com.kingtyphon.kaijucraft.entity.kaiju;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KaijuPartEntity extends Entity {

    public KaijuPartEntity(EntityType<? extends KaijuPartEntity> entityType, Level level) {
        super(entityType, level);

    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(2.5F, 1F); // Width 2 blocks, Height 1.5 blocks
    }
    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.refreshDimensions(); // Updates hitbox after spawning
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public void tick() {
        super.tick();
        // Ensure the entity does not move
        this.setDeltaMovement(Vec3.ZERO);
    }


    @Override
    public boolean canBeCollidedWith() {
        return true;
    }
}
