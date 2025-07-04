package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class TrichonephilaEntity extends Animal implements GeoEntity {

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private int attackAnimationTimeout = 0;

    public TrichonephilaEntity(EntityType<? extends Animal> pEntityType, Level pLevel){
        super(pEntityType, pLevel);
    }
    private static final EntityDataAccessor<Boolean> IS_ROARING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROARING, false);
        this.entityData.define(ATTACKING, false);
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
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TrichonephilaEntity.AttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class,5f));
//        this.goalSelector.addGoal(3, new TrichonephilaEntity.RoarGoal(this));
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this));


    }
    private static class AttackGoal extends MeleeAttackGoal {
        private int attackDelay = 15;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private final TrichonephilaEntity entity;


        public AttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((TrichonephilaEntity) pMob);
        }

        private boolean isEnemyWithingAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr) {
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
            if (isEnemyWithingAttackDistance(pEnemy, pDistToEnemySqr)) {
                shouldCountTillNextAttack = true;
                if (isTimeToStartAttackAnimation()) {
                    entity.setAttacking(true);
                }
                if (isTimeToAttack()) {
                    this.mob.getLookControl().setLookAt(pEnemy.getX(), pEnemy.getEyeY(), pEnemy.getZ());
                    performAttack(pEnemy);
                }
            } else {
                resetAttackCooldown();
                shouldCountTillNextAttack = false;
                entity.setAttacking(false);
                entity.attackAnimationTimeout = 0;
            }
        }

        protected void performAttack(LivingEntity pEnemy) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay);
        }

        protected boolean isTimeToAttack() {
            return this.ticksUntilNextAttack <= 0;
        }

        protected boolean isTimeToStartAttackAnimation() {
            return this.ticksUntilNextAttack <= attackDelay;
        }

        protected int getTicksUntilNextAttack() {
            return this.ticksUntilNextAttack;
        }

        @Override
        public void tick() {
            super.tick();
            if (shouldCountTillNextAttack) {
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            }
        }

        @Override
        public void stop() {
            entity.setAttacking(false);
            super.stop();
        }
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    public void setAttacking(boolean attacking){
        this.entityData.set(ATTACKING, attacking);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isRoaring() {
        return this.entityData.get(IS_ROARING);
    }

    public void setRoaring(boolean roaring) {
        this.entityData.set(IS_ROARING, roaring);
    }
    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<T> tAnimationState) {
        AnimationController<T> controller = tAnimationState.getController();

        if (this.isRoaring()) {
//            controller.setAnimation(RawAnimation.begin().then("animation.trichonephila.roar", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        } else if (this.isAttacking()) {
//            controller.setAnimation(RawAnimation.begin().then("animation.trichonephila.attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }else if (this.isSprinting()) {
//            controller.setAnimation(RawAnimation.begin().then("animation.trichonephila.run", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }else if (tAnimationState.isMoving()) {
            controller.setAnimation(RawAnimation.begin().then("animation.trichonephila.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        controller.setAnimation(RawAnimation.begin().then("animation.trichonephila.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200D)
                .add(Attributes.ATTACK_DAMAGE, 20.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0f);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
}