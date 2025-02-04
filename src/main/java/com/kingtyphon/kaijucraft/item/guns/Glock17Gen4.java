package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.ParticleEffectPacket;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.lowdragmc.photon.client.fx.IFXEffect;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.List;

public class Glock17Gen4 extends Item {
    private static final double RANGE = 50.0; // Max range for the hitscan
    private static final float DAMAGE = 4.0f; // Damage per shot
    private static final int BULLETS = 17;
    private static int bulletsUsed = 0;
    public Glock17Gen4(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            if(bulletsUsed < (BULLETS -1)){
                performHitscan(level, player);
                player.getCooldowns().addCooldown(this, 3);
                bulletsUsed++;
            }
            //Last Shot handler
            else if(bulletsUsed == 16){
                performHitscan(level, player);
                bulletsUsed++;
            }else{
            bulletsUsed = 0;
            player.getCooldowns().addCooldown(this, 20);
            }// Short cooldown (adjust if needed)
        }

        return InteractionResultHolder.fail(itemStack);
    }
    private void performHitscan(Level level, Player player) {
        Vec3 startPos = player.getEyePosition(1.0F);
        Vec3 lookVector = player.getLookAngle().scale(RANGE);
        Vec3 endPos = startPos.add(lookVector);

        // Check for entity collision first
        EntityHitResult entityHit = getEntityInLineOfSight(level, player, startPos, endPos);
        if (entityHit != null) {
            Entity hitEntity = entityHit.getEntity();
            if (hitEntity instanceof LivingEntity target) {
                target.hurt(level.damageSources().playerAttack(player), DAMAGE);
            }

            // Spawn the gunshot effect at the entity's position
            spawnGunshotEffect(level, hitEntity.position());
            return;
        }

        // If no entity was hit, spawn the effect at the block impact position
        HitResult blockHit = player.pick(RANGE, 1.0F, false);
        if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
            spawnGunshotEffect(level, blockHit.getLocation());
        }
    }

    private void spawnGunshotEffect(Level level, Vec3 position) {
        if (!level.isClientSide) { // Ensure it's only sent from the server
            ModMessages.sendToAll(new ParticleEffectPacket(position));
        }
    }

    private EntityHitResult getEntityInLineOfSight(Level level, Player player, Vec3 start, Vec3 end) {
        EntityHitResult closestHit = null;
        double closestDistance = RANGE;

        for (Entity entity : level.getEntities(player, new AABB(start, end).inflate(1.0))) {
            if (entity.isPickable() && entity != player) {
                AABB aabb = entity.getBoundingBox();
                Optional<Vec3> hitVec = aabb.clip(start, end);

                if (hitVec.isPresent()) {
                    double distance = start.distanceTo(hitVec.get());
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestHit = new EntityHitResult(entity, hitVec.get());
                    }
                }
            }
        }
        return closestHit;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.isUsingItem() && entityLiving.getUsedItemHand() == hand) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }
                return HumanoidModel.ArmPose.ITEM;
            }
        });
    }
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true; // Prevents melee swing animation when holding the gun
    }
}