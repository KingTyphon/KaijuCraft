package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Kaiju extends Animal {
    protected Kaiju(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource); // Always call super

        if (!this.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            // Replace with your desired entity type
            EntityType<?> entityTypeToSpawn = EntityInit.KAIJU_PART.get();

            Entity newEntity = entityTypeToSpawn.create(serverLevel);
            if (newEntity != null) {
                newEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                serverLevel.addFreshEntity(newEntity);
            }
        }
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
}
