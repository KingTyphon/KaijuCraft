package com.kingtyphon.kaijucraft.entity.kaiju;

import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class PhaneroplusEntity extends Animal implements GeoEntity {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private float scale;
    private final float magnitude = 2.3F;
    private OrganWeakSpotEntity weakSpot;
    private boolean canDuplicate = true; // you could tie this to "organs intact"
    private int duplicationCooldown = 0;
    private boolean isChild = false; // 👈 mark duplicates

    public PhaneroplusEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.scale = 1.0F + this.random.nextFloat() * 1.25F;

    }
    public PhaneroplusEntity(EntityType<? extends Animal> type, Level level, float scale, boolean isChild) {
        super(type, level);
        this.scale = scale;
        this.isChild = isChild;
        this.canDuplicate = !isChild; // children can’t duplicate again
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 100D)
                .add(Attributes.ATTACK_DAMAGE, 10.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.55f)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.ARMOR_TOUGHNESS,  20.0D)
                .add(Attributes.ARMOR,  5.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 3.0f);
    }
    @Override
    public void die(DamageSource source) {

        if (!this.level().isClientSide && canDuplicate) {
            int splits = 2 + this.random.nextInt(2); // 2–3 copies
            for (int i = 0; i < splits; i++) {
                duplicateEntity();
            }
        }
        super.die(source); // parent cleanup after children are spawned

    }
    private void duplicateEntity() {
        float newScale = Math.max(0.5F, this.scale * 0.5F); // half size
        PhaneroplusEntity duplicate = new PhaneroplusEntity(EntityInit.PHANEROPLUS.get(), this.level(), newScale, true);

        duplicate.moveTo(
                this.getX() + (this.random.nextDouble() - 0.5D) * 2.0D,
                this.getY(),
                this.getZ() + (this.random.nextDouble() - 0.5D) * 2.0D,
                this.getYRot(),
                this.getXRot()
        );

        duplicate.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getMaxHealth() / 2);
        duplicate.setHealth((float) duplicate.getAttribute(Attributes.MAX_HEALTH).getBaseValue());

        this.level().addFreshEntity(duplicate);
    }
    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            if ((weakSpot == null || !weakSpot.isAlive()) && this.isAlive()) {
                weakSpot = new OrganWeakSpotEntity(this, this.level());
                weakSpot.setPos(this.getX(), this.getY() + 1.5D, this.getZ());
                this.level().addFreshEntity(weakSpot);
            }
            if (duplicationCooldown > 0) {
                duplicationCooldown--;
            } else if (canDuplicate && this.getHealth() <= this.getMaxHealth() / 2) {
                // Example: starts duplicating once health < 50%
                duplicateEntity();
                duplicationCooldown = 20 * 30; // 30s cooldown
            }
        }
    }

    public void onOrganBurst(DamageSource source, float damage) {
        this.canDuplicate = false; // organs destroyed → no splitting anymore
        this.hurt(source, damage * 3.0F); // take extra heavy damage
    }
    public float getEntityScale() {
        return this.scale;
    }
    @Override
    protected void registerGoals(){
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(5, new HurtByTargetGoal(this));

    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar){
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }
    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
