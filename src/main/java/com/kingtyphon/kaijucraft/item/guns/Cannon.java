package com.kingtyphon.kaijucraft.item.guns;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class Cannon extends Item implements GeoItem {
    private static final double RANGE = 50.0; // Max range for the hitscan
    private static final float DAMAGE = 4.0f; // Damage per shot
    private static final int BULLETS = 5;
    private static int bulletsUsed = 0;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Cannon(Properties pProperties) {
        super(pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CannonRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new CannonRenderer();
                }
                return this.renderer;
            }
        });
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "cannon_controller", state -> {
            AnimationController<?> controller = state.getController();

            // If the animation has finished or isn't playing, ensure "idle" is looping
            if (controller.hasAnimationFinished() || controller.getCurrentAnimation() == null) {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }

            return PlayState.CONTINUE;
        }));
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        if (!level.isClientSide) {
            handleSwingShot(level, player, stack);
        }
        return true; // Prevents default attack behavior if desired
    }
    public void handleSwingShot(Level level, Player player, ItemStack stack) {
        if (bulletsUsed < (BULLETS - 1)) {
            player.getCooldowns().addCooldown(this, 3); // Quick cooldown
            bulletsUsed++;
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "cannon_controller", "swingflash");

        } else if (bulletsUsed == (BULLETS - 1)) { // Last shot
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "cannon_controllerx", "swingflash");
            bulletsUsed = 0; // Reset after last shot
            player.getCooldowns().addCooldown(this, 20); // Longer cooldown
        }

        // Client-side sound (handled via packet or separate logic if needed)
        if (level.isClientSide && bulletsUsed < BULLETS) {
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
