package com.kingtyphon.kaijucraft.entity.kaiju;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KaijuPartEntity extends Entity {

    public KaijuPartEntity(EntityType<? extends KaijuPartEntity> entityType, Level level) {
        super(entityType, level);
        this.setBoundingBox(new AABB(
                 -18, -22,  -25.0,  // Min X, Y, Z
                 18, 22, 25.0  // Max X, Y, Z
        ));
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
