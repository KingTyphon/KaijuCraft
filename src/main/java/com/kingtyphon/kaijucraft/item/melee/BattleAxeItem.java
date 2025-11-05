package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.KaijuCraft;
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
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

public class BattleAxeItem extends Item implements GeoItem {
    private static final int SWING_DURATION = 5;// Approximate ticks for a normal swing
    private boolean startedSlam;
    private boolean startedEnding;
    private boolean isOverheadCooldown = false; // Flag to track if item is on cooldown
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private double radius = 2.0;
    private double attackDamage = 5;


    public BattleAxeItem(Properties pProperties) {
        super(pProperties);
    }
    public boolean getStartedSlam(){
    return startedSlam;
    }
    public boolean getFinalSlam(){
        return startedEnding;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        // Prevent breaking blocks when using this item
        return false;
    }
    private void performSwooshSound(Level level, Player player) {
        if (level.isClientSide) {
            // Play looping sound on the client side
            RandomSource random = level.getRandom();
            SimpleSoundInstance soundInstance = new SimpleSoundInstance(
                    KaijuSounds.AXESWOOSH.get(),       // Sound event
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
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
        boolean retval = super.onEntitySwing(itemstack, entity);

        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
                return false; // Cancel swing if on cooldown
            }

            entity.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
                boolean isWallRunning = kaiju.isRunningwall();

                CompoundTag tag = itemstack.getOrCreateTag();

                if (player.onGround()) {
                    // ---------- normal swing ----------
                    Vec3 playerPos = player.position();
                    Vec3 playerLook = player.getLookAngle();
                    performSwooshSound(player.level(), player);
                    playAnimation(player, "swing_ba");

                    // 🔥 raytrace just for weak spot
                    Vec3 start = player.getEyePosition(1.0F);
                    Vec3 end = start.add(player.getLookAngle().scale(4.0D)); // melee reach
                    EntityHitResult weakspotHit = getWeakSpotInSight(player.level(), player, start, end);
                    if (weakspotHit != null && weakspotHit.getEntity() instanceof OrganWeakSpotEntity weakSpot) {
                        weakSpot.hurt(player.damageSources().playerAttack(player), 8.0F); // axe bonus dmg
                    }

                    // normal AoE for other mobs
                    List<Entity> nearbyEntities = player.level().getEntities(player, new AABB(
                            playerPos.x - radius, playerPos.y - 1, playerPos.z - radius,
                            playerPos.x + radius, playerPos.y + 2, playerPos.z + radius
                    ));
                    for (Entity entityNear : nearbyEntities) {
                        if (entityNear instanceof LivingEntity target && entityNear != player && !(entityNear instanceof OrganWeakSpotEntity)) {
                            Vec3 toEntity = entityNear.position().subtract(playerPos).normalize();
                            double angle = Math.toDegrees(Math.acos(toEntity.dot(playerLook)));

                            if (angle <= 90) {
                                target.hurt(player.damageSources().playerAttack(player), (float) attackDamage);
                            }
                        }
                    }

                    player.getCooldowns().addCooldown(itemstack.getItem(), 7);

                } else if (!isOverheadCooldown && !isWallRunning) {
                    // ---------- overhead slam ----------
                    playAnimation(player, "overheadstart_ba");
                    startedSlam = true;
                    isOverheadCooldown = true;
                    KaijuCraft.queueServerWork(60, () -> {
                        isOverheadCooldown = false;
                    });

                    // Lunge forward
                    Vec3 lookVec = player.getLookAngle();
                    Vec3 lunge = new Vec3(lookVec.x * 0.75, 0, lookVec.z * 0.75);
                    player.setDeltaMovement(player.getDeltaMovement().add(lunge));

                    player.getCooldowns().addCooldown(itemstack.getItem(), 60);
                }
            });
        }

        return retval;
    }

    /** Same helper from sword, only checks for OrganWeakSpotEntity */
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
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {

        if (entity instanceof Player player) {
            if (!player.level().isClientSide ) {
                double groundDistance = player.position().y - player.level().getHeightmapPos(Heightmap.Types.WORLD_SURFACE, player.blockPosition()).getY();
                boolean isFalling = player.getDeltaMovement().y < -0.1;

                if (startedSlam) {
                    if (isFalling && groundDistance > 2.1) {
                        // If the player is still in the air, play or maintain the overhead idle animation
                        playAnimation(player, "overheadidle_ba");
                    } else if (groundDistance < 2.0) {
                        // Trigger the ending animation when close to the ground
                        this.startedSlam = false;
                        this.startedEnding = true;
                        playAnimation(player, "overheadend_ba");
                        triggerSlam(player);
                        KaijuCraft.queueServerWork(3, () -> {


                            startedEnding = false;
                        });

                    }

                }
            }
        }
    }

    private void triggerSlam(Player player) {
        if (!player.level().isClientSide ) {
        // Play end animation when swing finishes
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            applySlamDamageToNearbyEntities(player, kaiju.getLevel());
        });
        }
    }

    private void applySlamDamageToNearbyEntities(Player player, int kaijuLevel) {
        double radius = 2.0; // Vertical radius for slam attack (below the player)
        double attackDamage = Math.max(4.0, kaijuLevel * 0.1F) * 1.5; // Increased damage for slam

        Vec3 playerPos = player.position();

        // Get all nearby entities within the vertical radius below the player
        List<Entity> nearbyEntities = player.level().getEntities(player, new AABB(
                playerPos.x - radius, playerPos.y - 5, playerPos.z - radius, // Min bounds (vertical radius below)
                playerPos.x + radius, playerPos.y + 5, playerPos.z + radius  // Max bounds (vertical radius above)
        ));

        // Apply damage to entities directly below the player (y-position check)
        for (Entity entityNear : nearbyEntities) {
            if (entityNear instanceof LivingEntity target && entityNear != player) {
                // Only hurt entities that are below the player (y-position check)
                if (entityNear.position().y < player.position().y) {
                    target.hurt(player.damageSources().playerAttack(player), (float) attackDamage);
                }
            }
        }

    }

    private void playAnimation(Player player, String animationName) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            var animationLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                    .getPlayerAssociatedData(clientPlayer)
                    .get(new ResourceLocation(MODID, "animationaxe"));
            if(animationName == "overheadend_ba"){ this.startedEnding = true;
                this.startedSlam = false;
            }
            if (animationLayer != null) {
                KeyframeAnimationPlayer animPlayer = new KeyframeAnimationPlayer(
                        PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))
                ).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL)
                        .setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true));
//
                // Set the animation to the player
                animationLayer.setAnimation(animPlayer);

                // Check if we need to transition to "overheadidle_ba" after "overheadstart_ba"

            }
        }
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BattleAxeRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new BattleAxeRenderer();
                }
                return this.renderer;
            }
        });
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", state -> {
            AnimationController<?> controller = state.getController();

            // If the animation has finished or isn't playing, ensure "idle" is looping
            if (controller.hasAnimationFinished() || controller.getCurrentAnimation() == null) {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}