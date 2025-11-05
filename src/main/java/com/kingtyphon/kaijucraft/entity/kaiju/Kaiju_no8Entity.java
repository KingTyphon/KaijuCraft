package com.kingtyphon.kaijucraft.entity.kaiju;


import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Kaiju_no8Entity extends Animal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private RawAnimation currentAnimation = null;

    public Kaiju_no8Entity(EntityType<? extends Animal> pEntityType, Level plevel){
        super(pEntityType,plevel);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    private Player animationDriver;

    public void setAnimationDriver(Player player) {
        this.animationDriver = player;
    }

    public Player getAnimationDriver() {
        return this.animationDriver;
    }
    private <T extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<T> tAnimationState) {
        AnimationController<T> controller = tAnimationState.getController();
        RawAnimation nextAnim;

        if (animationDriver != null) {
            double speed = animationDriver.getDeltaMovement().horizontalDistance();
            if(animationDriver.swinging){
                nextAnim = RawAnimation.begin().then("animation.kaijuno8.attack", Animation.LoopType.PLAY_ONCE);
            }
            else if (speed > 0.1) {
                nextAnim = RawAnimation.begin().then("animation.kaijuno8.run", Animation.LoopType.LOOP);
            } else {
                nextAnim = RawAnimation.begin().then("animation.kaijuno8.idle", Animation.LoopType.LOOP);
            }
        } else if (this.isRoaring()) {
            nextAnim = RawAnimation.begin().then("animation.kaijuno8.roar", Animation.LoopType.PLAY_ONCE);
        } else if (this.isAttacking()) {
            nextAnim = RawAnimation.begin().then("animation.kaijuno8.attack", Animation.LoopType.PLAY_ONCE);
        } else if (this.isSprinting()) {
            nextAnim = RawAnimation.begin().then("animation.kaijuno8.run", Animation.LoopType.LOOP);
        }else if (tAnimationState.isMoving()) {
            controller.setAnimation(RawAnimation.begin().then("animation.kaijuno8.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        } else {
            nextAnim = RawAnimation.begin().then("animation.kaijuno8.idle", Animation.LoopType.LOOP);
        }

        // Only change the animation if it’s different
        if (currentAnimation != nextAnim) {
            controller.setAnimation(nextAnim);
            currentAnimation = nextAnim;
        }

        return PlayState.CONTINUE;
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();
    private int roarAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    private int idleAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> IS_ROARING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHARGING_PUNCH = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROARING, false);
        this.entityData.define(ATTACKING, false);
        this.entityData.define(OWNER, ""); // Default to empty string
        this.entityData.define(CHARGING_PUNCH, false);

    }
    public boolean isChargingPunch() {
        return this.entityData.get(CHARGING_PUNCH);
    }

    public void setChargingPunch(boolean charging) {
        this.entityData.set(CHARGING_PUNCH, charging);
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
    private static final EntityDataAccessor<String> OWNER = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.STRING);


    public void setOwner(Player player) {
        this.entityData.set(OWNER, player.getUUID().toString());
    }

    @Nullable
    public UUID getOwnerUUID() {
        String uuidString = this.entityData.get(OWNER);
        return uuidString.isEmpty() ? null : UUID.fromString(uuidString);
    }






    @Override
    public void tick() {
        super.tick();

        // Reset roar animation after some time
        if (this.isRoaring() && this.tickCount % 40 == 0) {
            this.setRoaring(false);
        }

        // Reset attack animation after some time
        if (this.isAttacking() && this.tickCount % 26 == 0) {
            this.setAttacking(false);
        }
    }



    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new ChargedPunchGoal(this)); // <-- Added here
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class,5f));
        this.goalSelector.addGoal(5, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(6, new RoarGoal(this));


    }

    public static AttributeSupplier.Builder createAttributes() {
            return Animal.createLivingAttributes()
                    .add(Attributes.MAX_HEALTH, 1000D)
                    .add(Attributes.ATTACK_DAMAGE, 60.0f)
                    .add(Attributes.ATTACK_SPEED, 1.0f)
                    .add(Attributes.MOVEMENT_SPEED, 0.7f)
                    .add(Attributes.FOLLOW_RANGE, 40.0D)
                    .add(Attributes.ARMOR,  30.0D)
                    .add(Attributes.ARMOR_TOUGHNESS,  20.0D)
                    .add(Attributes.ATTACK_KNOCKBACK, 5.0f);
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    public void roar() {
        if (!this.isRoaring()) {
            this.setRoaring(true);
        }
    }
    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return KaijuSounds.KAIJU_NO8_GRUNT1.get();
    }
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isRoaring()) {
            List<SoundEvent> roarSounds = List.of(
                    KaijuSounds.KAIJU_NO8_ROAR1.get(),
                    KaijuSounds.KAIJU_NO8_ROAR2.get()
            );
            SoundEvent selectedSound = roarSounds.get(ThreadLocalRandom.current().nextInt(roarSounds.size()));
            return selectedSound;

        }
        return null;
    }

    private void notifyNearbyPlayers() {
        this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(10)) // 10-block radius
                .forEach(player -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        player.setDeltaMovement(player.getDeltaMovement().with(Direction.Axis.Y, 0.4D));
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(serverPlayer));                    }
                });
    }
    @Override
    public void playAmbientSound() {

            super.playAmbientSound();

    }private static class RoarGoal extends Goal {
        private final Kaiju_no8Entity entity;

        public RoarGoal(Kaiju_no8Entity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return !this.entity.isAttacking() && this.entity.getRandom().nextInt(1000) == 0 && entity.roarAnimationTimeout <= 0;
        }

        @Override
        public void start() {
            this.entity.roar();
            List<SoundEvent> roarSounds = List.of(KaijuSounds.KAIJU_NO8_ROAR1.get(), KaijuSounds.KAIJU_NO8_ROAR2.get());
            SoundEvent selectedSound = roarSounds.get(ThreadLocalRandom.current().nextInt(roarSounds.size()));
            this.entity.playSound(selectedSound, 1.0f, 1.0f);
            this.entity.notifyNearbyPlayers();
        }
    }
    private static class AttackGoal extends MeleeAttackGoal {
        private int attackDelay = 15;
        private int ticksUntilNextAttack = 10;
        private boolean shouldCountTillNextAttack = false;
        private final Kaiju_no8Entity entity;


        public AttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((Kaiju_no8Entity) pMob);
        }
        private boolean isEnemyWithingAttackDistance(LivingEntity pEnemy, double pDistToEnemySqr){
            return pDistToEnemySqr <= this.getAttackReachSqr(pEnemy);
        }
        @Override
        protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr){
            if(isEnemyWithingAttackDistance(pEnemy, pDistToEnemySqr)){
                shouldCountTillNextAttack = true;
                if(isTimeToStartAttackAnimation()){
                    entity.setAttacking(true);
                }
                if(isTimeToAttack()){
                    this.mob.getLookControl().setLookAt(pEnemy.getX(),pEnemy.getEyeY(), pEnemy.getZ());
                    performAttack(pEnemy);
                }
            }else{
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
        protected void resetAttackCooldown(){this.ticksUntilNextAttack = this.adjustedTickDelay(attackDelay);}
        protected boolean isTimeToAttack(){return this.ticksUntilNextAttack <= 0;}
        protected boolean isTimeToStartAttackAnimation(){return this.ticksUntilNextAttack<= attackDelay;}
        protected int getTicksUntilNextAttack(){return this.ticksUntilNextAttack;}
        @Override
        public void tick() {
            super.tick();
            if(shouldCountTillNextAttack){
                this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack -1, 0);
            }
        }

        @Override
        public void stop() {
            entity.setAttacking(false);
            super.stop();
        }
    }
    private static class ChargedPunchGoal extends Goal {
        private final Kaiju_no8Entity entity;
        private int chargeTime;

        public ChargedPunchGoal(Kaiju_no8Entity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            // Small chance to use if near a target
            LivingEntity target = this.entity.getTarget();
            return target != null && this.entity.distanceToSqr(target) < 16 && this.entity.getRandom().nextInt(500) == 0;
        }

        @Override
        public void start() {
            chargeTime = 40; // 2 seconds charge
            entity.setChargingPunch(true);
            entity.getNavigation().stop(); // stop moving while charging
        }

        @Override
        public void tick() {
            if (chargeTime > 0) {
                chargeTime--;

                // Play charge sound midway
                if (chargeTime == 20) {
                    entity.playSound(KaijuSounds.KAIJU_NO8_ROAR1.get(), 2.0f, 0.8f);
                }

                // After charging -> punch
                if (chargeTime == 0) {
                    unleashPunch();
                }
            }
        }

        private void unleashPunch() {
            entity.setChargingPunch(false);
            LivingEntity target = entity.getTarget();
            if (target != null && entity.distanceToSqr(target) < 25) {
                // Massive damage
                target.hurt(entity.damageSources().mobAttack(entity), 150.0f);

                // Knockback effect
                double dx = target.getX() - entity.getX();
                double dz = target.getZ() - entity.getZ();
                target.knockback(5.0D, dx, dz);

                // Punch sound
                entity.playSound(KaijuSounds.KAIJU_NO8_GRUNT1.get(), 2.0f, 0.6f);
            }
        }

        @Override
        public void stop() {
            entity.setChargingPunch(false);
        }
    }


}
