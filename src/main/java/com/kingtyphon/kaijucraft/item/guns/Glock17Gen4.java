package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.ParticleEffectPacket;
import com.kingtyphon.kaijucraft.networking.packets.SyncPlayerAnimationPacket;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Optional;
import java.util.function.Consumer;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

public class Glock17Gen4 extends Item  implements GeoItem {
    private static final double RANGE = 50.0; // Max range for the hitscan
    private static final float DAMAGE = 4.0f; // Damage per shot
    private static final int BULLETS = 17;
    private static int bulletsUsed = 0;
    private static boolean isShooting=false;
    private static boolean isReloading=false;
    // Ice,Explosive
    private static String bulletType = "Regular";
    private static float recoilPitch = 0.0f; // Stores accumulated recoil
    private static float recoilDecay = 0.6f;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Glock17Gen4(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }
    public void setBulletType(String type){
        if(bulletType.equals(type)){
            return;
        }else{
            bulletType = type;
        }
        //reload animation
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
    public int getUseDuration(ItemStack stack) {
        return 999999; // Standard for bows; adjust as needed
    }
    public void syncGunFlagsToCapability(Entity entity) {
        if (entity instanceof Player player) {
            // Get the player's capability
            IKaijuCapability capability;
            capability = player.getCapability(KaijuProvider.KAIJU_CAPABILITY).orElseThrow(() -> new IllegalArgumentException("No capability found"));

            // Sync the flags (reload, shoot) from the gun to the capability
            capability.setReloading(this.isReloading);
            capability.setShooting(this.isShooting);
        }
    }
    public void reload(Player player,ItemStack weapon){
        player.getCooldowns().addCooldown(this, 30);
        bulletsUsed = 0;
        if ((player.level() instanceof ServerLevel serverLevel)) {
            KaijuCraft.queueServerWork(20, () -> {setReloading(false);});
            triggerAnim(player, GeoItem.getOrAssignId(weapon,serverLevel), "flash_controller", "reload");
            sendAnimationToServer(player, "reload_glock", "animationglock");
        }if(player.level().isClientSide){
            playAnimation(player, "reload_glock");

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
        }}
        super.inventoryTick(pStack,pLevel,pEntity,pSlotId,pIsSelected);
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
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            // Player released right-click, stop shooting
            setReloading(false);
            setShooting(false);
            syncGunFlagsToCapability(player);
        }
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (level.isClientSide) {
            if(isReloading()){return InteractionResultHolder.fail(itemStack);}
            if (bulletsUsed < 17 && !isReloading()) {
                performShotSound(level, player);
                playAnimation(player, "recoil_glock");
                setReloading(false);
                setShooting(true);
                syncGunFlagsToCapability(player);
//                rotatePlayerBodyToLook(player);


            } else {
                reload(player, itemStack);
                playAnimation(player, "reload_glock");
                syncGunFlagsToCapability(player);
                player.getCooldowns().addCooldown(this, 30);
                setReloading(true);
                setShooting(false);
            }
        }else{
            if (bulletsUsed < (BULLETS - 1)&& !isReloading()) {
                performHitscan(level, player);
                player.getCooldowns().addCooldown(this, 3);
                applyRecoil(player);
                bulletsUsed++;
//                rotatePlayerBodyToLook(player);
                triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "flash_controller", "gunflash");
                sendAnimationToServer(player,"recoil_glock", "animationglock");
                syncGunFlagsToCapability(player);

            }
            //Last Shot handler
            else if (bulletsUsed == 16) {
                performHitscan(level, player);
                applyRecoil(player);
//                rotatePlayerBodyToLook(player);
                triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), (ServerLevel) level), "flash_controller", "gunflash");
                sendAnimationToServer(player,"recoil_glock", "animationglock");
                bulletsUsed++;
                syncGunFlagsToCapability(player);

            } else {
                triggerAnim(player, GeoItem.getOrAssignId(itemStack, (ServerLevel) level), "flash_controller", "reload");
                sendAnimationToServer(player,"reload_glock", "animationglock");
                player.getCooldowns().addCooldown(this, 30);
                reload(player, itemStack);

            }// Short cooldown (adjust if needed)


        }
        player.startUsingItem(hand);
        return InteractionResultHolder.fail(itemStack);
    }
//    private void rotatePlayerBodyToLook(Player player) {
//        if (player instanceof ServerPlayer) {
//            ServerPlayer serverPlayer = (ServerPlayer) player;
//
//            // Get the player's look direction (camera angle)
//            Vec3 lookDirection = player.getViewVector(1.0F);
//
//            // Calculate the yaw and pitch based on the look direction
//            double yaw = Math.toDegrees(Math.atan2(lookDirection.x, lookDirection.z)) - 90.0;
//            double pitch = Math.toDegrees(Math.atan2(lookDirection.y, Math.sqrt(lookDirection.x * lookDirection.x + lookDirection.z * lookDirection.z)));
//
//            // Apply the calculated yaw and pitch to the player's body rotation
//            serverPlayer.setYHeadRot((float) yaw);  // Set the head rotation
//            serverPlayer.setXRot((float) pitch);  // Set the body pitch
//        }
//    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();
        if (!level.isClientSide) {
//            handleSwingShot(level, player, stack);

        }
        return true; // Prevents default attack behavior if desired
    }

    public void handleSwingShot(Level level, Player player, ItemStack stack) {
        if (bulletsUsed < (BULLETS - 1)) {
            performHitscan(level, player);
            player.getCooldowns().addCooldown(this, 3); // Quick cooldown
            applyRecoil(player);
            bulletsUsed++;
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "swing_controller", "swingflash");

        } else if (bulletsUsed == (BULLETS - 1)) { // Last shot
            performHitscan(level, player);
            applyRecoil(player);
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "swing_controller", "swingflash");
            player.getCooldowns().addCooldown(this, 30); // Longer cooldown
        } else {
            reload(player, stack);
            player.getCooldowns().addCooldown(this, 30);
        }

        // Client-side sound (handled via packet or separate logic if needed)
        if (level.isClientSide && bulletsUsed < BULLETS) {
            performShotSound(level, player);
        }
    }

    private void performShotSound(Level level, Player player) {
        if (level.isClientSide) {
            // Play looping sound on the client side
            RandomSource random = level.getRandom();
            SimpleSoundInstance soundInstance = new SimpleSoundInstance(
                    KaijuSounds.GUNSHOTG17.get(),       // Sound event
                    SoundSource.PLAYERS,                  // Sound source
                    0.1F,                                 // Volume
                    1.0F,                                 // Pitch
                    random,                              // Random source
                    player.getX(), player.getY(), player.getZ()  // Position
            );
            Minecraft.getInstance().getSoundManager().play(soundInstance);
        }
    }

    private void performHitscan(Level level, Player player) {
        Vec3 startPos = player.getEyePosition(1.0F);
        Vec3 lookVector = player.getLookAngle().scale(RANGE);
        Vec3 endPos = startPos.add(lookVector);

        // First, check if we hit an entity
        EntityHitResult entityHit = getEntityInLineOfSight(level, player, startPos, endPos);
        if (entityHit != null) {
            Vec3 hitPos = entityHit.getLocation(); // Get exact hit position
            Entity hitEntity = entityHit.getEntity();
            IKaijuCapability capability = player.getCapability(KaijuProvider.KAIJU_CAPABILITY)
                    .orElseThrow(() -> new IllegalArgumentException("No capability found"));

            // Scale damage based on power level
            int powerLevel = capability.getLevel();
            if (bulletType.equals("Explosive")) {
                if (!level.isClientSide) {
                    // Create explosion damage (without particles)
                    level.explode(null, hitPos.x, hitPos.y, hitPos.z, 1.0F, Level.ExplosionInteraction.MOB);

                    // Spawn a flash effect
                    ((ServerLevel)level).sendParticles(ParticleTypes.FLASH, hitPos.x, hitPos.y, hitPos.z, 1, 0, 0, 0, 0);
                }
            } else {
                // Normal bullet damage
                if (hitEntity instanceof LivingEntity target) {
                    target.hurt(level.damageSources().playerAttack(player), (DAMAGE + (powerLevel * 0.25f )));
                }
            }

            isShooting = true;
            return;
        }

        // If no entity was hit, check for block collision
        HitResult blockHit = player.pick(RANGE, 1.0F, false);
        if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
            Vec3 hitPos = blockHit.getLocation();

            if (bulletType.equals("Explosive")) {
                if (!level.isClientSide) {
                    // Create explosion damage (without particles)
                    level.explode(null, hitPos.x, hitPos.y, hitPos.z, 1.0F, Level.ExplosionInteraction.MOB);

                    // Spawn a flash effect
                    ((ServerLevel)level).sendParticles(ParticleTypes.FLASH, hitPos.x, hitPos.y, hitPos.z, 1, 0, 0, 0, 0);
                }
            }
        }

        isShooting = true;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }


    private void spawnGunshotEffect(Level level, Vec3 position, Entity entity) {
        if (!level.isClientSide) { // Ensure it's only sent from the server
            ModMessages.sendToAll(new ParticleEffectPacket(position, entity));
        }
    }

    private void applyRecoil(Player player) {
        float recoilAmount = 2.5f + player.getRandom().nextFloat() * 1.5f; // Random between 2.5f and 4.0f
        recoilPitch += recoilAmount; // Accumulate recoil
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
            private Glock17Gen4Renderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new Glock17Gen4Renderer();
                }
                return this.renderer;
            }

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
//
//        Player player = (Player) entity;
//        Level level = player.level();
//
//        if (level.isClientSide) {
//            if (bulletsUsed < 17) {
//                performShotSound(level, player);
//            } else {
//
//            }
//        }
//        if (!level.isClientSide) {
//            if (bulletsUsed < (BULLETS - 1)) {
//                performHitscan(level, player);
//                player.getCooldowns().addCooldown(this, 3);
//                applyRecoil(player);
//                bulletsUsed++;
//                triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "flash_controller", "gunflash");
//
//            }
//            //Last Shot handler
//            else if (bulletsUsed == 16) {
//                performHitscan(level, player);
//                applyRecoil(player);
//                triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerLevel) level), "flash_controller", "gunflash");
//                bulletsUsed++;
//            } else {
//                bulletsUsed = 0;
//                player.getCooldowns().addCooldown(this, 20);
//            }// Short cooldown (adjust if needed)
//
//
//        }
        return false; // Prevents melee swing animation when holding the gun
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "flash_controller", state -> {
            AnimationController<?> controller = state.getController();

            // If the animation has finished or isn't playing, ensure "idle" is looping
            if (controller.hasAnimationFinished() || controller.getCurrentAnimation() == null) {
                controller.setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }

            return PlayState.CONTINUE;
        }).triggerableAnim("gunflash", DefaultAnimations.ITEM_ON_USE).triggerableAnim("reload",RawAnimation.begin().then("reload", Animation.LoopType.PLAY_ONCE)));
    }
    private static void playAnimation(Player player, String animationName ){
        if(player.level().isClientSide){
                if (player instanceof AbstractClientPlayer clientPlayer) {
                    var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                            .getPlayerAssociatedData(clientPlayer)
                            .get(new ResourceLocation(MODID, "animationglock"));

                    if (animation != null) {
                        animation.setAnimation(new KeyframeAnimationPlayer(
                                PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))).setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL).setFirstPersonConfiguration(new FirstPersonConfiguration().setShowRightArm(true).setShowLeftArm(true)));
                    }

                }
            sendAnimationToServer(player, animationName, "animationglock" );
        }
    }
    private static void sendAnimationToServer(Player player, String animationName, String animationType) {
            ModMessages.sendToServer(new SyncPlayerAnimationPacket(player.getUUID(), animationName, animationType));
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}