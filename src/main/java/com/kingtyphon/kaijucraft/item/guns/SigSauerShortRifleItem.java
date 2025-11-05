package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.common.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.entity.kaiju.OrganWeakSpotEntity;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.SyncPlayerAnimationPacket;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.function.Consumer;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

public class SigSauerShortRifleItem extends Item implements GeoItem {
    private static final int FIRE_RATE_TICKS = 3; // Controls automatic fire rate (1 = very fast, higher = slower)
    private static final float DAMAGE = 7.0f;
    private static final double RANGE = 75.0;
    private static final int BULLETS = 30;
    private static int bulletsUsed = 0;
    private static String bulletType = "Regular";

    private static boolean isShooting=false;
    private static boolean isReloading=false;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SigSauerShortRifleItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    public void setBulletType(String type){
        if(bulletType.equals(type)){return;}
        bulletType = type;
        //reload animation
    }
    public void reload(Player player,ItemStack weapon){

        if ((player.level() instanceof ServerLevel serverLevel)) {
            KaijuCraft.queueServerWork(30, () -> {setReloading(false);});
            triggerAnim(player, GeoItem.getOrAssignId(weapon, serverLevel), "sigsauer_controller", "reload");
            sendAnimationToServer(player, "reload_sigsauer", "animationsigsauer");
        }if(player.level().isClientSide){
        playAnimation(player, "reload_sigsauer");
        }
        syncGunFlagsToCapability(player);


    }
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof Glock17Gen4 glock) {
                if (!player.getMainHandItem().equals(pStack) || !glock.isReloading() &&!player.getMainHandItem().equals(pStack)) {
                    setShooting(false);
                    setReloading(false);
                    syncGunFlagsToCapability(player);

                }
            }}}
    private static void playAnimation(Player player, String animationName ){
        if(player.level().isClientSide){
            if (player instanceof AbstractClientPlayer clientPlayer) {
                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                        .getPlayerAssociatedData(clientPlayer)
                        .get(new ResourceLocation(MODID, "animationsigsauer"));
                if(animation != null) {
                    if (!animationName.equals("aim_glock") && !animationName.equals("aim_sigsauer") && !animationName.equals("recoil_glock") && !animationName.equals("recoil_sigsauer")) {
                        animation.setAnimation(new KeyframeAnimationPlayer(
                                PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))).setFirstPersonMode(FirstPersonMode.VANILLA).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true)));
                    } else if (animationName.equals("aim_glock") || animationName.equals("aim_sigsauer") || animationName.equals("recoil_glock") || animationName.equals("recoil_sigsauer")) {
                        animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))));
                    }
                }

            }
            sendAnimationToServer(player, animationName, "animationsigsauer" );
        }
    }
    private static void sendAnimationToServer(Player player, String animationName, String animationType) {
        ModMessages.sendToServer(new SyncPlayerAnimationPacket(player.getUUID(), animationName, animationType));
    }
    public void syncGunFlagsToCapability(Entity entity) {
        if (entity instanceof Player player) {
            // Get the player's capability
            IKaijuCapability capability = player.getCapability(KaijuProvider.KAIJU_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("No capability found"));

            // Sync the flags (reload, shoot) from the gun to the capability
            capability.setReloading(this.isReloading);
            capability.setShooting(this.isShooting);
        }
    }
    public void setReloading(boolean isReloading){
        this.isReloading = isReloading;
    }
    public void setShooting(boolean isShooting){
        this.isShooting = isShooting;
    }
    public boolean isReloading(){
        return isReloading;
    }
    public boolean isShooting(){
        return isShooting;
    }
    @Override
    public int getUseDuration(ItemStack stack) {
        return 999999; // Standard for bows; adjust as needed
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SigSauerShortRifleRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new SigSauerShortRifleRenderer();
                }
                return this.renderer;
            }
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.isUsingItem() && entityLiving.getUsedItemHand() == hand) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW; // Shooting animation
                }
                return HumanoidModel.ArmPose.ITEM;
            }
        });
    }
    public void changeBulletEvent(Player player){
//        reload(player, player.getMainHandItem());
        if(bulletType.equals("Explosive")){
            if ((player.level() instanceof ServerLevel serverLevel)) {
                KaijuCraft.queueServerWork(20, () -> {setBulletType("Regular");});
            }
        } else {
            if ((player.level() instanceof ServerLevel serverLevel)) {
                KaijuCraft.queueServerWork(20, () -> {setBulletType("Explosive");});
            }
        }

    }
    public String getBulletType(){

        return bulletType;
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            if (capability.getLevel() < 5) {
                player.sendSystemMessage(Component.literal("You are not strong enough to hold this."));
                return;
            }

            player.startUsingItem(hand); // Starts continuous shooting
        });

        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {

        InteractionHand hand = entity.getUsedItemHand();

        if (!(entity instanceof Player player)) return;
        if (player.getCooldowns().isOnCooldown(this)) return; // Prevents firing too fast

        if (level.isClientSide) {
            if (bulletsUsed < 29 && !isReloading()) {
                playAnimation(player, "recoil_sigsauer");
                setReloading(false);
                setShooting(true);
                syncGunFlagsToCapability(player);
//                rotatePlayerBodyToLook(player);


            } else {
                reload(player, stack);
                playAnimation(player, "reload_sigsauer");
                syncGunFlagsToCapability(player);
                player.getCooldowns().addCooldown(this, 30);
                setReloading(true);
                setShooting(false);
            }
        }else{
            if(bulletsUsed < (BULLETS -1)) {
                if (player.getCooldowns().isOnCooldown(this)) return; // Prevents firing too fast
                bulletsUsed++;
                shoot(level, player);
                player.getCooldowns().addCooldown(this, FIRE_RATE_TICKS); // Controls fire rate
                triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "sigsauer_controller", "siggunflash");

            }else if(bulletsUsed == 29){
                shoot(level, player);
                bulletsUsed++;
                triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "sigsauer_controller", "siggunflash");
            }else{
                player.getCooldowns().addCooldown(this, 30);
                reload(player, stack);// Controls fire rate
                bulletsUsed = 0;

            }
        }
    }
    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            // Player released right-click, stop shooting
            setReloading(false);
            setShooting(false);
            syncGunFlagsToCapability(player);
        }
    }
    private EntityHitResult getWeakSpotHit(Level level, Player shooter, Vec3 start, Vec3 end) {
        EntityHitResult closest = null;
        double closestDist = RANGE;

        // Look for ONLY OrganWeakSpotEntity
        for (OrganWeakSpotEntity weakSpot : level.getEntitiesOfClass(OrganWeakSpotEntity.class,
                new AABB(start, end).inflate(0.2))) {

            // skip if not alive
            if (!weakSpot.isAlive()) continue;

            // does the ray intersect the weak spot’s bounding box?
            Optional<Vec3> hit = weakSpot.getBoundingBox().clip(start, end);
            if (hit.isPresent()) {
                double dist = start.distanceTo(hit.get());
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = new EntityHitResult(weakSpot, hit.get());
                }
            }
        }

        return closest;
    }
    private void shoot(Level level, Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 look = player.getViewVector(1.0F);
        Vec3 end = start.add(look.scale(RANGE));

        IKaijuCapability capability = player.getCapability(KaijuProvider.KAIJU_CAPABILITY)
                .orElseThrow(() -> new IllegalArgumentException("No capability found"));

        int powerLevel = capability.getLevel();

        // First: check weak spot explicitly
        EntityHitResult weakspotHit = getWeakSpotHit(level, player, start, end);
        if (weakspotHit != null && weakspotHit.getEntity() instanceof OrganWeakSpotEntity weakSpot) {
            weakSpot.hurt(player.damageSources().playerAttack(player), (DAMAGE * 2.0F)); // gun does crit on weak spot
            return; // Stop here, don't double-hit other entities
        }

        // Otherwise: check for normal entities
        EntityHitResult entityHit = getEntityHit(level, player, start, end);

        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target) {
            if (capability.getRange() >= 7) {
                target.hurt(player.damageSources().playerAttack(player), (DAMAGE + (powerLevel * 0.15f)));
            } else {
                target.hurt(player.damageSources().playerAttack(player), DAMAGE);
            }
        } else {
            // Block collision check
            BlockHitResult blockHit = level.clip(new ClipContext(start, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
            if (blockHit.getType() == HitResult.Type.BLOCK) {
                BlockPos blockPos = blockHit.getBlockPos();
                // TODO: bullet impact effects here
            }
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
                    Vec3 hitPos = entityHitResult.getLocation();

                    // Send the particle packet to all clients
//                    ParticleEffectPacket packet = new ParticleEffectPacket(hitPos, entity);
//                    if (!level.isClientSide) { // Only send from the server
//                        ModMessages.sendToAll(packet);
//                    }
                }
            }
        }
        return entityHitResult;
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "sigsauer_controller", state -> {
            AnimationController<?> controller = state.getController();

            // If the animation has finished or isn't playing, ensure "idle" is looping
            if (controller.hasAnimationFinished() || controller.getCurrentAnimation() == null) {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }

            return PlayState.CONTINUE;
        }).triggerableAnim("siggunflash", DefaultAnimations.ITEM_ON_USE).triggerableAnim("reload",RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}