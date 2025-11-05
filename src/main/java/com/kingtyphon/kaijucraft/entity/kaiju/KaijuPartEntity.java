package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class KaijuPartEntity extends Entity {
    private static final EntityDataAccessor<Integer> MEAT_POINTS =
            SynchedEntityData.defineId(KaijuPartEntity.class, EntityDataSerializers.INT);

    private static final int MAX_MEAT_POINTS = 50;

    public KaijuPartEntity(EntityType<? extends KaijuPartEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        // Use the built-in random from Entity, not your own field
        this.entityData.define(MEAT_POINTS, this.random.nextInt(MAX_MEAT_POINTS) + 1);
    }

    public int getMeatPoints() {
        return this.entityData.get(MEAT_POINTS);
    }

    public void setMeatPoints(int points) {
        this.entityData.set(MEAT_POINTS, points);
    }

    public void reduceHitPoints(Player player) {
        int current = getMeatPoints();
        setMeatPoints(current - 1);

        int chance = this.random.nextInt(100) + 1;
        if (chance < 35) {
            player.addItem(new ItemStack(ItemInit.KAIJU_MUSCLE.get()));
        } else {
            player.addItem(new ItemStack(ItemInit.ROTTEN_KAIJU_MUSCLE.get()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (getMeatPoints() <= 0) {
            this.kill();
        }
        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return EntityDimensions.scalable(2.5F, 1F);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.refreshDimensions();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("MeatPoints", getMeatPoints());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("MeatPoints")) {
            setMeatPoints(compound.getInt("MeatPoints"));
        }
    }
}
