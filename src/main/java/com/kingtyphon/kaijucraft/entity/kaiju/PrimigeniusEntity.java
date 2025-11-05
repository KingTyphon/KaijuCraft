package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PrimigeniusEntity extends Animal {
    public PrimigeniusEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {

        super(pEntityType, pLevel);
    }

    public final AnimationState idleAnimationState = new AnimationState();
    private int roarAnimationTimeout = 0;
    private int attackAnimationTimeout = 0;
    private int idleAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState roarAnimationState = new AnimationState();
    private static final EntityDataAccessor<Boolean> IS_ROARING = SynchedEntityData.defineId(PrimigeniusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(PrimigeniusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_SHINY = SynchedEntityData.defineId(PrimigeniusEntity.class, EntityDataSerializers.BOOLEAN);
    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData groupData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);

        // 5% chance to spawn as shiny (adjust as needed)
        if (this.random.nextFloat() < 0.05f) {
            this.setShiny(true);
        }

        return groupData;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_ROARING, false);
        this.entityData.define(IS_SHINY, false);
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
    public boolean isShiny() {
        return this.entityData.get(IS_SHINY);
    }

    public void setShiny(boolean shiny) {
        this.entityData.set(IS_SHINY, shiny);
    }
    public static boolean canSpawn(EntityType<PrimigeniusEntity> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkAnimalSpawnRules(entityType, level, spawnType, pos, random) && pos.getY() > 100;
    }
    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();
        }


        // Trigger roar animation when low health
        if (this.isRoaring() && this.tickCount % 40 == 0) { // Adjust tick duration as needed
            this.setRoaring(false);
        }
    }
    private void setupAnimationStates() {
        // Stop idle animation if any other animations are active
        if (isAttacking() && attackAnimationTimeout == 0) {
            stopAllAnimationsExcept(attackAnimationState);
            attackAnimationTimeout = 40;
            attackAnimationState.start(this.tickCount);
        } else if (isRoaring() && roarAnimationTimeout == 0) {
            stopAllAnimationsExcept(roarAnimationState);
            roarAnimationState.start(this.tickCount);
            roarAnimationTimeout = 400;
        } else {
            // If no other animations are active, play idle animation
            if (idleAnimationTimeout <= 0) {
                stopAllAnimationsExcept(idleAnimationState);
                idleAnimationState.start(this.tickCount);
                idleAnimationTimeout = this.random.nextInt(40) + 80;
            } else {
                --idleAnimationTimeout;
            }
        }

        // Decrease animation timeouts
        if (attackAnimationTimeout > 0) {
            attackAnimationTimeout--;
        }
        if (roarAnimationTimeout > 0) {
            roarAnimationTimeout--;
        }
    }
    private void stopAllAnimationsExcept(@Nullable AnimationState activeState) {
         if (activeState != idleAnimationState) idleAnimationState.stop();
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
    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return 2.0F; // Adjust this to match your model's proportions
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 200D)
                .add(Attributes.ATTACK_DAMAGE, 40.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0f);
    }
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Shiny", this.isShiny());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setShiny(tag.getBoolean("Shiny"));
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
        return null;
    }
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return null;
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
        private final PrimigeniusEntity entity;

        public RoarGoal(PrimigeniusEntity entity) {
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
        private final PrimigeniusEntity entity;


        public AttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
            entity = ((PrimigeniusEntity) pMob);
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
}
