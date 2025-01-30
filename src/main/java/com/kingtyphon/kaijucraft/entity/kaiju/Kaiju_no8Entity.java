package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.client.Kaiju_no8Model;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Kaiju_no8Entity extends Animal {
    public Kaiju_no8Entity(EntityType<? extends Animal> pEntityType, Level plevel){
        super(pEntityType,plevel);
    }

    private int idleAnimationTimeout = 0;
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState runAnimationState = new AnimationState();
    private int roarAnimationTimeout = 0;

    private int attackAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> IS_ROARING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Kaiju_no8Entity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROARING, false);
        this.entityData.define(ATTACKING, false);
    }
    public void setAttacking(boolean attacking){
        this.entityData.set(ATTACKING, attacking);
    }
    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }
    public boolean isWalking(){
        double speed = this.getDeltaMovement().horizontalDistance();
        return speed > 0.1 && speed <= 0.6; // Speed range for walking
    }
    public boolean isRoaring() {
        return this.entityData.get(IS_ROARING);
    }

    public void setRoaring(boolean roaring) {
        this.entityData.set(IS_ROARING, roaring);
    }

    @Override
    public void tick()
    {
        super.tick();

        if(this.level().isClientSide()){
            setupAnimationStates();
        }
        // Trigger roar animation when low health
        if (this.isRoaring() && this.tickCount % 40 == 0) { // Adjust tick duration as needed
            this.setRoaring(false);
        }

    }

    private void setupAnimationStates() {
        // Stop idle animation if any other animations are active
        if (isAttacking()) {
            stopAllAnimationsExcept(attackAnimationState);
            attackAnimationTimeout = 40;
            attackAnimationState.start(this.tickCount);
        }
        if (isRoaring()) {
            stopAllAnimationsExcept(roarAnimationState);
            roarAnimationState.start(this.tickCount);
            roarAnimationTimeout = 400;
        }else {
            --this.roarAnimationTimeout;
        }
        if (isSprinting() && !isWalking()) {
            stopAllAnimationsExcept(runAnimationState);
            runAnimationState.start(this.tickCount);
        } else if (isWalking() && !isSprinting()) {
            stopAllAnimationsExcept(null); // Stop other animations without starting a new one
        } else if (attackAnimationTimeout <= 0 && roarAnimationTimeout <=0) {
            // Start idle animation only when no other animations are active
            if (!idleAnimationState.isStarted()) {
                idleAnimationState.start(this.tickCount);
            }
        }

        // Decrease attack animation timeout
        if (attackAnimationTimeout > 0) {
            attackAnimationTimeout--;
        }
    }

    private void stopAllAnimationsExcept(@Nullable AnimationState activeState) {
        if (activeState != idleAnimationState) idleAnimationState.stop();
        if (activeState != runAnimationState) runAnimationState.stop();
        if (activeState != attackAnimationState) attackAnimationState.stop();
        if (activeState != roarAnimationState) roarAnimationState.stop();
    }
    @Override
    protected void updateWalkAnimation(float pPartialTick){
        double speed = this.getDeltaMovement().horizontalDistance();
        float f = Math.min((float) (speed * 10f), 1f); // Scale movement speed to animation progress
        this.walkAnimation.update(f, 0.2f);
    }
    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class,5f));
        this.goalSelector.addGoal(3, new RoarGoal(this));
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this));


    }

    public static AttributeSupplier.Builder createAttributes() {
            return Animal.createLivingAttributes()
                    .add(Attributes.MAX_HEALTH, 50D)
                    .add(Attributes.ATTACK_DAMAGE, 10.0f)
                    .add(Attributes.ATTACK_SPEED, 1.0f)
                    .add(Attributes.MOVEMENT_SPEED, 0.6f)
                    .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0f);
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
            return !this.entity.isAttacking() && this.entity.getRandom().nextInt(100) == 0 && entity.roarAnimationTimeout <= 0;
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

}
