package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Optional;
import java.util.function.Consumer;

public class SigSauerShortRifleItem extends Item {
    private static final int FIRE_RATE_TICKS = 3; // Controls automatic fire rate (1 = very fast, higher = slower)
    private static final float DAMAGE = 6.0f;
    private static final double RANGE = 50.0;
    private static final int BULLETS = 30;
    private static int bulletsUsed = 0;

    public SigSauerShortRifleItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Allows holding right-click for auto-fire
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.isUsingItem() && entityLiving.getUsedItemHand() == hand) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW; // Shooting animation
                }
                return HumanoidModel.ArmPose.ITEM;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            if (capability.getLevel() < 5) {
                player.sendSystemMessage(Component.literal("You are not strong enough to hold this."));
                //player.addEffect(MobEffectInstance)
                return;
            }

            player.startUsingItem(hand); // Starts continuous shooting
        });

        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!(entity instanceof Player player)) return;

        if (!level.isClientSide) {
            if(bulletsUsed < (BULLETS -1)) {
                if (player.getCooldowns().isOnCooldown(this)) return; // Prevents firing too fast
                bulletsUsed++;
                shoot(level, player);
                player.getCooldowns().addCooldown(this, FIRE_RATE_TICKS); // Controls fire rate
            }else if(bulletsUsed == 29){
                shoot(level, player);
                bulletsUsed++;
            }else{
                player.getCooldowns().addCooldown(this, 30);
                bulletsUsed = 0;// Controls fire rate
            }
        }
    }

    private void shoot(Level level, Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(RANGE));

        // Block collision check
        BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        // Entity hit check
        EntityHitResult entityHit = getEntityHit(level, player, start, end);

        if (entityHit != null) { // If an entity is hit before a block
            LivingEntity target = (LivingEntity) entityHit.getEntity();
            target.hurt(player.damageSources().playerAttack(player), DAMAGE);
        } else if (blockHit.getType() == HitResult.Type.BLOCK) {
            end = blockHit.getLocation(); // Bullet stops at the block
        }

        // Spawn muzzle flash
        if (level instanceof ServerLevel serverLevel) {
            spawnMuzzleFlash(serverLevel, player);
        }
    }

    private EntityHitResult getEntityHit(Level level, Player shooter, Vec3 start, Vec3 end) {
        Vec3 direction = end.subtract(start).normalize();
        AABB box = new AABB(start, end).inflate(1.0); // Hitbox range
        EntityHitResult entityHitResult = null;
        double closestDistance = RANGE;

        for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, box, e -> e != shooter)) {
            AABB entityBox = entity.getBoundingBox().inflate(0.1);
            Optional<Vec3> optional = entityBox.clip(start, end);
            if (optional.isPresent()) {
                double distance = start.distanceTo(optional.get());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    entityHitResult = new EntityHitResult(entity, optional.get());
                }
            }
        }
        return entityHitResult;
    }

    private void spawnMuzzleFlash(ServerLevel serverLevel, Player player) {
        Vec3 particlePosition = player.getEyePosition().add(player.getViewVector(1.0F).scale(1.2));
        serverLevel.sendParticles(ParticleTypes.FLAME, particlePosition.x, particlePosition.y, particlePosition.z, 5, 0.05, 0.05, 0.05, 0.02);
        serverLevel.sendParticles(ParticleTypes.SMOKE, particlePosition.x, particlePosition.y, particlePosition.z, 3, 0.1, 0.1, 0.1, 0.01);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true; // Prevents melee swing animation when holding the gun
    }
}