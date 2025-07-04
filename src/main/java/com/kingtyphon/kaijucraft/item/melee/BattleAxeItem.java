package com.kingtyphon.kaijucraft.item.melee;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4Renderer;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
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
    public boolean onEntitySwing(ItemStack itemstack, LivingEntity entity) {
        boolean retval = super.onEntitySwing(itemstack, entity);
        if (entity instanceof Player player) {
            // Check if either sword is on cooldown
            if (player.getCooldowns().isOnCooldown(itemstack.getItem())) {
                return false; // Cancel the swing if cooldown is active
            }
        }


        entity.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            boolean isWallRunning = kaiju.isRunningwall();
            if (entity instanceof Player player) {
                CompoundTag tag = itemstack.getOrCreateTag();

                // If player is on the ground, play the "swing_ba" animation
                if (player.onGround()) {
                    Vec3 playerPos = player.position();
                    Vec3 playerLook = player.getLookAngle();
                    performSwooshSound(player.level(), player);
                    playAnimation(player, "swing_ba");
                    List<Entity> nearbyEntities = player.level().getEntities(player, new AABB(
                            playerPos.x - radius, playerPos.y - 1, playerPos.z - radius, // Min bounds
                            playerPos.x + radius, playerPos.y + 2, playerPos.z + radius  // Max bounds
                    ));
                    for (Entity entityNear : nearbyEntities) {
                        if (entityNear instanceof LivingEntity target && entityNear != player) {
                            Vec3 toEntity = entityNear.position().subtract(playerPos).normalize(); // Direction to entity
                            double angle = Math.toDegrees(Math.acos(toEntity.dot(playerLook))); // Angle between look and target

                            if (angle <= 90) { // 180-degree check (90 degrees in each direction)
                                target.hurt(player.damageSources().playerAttack(player), (float) attackDamage);
                            }
                        }
                    }
                    player.getCooldowns().addCooldown(itemstack.getItem(), 7);

                } else if (isOverheadCooldown == false && !isWallRunning) {

                    playAnimation(player, "overheadstart_ba");
                    startedSlam = true;
                    isOverheadCooldown = true;
                    KaijuCraft.queueServerWork(60,() ->{
                        isOverheadCooldown = false;
                    });
                    // Apply forward movement based on the player's look direction
                    Vec3 lookVec = player.getLookAngle();
                    double lungeStrength = 0.75; // Adjust strength as needed
                    Vec3 lunge = new Vec3(lookVec.x * lungeStrength, 0, lookVec.z * lungeStrength); // No Y movement
                    player.setDeltaMovement(player.getDeltaMovement().add(lunge)); // Add to current velocity
                    player.getCooldowns().addCooldown(itemstack.getItem(), 60);

                }
            }
        });

        return retval;
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