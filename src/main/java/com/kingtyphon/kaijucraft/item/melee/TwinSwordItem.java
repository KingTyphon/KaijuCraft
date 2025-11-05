package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.entity.kaiju.OrganWeakSpotEntity;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;


import java.util.List;
import java.util.Optional;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

public class TwinSwordItem extends Item {
    public TwinSwordItem(Properties pProperties) {
        super(pProperties);
    }

    //If its dual wielding
    public static boolean isDualWielding(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        ItemStack offHand = entity.getOffhandItem();
        return mainHand.getItem() instanceof TwinSwordItem && offHand.getItem() instanceof TwinSwordItem;
    }
    private void performSwooshSound(Level level, Player player) {
        if (level.isClientSide) {
            // Play looping sound on the client side
            RandomSource random = level.getRandom();
            SimpleSoundInstance soundInstance = new SimpleSoundInstance(
                    KaijuSounds.SWORD.get(),       // Sound event
                    SoundSource.PLAYERS,                  // Sound source
                    0.3F,                                 // Volume
                    1.0F,                                 // Pitch
                    random,                              // Random source
                    player.getX(), player.getY(), player.getZ()  // Position
            );
            Minecraft.getInstance().getSoundManager().play(soundInstance);
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        // Prevent breaking blocks when using this item
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
                return false;
            }

            boolean retval = super.onEntitySwing(itemstack, entity);

            entity.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
                if (isDualWielding(entity)) {
                    performSwooshSound(player.level(), player);

                    CompoundTag tag = itemstack.getOrCreateTag();
                    String prevAttack = tag.getString("prevAttack");
                    int cooldownTicks = (kaiju.getMelee() >= 7) ? 3 : 10;

                    // pick swing side
                    if (prevAttack.isEmpty()) {
                        prevAttack = Math.random() < 0.5 ? "leftattackswing_ts" : "rightattackswing_ts";
                    } else if (prevAttack.equals("leftattackswing_ts")) {
                        prevAttack = "rightattackswing_ts";
                        player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), cooldownTicks);
                    } else {
                        prevAttack = "leftattackswing_ts";
                        player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), cooldownTicks);
                    }

                    playAnimation(player, prevAttack);

                    // little lunge forward
                    Vec3 lookVec = player.getLookAngle();
                    player.push(lookVec.x * 0.5, 0, lookVec.z * 0.5);

                    // ---------- 🔥 NEW: raytrace just for weak spot ----------
                    Vec3 start = player.getEyePosition(1.0F);
                    Vec3 end = start.add(player.getLookAngle().scale(4.0D)); // melee reach ~4 blocks
                    EntityHitResult hitResult = getWeakSpotInSight(player.level(), player, start, end);
                    if (hitResult != null) {
                        Entity hitEntity = hitResult.getEntity();
                        if (hitEntity instanceof OrganWeakSpotEntity weakSpot) {
                            weakSpot.hurt(player.damageSources().playerAttack(player), 6.0F);
                        }
                    }

                    // ---------- existing AoE swing for other entities ----------
                    double radius = 2.0;
                    float attackDamage = (kaiju.getMelee() > 7)
                            ? Math.max(10, kaiju.getLevel() * 0.1F)
                            : 4.0F;

                    Vec3 playerPos = player.position();
                    Vec3 playerLook = player.getLookAngle();

                    List<Entity> nearbyEntities = player.level().getEntities(player, new AABB(
                            playerPos.x - radius, playerPos.y - 1, playerPos.z - radius,
                            playerPos.x + radius, playerPos.y + 2, playerPos.z + radius
                    ));

                    for (Entity entityNear : nearbyEntities) {
                        if (entityNear instanceof LivingEntity target && entityNear != player && !(entityNear instanceof OrganWeakSpotEntity)) {
                            Vec3 toEntity = entityNear.position().subtract(playerPos).normalize();
                            double angle = Math.toDegrees(Math.acos(toEntity.dot(playerLook)));

                            if (angle <= 90) {
                                target.hurt(player.damageSources().playerAttack(player), attackDamage);
                            }
                        }
                    }

                    // save swing state
                    tag.putString("prevAttack", prevAttack);
                    itemstack.setTag(tag);
                }
            });

            return retval;
        }
        return false;
    }

    /**
     * Only finds OrganWeakSpotEntity in the player’s look vector.
     */
    private EntityHitResult getWeakSpotInSight(Level level, Player player, Vec3 start, Vec3 end) {
        for (Entity entity : level.getEntities(player, new AABB(start, end).inflate(0.5))) {
            if (entity instanceof OrganWeakSpotEntity) {
                AABB aabb = entity.getBoundingBox();
                Optional<Vec3> hitVec = aabb.clip(start, end);
                if (hitVec.isPresent()) {
                    return new EntityHitResult(entity, hitVec.get());
                }
            }
        }
        return null;
    }
    private static void playAnimation(Player player, String animationName) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                    .getPlayerAssociatedData(clientPlayer)
                    .get(new ResourceLocation(MODID, "animation"));

            if (animation != null) {
                animation.setAnimation(new KeyframeAnimationPlayer(
                        PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true)));
            }

        }}

}
