package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.common.commands.*;
import com.kingtyphon.kaijucraft.client.effects.PowerPlayerEffect;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.guns.Cannon;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import com.kingtyphon.kaijucraft.item.melee.BattleAxeItem;
import com.kingtyphon.kaijucraft.item.melee.TwinSwordItem;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import com.kingtyphon.kaijucraft.networking.packets.SyncPlayerAnimationPacket;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;
import static com.kingtyphon.kaijucraft.KaijuCraft.queueServerWork;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final int SATURATION_INTERVAL = 100; // 100 ticks = 5 seconds
    private static final Map<UUID, Integer> playerTickCounters = new HashMap<>();
    private static long lastJumpTime = 0; // Tracks the last time the player jumped
    private static final int COOLDOWN_TICKS = 20;
    private static long wallRunStartTime = 0; // Tracks when the wall run starts
    private static final int MAX_WALL_RUN_TIME = 100;
    private static final Map<UUID, Kaiju_no8Entity> kaijuEntities = new HashMap<>();
    private static String currentAnimation = "";
    private static final String TWINSWORDANIM = "melee";
    private static final String BATTLEAXEANIM = "axe";
    private static final String CANNON = "cannon";
    private static final String GLOCK = "glock";
    private static final String SIGSAUER = "sigsauer";
    private static final Map<UUID, Boolean> emoteStates = new HashMap<>();
    private static void startEmote(Player player) {
        UUID playerId = player.getUUID();
        setPlayerEmoting(playerId, true);

    }
    public static boolean isPlayerEmoting(UUID playerId) {
        return emoteStates.getOrDefault(playerId, false);
    }

    public static void setPlayerEmoting(UUID playerId, boolean isEmoting) {
        emoteStates.put(playerId, isEmoting);
    }

    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        //Commands Registry
        new GetLevelCommand(event.getDispatcher());
        new SetLevelCommand(event.getDispatcher());
        new SetSkillPointCommand(event.getDispatcher());
        new SetRandomizerFlagCommand(event.getDispatcher());
        new SetStatCommand(event.getDispatcher());
        new SetXPCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());}
    @SubscribeEvent
    public static void onEntityKilled(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            event.getSource().getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
                kaiju.getMaxXp();
                // Check if the entity is one of your custom Kaiju entities
                int xpGain = 0;
                if (event.getEntity() instanceof Kaiju_no8Entity) {
                    xpGain = 500; // Example XP value
                } else if (event.getEntity().getType() == EntityInit.TRICHONEPHILA.get()) {
                    xpGain = 50;
                }  else if (event.getEntity().getType() == EntityInit.PRIMIGENIUS.get()) {
                    xpGain = 40;
                } else if (event.getEntity().getType() == EntityInit.PHANEROPLUS.get()) {
                    xpGain = 20;
                } else if (event.getEntity() instanceof ServerPlayer ) {
                    if(KaijuHelper.isHigherLevel((ServerPlayer)event.getEntity(), (ServerPlayer) event.getSource().getEntity() )){
                        xpGain = 25;
                    }else {
                    xpGain = 5;
                    }
                } else {
                     xpGain = 1; // Default XP for normal mobs
                }

                // Add XP to the player and check for level-up
                kaiju.setXP((kaiju.getXP()+xpGain));

                // Send updated capability data to the client
                ModMessages.send(new KaijuPacket(kaiju.serializeNBT()), (ServerPlayer) player);
            });
        }
    }
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (!player.getCapability(KaijuProvider.KAIJU_CAPABILITY).isPresent()) { // Prevent duplicate attachment
                event.addCapability(new ResourceLocation(MODID, "kaiju_capability"), new KaijuProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();
        boolean isDeath = event.isWasDeath();

//        System.out.println("[DEBUG] onPlayerClone fired! Reason: " + (isDeath ? "Death" : "Dimension Change"));
        oldPlayer.reviveCaps();

        oldPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(oldCap -> {
//            System.out.println("[DEBUG] Old player capability found! Copying data...");

            newPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(newCap -> {
                newCap.deserializeNBT(oldCap.serializeNBT());
//                System.out.println("[DEBUG] Copied capability data to new player!");

                // Ensure the client gets the updated data
                KaijuCraft.queueServerWork(2, () -> {
                if (!newPlayer.level().isClientSide) {
                    ModMessages.send(new KaijuPacket(newCap.serializeNBT()), (ServerPlayer) newPlayer);
//                    System.out.println("[DEBUG] Sent updated capability data to client.");
                }});
            });

            // Log an error if newCap is missing
            if (!newPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).isPresent()) {
                System.out.println("[ERROR] New player capability NOT found!");
            }
        });
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            if (capability.isTransformed()) {
                event.setCanceled(true);

                Kaiju_no8Entity kaiju = capability.getKaijuEntity();
                if (kaiju != null) {
                    PoseStack poseStack = event.getPoseStack();
                    MultiBufferSource buffer = event.getMultiBufferSource();
                    float partialTick = event.getPartialTick();

                    // Render just the Kaiju arms/first-person layer here
                    // (you may need a separate geo model with only arms bones visible)
                    Minecraft.getInstance().getEntityRenderDispatcher().render(
                            kaiju, 0, -1.0, 0, 0, partialTick, poseStack, buffer, 15728880
                    );
                }
            }
        });
    }
    private static final Map<UUID, Kaiju_no8Entity> ACTIVE_KAIJU_ENTITIES = new HashMap<>();

    private static Kaiju_no8Entity getOrCreateKaiju(Player player) {
        return ACTIVE_KAIJU_ENTITIES.computeIfAbsent(player.getUUID(), id -> {
            Kaiju_no8Entity k = new Kaiju_no8Entity(EntityInit.KAIJU_NO8.get(), player.level());
            k.setNoAi(true);
            k.noPhysics = true;
            return k;
        });
    }
    @SubscribeEvent
    public static void onPlayerRender(RenderLivingEvent.Pre<Player, PlayerModel<Player>> event) {
        if (event.getEntity() instanceof Player player)

            player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(cap -> {
                if (!cap.isTransformed()) {
                    // If transformation is off, remove cached entity
                    ACTIVE_KAIJU_ENTITIES.remove(player.getUUID());
                    return;
                }

                event.setCanceled(true); // cancel vanilla rendering

                Minecraft mc = Minecraft.getInstance();

                // Reuse or create the Kaiju entity
                Kaiju_no8Entity kaiju = ACTIVE_KAIJU_ENTITIES.computeIfAbsent(player.getUUID(), id -> {
                    Kaiju_no8Entity k = new Kaiju_no8Entity(EntityInit.KAIJU_NO8.get(), mc.level);
                    k.setNoAi(true);
                    k.noPhysics = true;
                    return k;
                });

                // Sync Kaiju state with player
                kaiju.yBodyRot = player.yBodyRot;
                kaiju.yBodyRotO = player.yBodyRotO; // smooth rotation
//                // DO NOT set head rotation (fixes the “funky” glitch)

                // Drive animations if needed
                kaiju.setAnimationDriver(player);

                // Render using GeckoLib renderer
                EntityRenderer<? super Kaiju_no8Entity> renderer = mc.getEntityRenderDispatcher().getRenderer(kaiju);
                if (renderer instanceof GeoEntityRenderer geoRenderer) {
                    geoRenderer.render(
                            kaiju,
                            kaiju.yBodyRot,
                            event.getPartialTick(),
                            event.getPoseStack(),
                            event.getMultiBufferSource(),
                            event.getPackedLight()
                    );
                }
            });
    }



            private static void sendAnimationToServer(Player player, String animationName, String animationType) {
        if (player instanceof AbstractClientPlayer) {
            ModMessages.sendToServer(new SyncPlayerAnimationPacket(player.getUUID(), animationName, animationType));
        }
    }

    private static void playAnimation(Player player, String animationName, String animationType){
        if(player.level().isClientSide){
            if(animationName.equals("getgriddy")){
                setPlayerEmoting(player.getUUID(),true);
                queueServerWork(30, ()->{
                            setPlayerEmoting(player.getUUID(),false);
                        }
                );
            }
            sendAnimationToServer(player, animationName, "animation" + animationType);
        }
    }

    private static void stopAnimation(Player player, String animationType) {
        if (player.level().isClientSide) {
            sendAnimationToServer(player, "", "animation" + animationType);
        }
    }

    private static void playAnimation(Player player, String animationName) {
        if (player.level().isClientSide) {
            sendAnimationToServer(player, animationName, "animation");
        }
    }
        private static void wallJump(Player player) {
        Vec3 lookDirection = player.getLookAngle(); // Get the direction the player is looking
        Vec3 velocity = player.getDeltaMovement();

        // Apply a boost towards the direction they're looking
        Vec3 jumpBoost = lookDirection.normalize().scale(0.5).add(new Vec3(0, 0.6, 0)); // Jump force and upward component
        player.setDeltaMovement(velocity.add(jumpBoost));

    }

    private static boolean isJumping(Player player) {
        return player.getDeltaMovement().y > 0; // If the Y velocity is positive, the player is moving up
    }
    private static boolean isNearWall(Player player) {
        BlockPos playerPos = player.blockPosition();
        Level level = player.level();
        Direction facing = player.getDirection();

        // Check front blocks at multiple heights
        BlockPos front = playerPos.relative(facing);
        BlockPos frontAbove = front.above();
        BlockPos frontAbove2 = frontAbove.above();

        // Check left and right walls
        Direction leftDir = facing.getCounterClockWise();
        Direction rightDir = facing.getClockWise();

        BlockPos left = playerPos.relative(leftDir);
        BlockPos right = playerPos.relative(rightDir);

        return level.getBlockState(front).isSolid() ||
                level.getBlockState(frontAbove).isSolid() ||
                level.getBlockState(frontAbove2).isSolid() || // For higher walls
                level.getBlockState(left).isSolid() ||
                level.getBlockState(right).isSolid();
    }

    // Track the last jump time for each player
    private static final Map<UUID, Long> lastJumpTimes = new HashMap<>();
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        // Only apply if holding melee weapons (not ranged guns)
        ItemStack weapon = player.getMainHandItem();
        if (!(weapon.getItem() instanceof SwordItem || weapon.getItem() instanceof BattleAxeItem || weapon.getItem() instanceof TwinSwordItem || weapon.isEmpty())) {
            return;
        }

        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            boolean empowered = isWearingSpecialArmor(player);
            boolean transformed = kaiju.isTransformed() ;
            if(transformed){
                event.setAmount(event.getAmount() + 20 + kaiju.getLevel() * 1);
            }
            if (empowered && !transformed) {
                // Add flat bonus based on Kaiju level
                float bonus = (float) (kaiju.getLevel() * 0.25); // Example: +0.5 damage per level
                event.setAmount(event.getAmount() + bonus);
            }
        });
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase != TickEvent.Phase.END) return;


        Minecraft mc = Minecraft.getInstance();
        if (!(event.player instanceof ServerPlayer player)) {

            return;
        }
        // Access the Kaiju capability
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            // Check if the player has enough XP to level up
             UUID playerUUID = player.getUUID();
            UUID SPEED_MODIFIER     = UUID.fromString("11111111-1111-1111-1111-111111111111");
            UUID HEALTH_MODIFIER    = UUID.fromString("33333333-3333-3333-3333-333333333333");
            UUID KNOCKBACK_MODIFIER = UUID.fromString("44444444-4444-4444-4444-444444444444");
            UUID ARMOR_MODIFIER     = UUID.fromString("55555555-5555-5555-5555-555555555555");
            UUID TOUGHNESS_MODIFIER = UUID.fromString("66666666-6666-6666-6666-666666666666");
            if (kaiju.isTransformed()) {
                // Reuse or create Kaiju entity for rendering/logic
                if (kaiju.isTransformed()) {
                    Kaiju_no8Entity kaijuEntity = getOrCreateKaiju(player);

                    kaijuEntity.setPos(player.getX(), player.getY(), player.getZ());
                    kaijuEntity.yBodyRot = player.yBodyRot;
                    kaijuEntity.yBodyRotO = player.yBodyRotO; // smooth rotation
//                // DO NOT set head rotation (fixes the “funky” glitch)

                    kaijuEntity.tick();
                    kaijuEntity.tickCount++; // now this is safe, only increments once per game tick
                    kaijuEntity.copyPosition(player);
                    kaijuEntity.yBodyRot = player.yBodyRot;
                    kaijuEntity.yBodyRotO = player.yBodyRotO;

                    // Update animations if needed
                    kaijuEntity.setAnimationDriver(player);

                } else {
                    ACTIVE_KAIJU_ENTITIES.remove(player.getUUID());
                }
            }
            boolean transformed = kaiju.isTransformed();


            if (isWearingSpecialArmor(player) || transformed) {

                // Hunger regen
                int tickCount = playerTickCounters.getOrDefault(playerUUID, 0);
                if (tickCount >= SATURATION_INTERVAL) {
                    FoodData foodData = player.getFoodData();
                    if (foodData.getFoodLevel() < 20) {
                        foodData.eat(1, 0.5F);
                    }
                    playerTickCounters.put(playerUUID, 0);
                } else {
                    playerTickCounters.put(playerUUID, tickCount + 1);
                }

                // Speed boost (make this higher than before)
                AttributeInstance speedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speedAttr != null && speedAttr.getModifier(SPEED_MODIFIER) == null) {
                    double multiplier = kaiju.isTransformed() ? 0.25D : 0.15D;

                    speedAttr.addTransientModifier(
                            new AttributeModifier(SPEED_MODIFIER,
                                    "Kaiju speed boost",
                                    multiplier + (0.0001 * kaiju.getLevel()), // was 0.1, now faster
                                    AttributeModifier.Operation.ADDITION));
                }

                // Extra health
                AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttr != null && healthAttr.getModifier(HEALTH_MODIFIER) == null) {
                    double multiplier = kaiju.isTransformed() ? 60D : 40D;

                    healthAttr.addTransientModifier(
                            new AttributeModifier(HEALTH_MODIFIER,
                                    "Kaiju health boost",
                                    20D + (multiplier * kaiju.getLevel() / 100), // more base HP
                                    AttributeModifier.Operation.ADDITION));
                }

                // Knockback boost
                AttributeInstance knockbackAttr = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
                if (knockbackAttr != null && knockbackAttr.getModifier(KNOCKBACK_MODIFIER) == null) {
                    double multiplier = kaiju.isTransformed() ? 5.0 : 2.0;

                    knockbackAttr.addTransientModifier(
                            new AttributeModifier(KNOCKBACK_MODIFIER,
                                    "Kaiju knockback boost",
                                    multiplier, // stronger knockback
                                    AttributeModifier.Operation.ADDITION));
                }

                // Armor boost
                if(transformed) {
                    AttributeInstance armorAttr = player.getAttribute(Attributes.ARMOR);
                    if (armorAttr != null && armorAttr.getModifier(ARMOR_MODIFIER) == null) {

                        armorAttr.addTransientModifier(
                                new AttributeModifier(ARMOR_MODIFIER,
                                        "Kaiju armor boost",
                                        10D + (10D * kaiju.getLevel() / 100),
                                        AttributeModifier.Operation.ADDITION));
                    }
                }
                // Toughness boost
                AttributeInstance toughnessAttr = player.getAttribute(Attributes.ARMOR_TOUGHNESS);
                if (toughnessAttr != null && toughnessAttr.getModifier(TOUGHNESS_MODIFIER) == null) {
                    double multiplier = kaiju.isTransformed() ? 10.0 : 5.0;

                    toughnessAttr.addTransientModifier(
                            new AttributeModifier(TOUGHNESS_MODIFIER,
                                    "Kaiju toughness boost",
                                    5D + (multiplier * kaiju.getLevel() / 100),
                                    AttributeModifier.Operation.ADDITION));
                }

                if (transformed) {
                    int healInterval = 20; // every 40 ticks = 2 seconds
                    int healAmount = 4;    // half a heart per tick interval

                    int ticks = playerTickCounters.getOrDefault(playerUUID, 0);

                    if (ticks >= healInterval) {
                        if (player.getHealth() < player.getMaxHealth()) {
                            player.heal(healAmount); // heals directly
                        }
                        playerTickCounters.put(playerUUID, 0); // reset counter
                    } else {
                        playerTickCounters.put(playerUUID, ticks + 1);
                    }
                }
            } else {
                playerTickCounters.remove(playerUUID); // Reset if they remove armor

                // Remove modifiers when armor is removed
                removeModifier(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
                removeModifier(player, Attributes.MAX_HEALTH, HEALTH_MODIFIER);
                removeModifier(player, Attributes.ATTACK_KNOCKBACK, KNOCKBACK_MODIFIER);
                removeModifier(player, Attributes.ARMOR, ARMOR_MODIFIER);
                removeModifier(player, Attributes.ARMOR_TOUGHNESS, TOUGHNESS_MODIFIER);


            }
            if (kaiju.getXP() >= kaiju.getMaxXp()) {
                // Level up the player
                kaiju.levelUp();
                // Sync the capability data with the client
                ModMessages.send(new KaijuPacket(kaiju), player);

                // Notify the player about the level up
                player.sendSystemMessage(Component.literal("You are now Level " + kaiju.getLevel()));
            }


        });
    }
        private static void removeModifier(LivingEntity player, Attribute attribute, UUID uuid) {
            AttributeInstance attr = player.getAttribute(attribute);
            if (attr != null && attr.getModifier(uuid) != null) {
                attr.removeModifier(uuid);
            }
        }



    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Player player = mc.player;
        Kaiju_no8Entity kaijuEntity = ACTIVE_KAIJU_ENTITIES.get(mc.player.getUUID());
        if (kaijuEntity != null) {
            kaijuEntity.tickCount++;

        }
        // Check if player is wearing the custom armor
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            ItemStack hand = player.getMainHandItem();

            if(isPlayerEmoting(player.getUUID())){
                playAnimation(player, "getgriddy","animation");
                return;}

            if (isWearingSpecialArmor(player)) {
                int kaijuLevel = kaiju.getLevel();
                boolean isWallRunning = kaiju.isRunningwall();
                int wallRunDuration = kaijuLevel * 10 + 40;
                FX fx = FXHelper.getFX(new ResourceLocation("photon:100percentmelee"));

                if(kaijuLevel >90){

                    new PowerPlayerEffect(fx, player.level(), player).start();
                }
                if(kaijuLevel >29){
                            if (isNearWall(player) && isJumping(player) && player.onGround() == false || wallRunStartTime >0 && player.onGround() == false && isNearWall(player) ){
                                // Player is jumping near a wall, start or continue the wall run
                                if (wallRunStartTime == 0 ) {
                                    wallRunStartTime = mc.level.getGameTime(); // Start wall run

                                    // Apply wall run effect (e.g., prevent falling, keep velocity)
                                    Vec3 direction = player.getLookAngle();
                                    Vec3 velocity = player.getDeltaMovement();
                                    player.setDeltaMovement(velocity.add(direction.x * 0.1, 0.0, direction.z * 0.1)); // Small horizontal boost
                                    player.hasImpulse = true;
                                    // Keep the player from falling

                                }if (player.getDeltaMovement().y < 0 && (mc.level.getGameTime() - wallRunStartTime) < wallRunDuration) {
                                    player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                                    Direction leftDir = player.getDirection().getCounterClockWise();
                                    Direction rightDir = player.getDirection().getClockWise();

                                    BlockPos leftWall = player.blockPosition().relative(leftDir);
                                    BlockPos rightWall = player.blockPosition().relative(rightDir);
                                    player.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(kapability ->{
                                      kapability.setRunningwall(true);
                                    });
                                    if (mc.level.getBlockState(leftWall).isSolid()) {
                                        playAnimation(player, "wallrunleft");
                                    } else if (mc.level.getBlockState(rightWall).isSolid()) {
                                        playAnimation(player, "wallrunright");
                                    }
                                    if (mc.options.keyJump.isDown()) {
                                        wallJump(player);
                                    }
                                }
                            }
                            // Stop the wall run if player stops jumping or loses wall contact
                            else {
                                player.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(kapability ->{
                                    kapability.setRunningwall(false);
                                });
                                wallRunStartTime = 0; // End wall run
                            }
                            }
                if(kaijuLevel > 4){
                    if (player.isCrouching() && mc.options.keyJump.isDown()) {
                        long currentTime = player.level().getGameTime(); // Get the current world time in ticks

                        if (currentTime - lastJumpTime >= COOLDOWN_TICKS) { // Check cooldown
                            player.setDeltaMovement(player.getDeltaMovement().add(0, 0.3 + (.005 * kaiju.getLevel()), 0)); // Apply vertical boost
                            lastJumpTime = currentTime; // Update last jump time
                        }
                    }if(!player.getOffhandItem().isEmpty() && !TwinSwordItem.isDualWielding(player)){
                        stopAnimation(player, CANNON);
                        stopAnimation(player, TWINSWORDANIM);
                        stopAnimation(player, BATTLEAXEANIM);
                        stopAnimation(player, GLOCK);
                        stopAnimation(player, SIGSAUER);
                        currentAnimation = "";
                        return;
                    }

                    //Cannon
                if(hand.getItem() instanceof Cannon){
                    String animationToPlay = "";
                    boolean isHoldingCannon = hand.getItem() instanceof Cannon;

                    if (player.onGround()) {
                        if (player.isSprinting()) {
                             // Sprint animation
                        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                            animationToPlay = "canon_walk"; // Walking animation (could be any other animation based on movement)
                        } else if (!player.isCrouching()) {
                            animationToPlay = "canon_idle"; // Idle animation (only if not moving)
                        }
                    } else if(!player.swinging && !kaiju.isRunningwall()){

                    }
                    // If not wall-running and not holding twin swords, stop animation ONLY if an animation is playing
                    if (!isWallRunning && !isHoldingCannon && !currentAnimation.isEmpty() && !player.swinging ) {
                        stopAnimation(player, CANNON);
                        currentAnimation = ""; // Reset current animation state
                    }
                    if(isWallRunning && isHoldingCannon && !player.onGround() && !player.swinging){
                        stopAnimation(player, CANNON);
                        currentAnimation = ""; // Reset current animation state
                    }

                    // Play the animation only if the state has changed and the animation is not empty
                    if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("") ) {
                        playAnimation(player, animationToPlay , CANNON);
                        currentAnimation = animationToPlay;
                    }
                }
                else if (TwinSwordItem.isDualWielding(player)) {
                boolean isHoldingTwinSwords = TwinSwordItem.isDualWielding(player);

                String animationToPlay = "";

                // Determine animation based on player state
                if (player.onGround()) {
                    if (player.isSprinting()) {
                        animationToPlay = "sprint_ts"; // Sprint animation
                    } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                        animationToPlay = "walk_ts"; // Walking animation (could be any other animation based on movement)
                    } else if (!player.isCrouching()) {
                        animationToPlay = "idle_ts"; // Idle animation (only if not moving)
                    }
                } else if(!player.swinging && isWallRunning){
                    // If airborne and holding twin swords, play the jump animation
                    if (isHoldingTwinSwords) {
                        stopAnimation(player, TWINSWORDANIM);
                    }
                }

                // If not wall-running and not holding twin swords, stop animation ONLY if an animation is playing
                if (!isWallRunning && !isHoldingTwinSwords && !currentAnimation.isEmpty() && !player.swinging ) {
                    stopAnimation(player, TWINSWORDANIM);
                    currentAnimation = ""; // Reset current animation state
                }
                if(isWallRunning && isHoldingTwinSwords && !player.onGround() && !player.swinging){
                    stopAnimation(player, TWINSWORDANIM);
                }

                // Play the animation only if the state has changed and the animation is not empty
                if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("") ) {
                    playAnimation(player, animationToPlay , TWINSWORDANIM);
                    currentAnimation = animationToPlay; // Update the current state
                }
                }


                //Battle Axe
                else if(hand.getItem() instanceof BattleAxeItem){
                    boolean isHoldingAxe = hand.getItem() instanceof BattleAxeItem;
                    if( ((BattleAxeItem) hand.getItem()).getStartedSlam()|| ((BattleAxeItem) hand.getItem()).getFinalSlam()){return;}
                    String animationToPlay = "";
                    if(!player.swinging){
                    if (player.onGround()) {
                        if (player.isSprinting()) {
                            animationToPlay = "sprint_ba"; // Sprint animation
                        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                            animationToPlay = "walk_ba"; // Walking animation (could be any other animation based on movement)
                        } else if (!player.isCrouching()) {
                            animationToPlay = "idle_ba"; // Idle animation (only if not moving)
                        }
                    } else if( !isWallRunning && !player.swinging){
                        // If airborne and holding twin swords, play the jump animation
                        if (hand.getItem() instanceof BattleAxeItem && !((BattleAxeItem) hand.getItem()).getStartedSlam()) {
                            animationToPlay = "jump"; // Jump animation while in the air
                        }
                    }}
                    // If not wall-running and not holding twin swords, stop animation ONLY if an animation is playing
                    if (!isWallRunning && !isHoldingAxe && !currentAnimation.isEmpty()) {
                        stopAnimation(player, BATTLEAXEANIM);
                        currentAnimation = ""; // Reset current animation state
                    }
                    if(isWallRunning && isHoldingAxe && !player.onGround()){
                        stopAnimation(player, BATTLEAXEANIM);
                    }

                    // Play the animation only if the state has changed and the animation is not empty
                    if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("")) {
                        playAnimation(player,animationToPlay, BATTLEAXEANIM);
                        currentAnimation = animationToPlay; // Update the current state
                    }

                }

                //Sig Sauer
                else if(hand.getItem() instanceof SigSauerShortRifleItem){
                    //animation logic for Glock
                    String animationToPlay = "";
                    boolean isHoldingSig = hand.getItem() instanceof SigSauerShortRifleItem;
                    if (player.onGround()) {
                        if (player.isSprinting()) {
                            animationToPlay = "sprint_sigsauer"; // Walking animation (could be any other animation based on movement)

                        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                            animationToPlay = "walk_sigsauer"; // Walking animation (could be any other animation based on movement)
                        } else if (!player.isCrouching()) {
                            animationToPlay = "idle_sigsauer"; // Idle animation (only if not moving)
                        }
                    }
                    if (!isWallRunning && !isHoldingSig && !currentAnimation.isEmpty()) {
                        stopAnimation(player, SIGSAUER);
                        currentAnimation = ""; // Reset current animation state
                    }
                    if(isWallRunning && isHoldingSig && !player.onGround()){
                        stopAnimation(player, SIGSAUER);
                    }
                    if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("") ) {
                        playAnimation(player, animationToPlay, SIGSAUER);
                        currentAnimation = animationToPlay;
                    }
                }

                //GLOck
                else if(hand.getItem() instanceof Glock17Gen4){
                    //animation logic for Glock
                    String animationToPlay = "";
                    Glock17Gen4 weapon = (Glock17Gen4) hand.getItem();
                    if( ((Glock17Gen4) hand.getItem()).isShooting() || player.isUsingItem()){
                        animationToPlay = "shooting";
                        return;
                    }
                    boolean isHoldingGlock = hand.getItem() instanceof Glock17Gen4;
                    if (player.onGround()) {
                        if (player.isSprinting()) {
                            animationToPlay = "sprint_glock"; // Walking animation (could be any other animation based on movement)

                        } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                            animationToPlay = "walk_glock"; // Walking animation (could be any other animation based on movement)
                        } else if (!player.isCrouching()) {
                            animationToPlay = "idle_glock"; // Idle animation (only if not moving)
                        }
                    }else if( !isWallRunning){
                        animationToPlay = "jump"; // Jump animation while in the air
                    }
                    if (!isWallRunning && !isHoldingGlock && !currentAnimation.isEmpty() ) {
                        stopAnimation(player, GLOCK);
                        currentAnimation = ""; // Reset current animation state
                    }
                    if(isWallRunning && isHoldingGlock && !player.onGround()){
                        stopAnimation(player, GLOCK);
                    }
                    if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("") ) {
                        playAnimation(player, animationToPlay , GLOCK);
                        currentAnimation = animationToPlay;
                    }
                }else{
                    stopAnimation(player, CANNON);
                    stopAnimation(player, TWINSWORDANIM);
                    stopAnimation(player, BATTLEAXEANIM);
                    stopAnimation(player, GLOCK);
                    stopAnimation(player, SIGSAUER);
                    currentAnimation = ""; // Reset current animation state
                }

            // Check if the player is holding shift and pressing jump

            }
        }else{
                //Glock 17
                if(hand.getItem() instanceof Glock17Gen4){
                //animation logic for Glock
                    if (kaiju.isReloading() || kaiju.isShooting()) {
                        // Prevent normal animations during reloading or shooting
                        String animationToPlay = "";
                        return;
                    }
                String animationToPlay = "";
                if (player.onGround()) {
                    if (player.isSprinting()) {
                        animationToPlay = "sprint_glock"; // Walking animation (could be any other animation based on movement)

                    } else if (player.getDeltaMovement().horizontalDistanceSqr() > 0.001) {
                        animationToPlay = "walk_glock"; // Walking animation (could be any other animation based on movement)
                    } else if (!player.isCrouching()) {
                        animationToPlay = "idle_glock"; // Idle animation (only if not moving)
                    }
                }
                    Glock17Gen4 weapon = (Glock17Gen4) hand.getItem();
                    if( ((Glock17Gen4) hand.getItem()).isShooting() || player.isUsingItem()){
                        currentAnimation = "";
                        return;
                    }

                if (!animationToPlay.equals(currentAnimation) && !animationToPlay.isEmpty() && !animationToPlay.equals("") ) {
                    playAnimation(player, animationToPlay , GLOCK);
                    currentAnimation = animationToPlay;
                }
            }else{
                //stop animations if they are not with the glock in hand
                    if(!currentAnimation.equalsIgnoreCase("")){
                stopAnimation(player,GLOCK);
                currentAnimation = "";
                    }
            } //end of wearing armor
        }
        });
    }

    public static boolean isWearingSpecialArmor(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET); // Change slot if needed
        return !chest.isEmpty() && chest.getItem() == ItemInit.MCOMBAT_CHESTPLATE.get() && !legs.isEmpty() && legs.getItem() == ItemInit.MCOMBAT_LEGGINGS.get() && !feet.isEmpty() && feet.getItem() == ItemInit.MCOMBAT_BOOTS.get();
    }
    @SubscribeEvent
    public static void onFallDamage(LivingFallEvent event) {
         if (event.getEntity() instanceof Player player) {
            // Example: fall damage reduction based on player level
            if (isWearingSpecialArmor(player)) {

                event.setCanceled(true);
            }
        }}
            @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Sync capability data with client
        event.getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            if(capability.getLevel() == 0){
                capability.setMaxXp(100);
            }
            CompoundTag nbt = capability.serializeNBT();
            ModMessages.send(new KaijuPacket(nbt), (ServerPlayer) event.getEntity());
        });

    }
    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof Glock17Gen4 gunItem) {
            Level level = player.level();
            if (!level.isClientSide) {
                gunItem.handleSwingShot(level, player, stack);
            }
        }
    }
}
