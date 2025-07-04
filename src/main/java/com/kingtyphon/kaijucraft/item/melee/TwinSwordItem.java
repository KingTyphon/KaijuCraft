package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
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
import net.minecraft.world.phys.Vec3;


import java.util.List;

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

    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
        if (entity instanceof Player player) {
            // Check if either sword is on cooldown
            if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
                return false; // Cancel the swing if cooldown is active
            }
        }

        boolean retval = super.onEntitySwing(itemstack, entity);
        entity.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
        if (entity instanceof Player player && isDualWielding(entity)) {
            // Get previous attack from item NBT
            performSwooshSound(player.level(), player);
            CompoundTag tag = itemstack.getOrCreateTag();
            String prevAttack = tag.getString("prevAttack");
            int cooldownTicks = (kaiju.getMelee()>= 7) ? 3 : 10; // Adjust cooldown duration as needed (10 ticks = 0.5 sec)


            if (prevAttack.isEmpty()) {
                prevAttack = Math.random() < 0.5 ? "leftattackswing_ts" : "rightattackswing_ts";
            } else if (prevAttack.equals("leftattackswing_ts")) {
                prevAttack = "rightattackswing_ts";
                player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), cooldownTicks);

            } else {
                prevAttack = "leftattackswing_ts";
                player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), cooldownTicks);

            }

            // Play the chosen animation
            playAnimation(player, prevAttack);

            // Apply lunge forward in the direction the player is looking
            Vec3 lookVec = player.getLookAngle(); // Get player's look direction
            double lungeStrength = 0.5; // Adjust strength as needed
            Vec3 lunge = new Vec3(lookVec.x * lungeStrength, 0, lookVec.z * lungeStrength); // No Y movement
            player.setDeltaMovement(player.getDeltaMovement().add(lunge)); // Add to current velocity
            double radius = 2.0;
            double attackDamage =4;
            if(kaiju.getMelee() >7) {
                attackDamage = Math.max(6 , kaiju.getLevel() * 0.1F); // Picks the greater value
            }
// Get player's position
            Vec3 playerPos = player.position();
            Vec3 playerLook = player.getLookAngle();

// Get all nearby entities within a radius
            List<Entity> nearbyEntities = player.level().getEntities(player, new AABB(
                    playerPos.x - radius, playerPos.y - 1, playerPos.z - radius, // Min bounds
                    playerPos.x + radius, playerPos.y + 2, playerPos.z + radius  // Max bounds
            ));

// Filter entities in front of the player within 180 degrees
            for (Entity entityNear : nearbyEntities) {
                if (entityNear instanceof LivingEntity target && entityNear != player) {
                    Vec3 toEntity = entityNear.position().subtract(playerPos).normalize(); // Direction to entity
                    double angle = Math.toDegrees(Math.acos(toEntity.dot(playerLook))); // Angle between look and target

                    if (angle <= 90) { // 180-degree check (90 degrees in each direction)
                        target.hurt(player.damageSources().playerAttack(player), (float) attackDamage);
                    }
                }
            }
            // Save new attack direction
            tag.putString("prevAttack", prevAttack);
            itemstack.setTag(tag);
        }

        });

        return retval;
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
