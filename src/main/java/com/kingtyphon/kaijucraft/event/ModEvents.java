package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.commands.*;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.melee.TwinSwordItem;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;
import static java.awt.Color.green;
import static java.awt.Color.red;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static long lastJumpTime = 0; // Tracks the last time the player jumped
    private static final int COOLDOWN_TICKS = 20;
    private static long wallRunStartTime = 0; // Tracks when the wall run starts
    private static final int MAX_WALL_RUN_TIME = 100;
    private static final Map<UUID, Kaiju_no8Entity> kaijuEntities = new HashMap<>();
    private static String currentAnimation = "";


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

        System.out.println("[DEBUG] onPlayerClone fired! Reason: " + (isDeath ? "Death" : "Dimension Change"));
        oldPlayer.reviveCaps();

        oldPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(oldCap -> {
            System.out.println("[DEBUG] Old player capability found! Copying data...");

            newPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(newCap -> {
                newCap.deserializeNBT(oldCap.serializeNBT());
                System.out.println("[DEBUG] Copied capability data to new player!");

                // Ensure the client gets the updated data
                KaijuCraft.queueServerWork(2, () -> {
                if (!newPlayer.level().isClientSide) {
                    ModMessages.send(new KaijuPacket(newCap.serializeNBT()), (ServerPlayer) newPlayer);
                    System.out.println("[DEBUG] Sent updated capability data to client.");
                }});
            });

            // Log an error if newCap is missing
            if (!newPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).isPresent()) {
                System.out.println("[ERROR] New player capability NOT found!");
            }
        });
    }


    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
//        Player player = event.getEntity();
//        player.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(capability -> {
//            if (capability.isTransformed()) {
//                event.setCanceled(true);
//
//                Minecraft mc = Minecraft.getInstance();
//                EntityType<Kaiju_no8Entity> kaijuType = EntityInit.KAIJU_NO8.get();
//                Kaiju_no8Entity kaijuNo8 = new Kaiju_no8Entity(kaijuType,mc.level);
//
//
//                EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
//                kaijuNo8.copyPosition(player);
//                PoseStack poseStack = event.getPoseStack();
//                MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();
//
//                poseStack.pushPose();
//
//                dispatcher.render(kaijuNo8, 0, 0, 0, 0, event.getPartialTick(), poseStack, bufferSource, 15728880);
//
//                poseStack.popPose();
//            }
//        });
    }



    // Helper method to play the animation
    private static void playAnimation(Player player, String animationName) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess
                    .getPlayerAssociatedData(clientPlayer)
                    .get(new ResourceLocation(MODID, "animation"));

            if (animation != null) {
                animation.setAnimation(new KeyframeAnimationPlayer(
                        PlayerAnimationRegistry.getAnimation(new ResourceLocation(MODID, animationName))
                ));
            }
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
    private static void transformToKaiju(Player player) {
        Kaiju_no8Entity kaiju = new Kaiju_no8Entity(EntityInit.KAIJU_NO8.get(), player.level());
        kaiju.copyPosition(player);
        player.level().addFreshEntity(kaiju);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (!(event.player instanceof ServerPlayer player)) {

            // Check i
            return;
        }


        // Access the Kaiju capability
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            // Check if the player has enough XP to level up
            float extraDamage = kaiju.getLevel() * .1F; // Example: 2 damage per Kaiju level

            UUID SPEED_MODIFIER = UUID.fromString("11111111-1111-1111-1111-111111111111");
            UUID DAMAGE_MODIFIER = UUID.fromString("22222222-2222-2222-2222-222222222222");
            UUID HEALTH_MODIFIER = UUID.fromString("33333333-3333-3333-3333-333333333333");
            UUID KNOCKBACK_MODIFIER = UUID.fromString("44444444-4444-4444-4444-444444444444");

            if (isWearingSpecialArmor(player)) {
                AttributeInstance speedAttr = player.getAttribute(Attributes.MOVEMENT_SPEED);
                if (speedAttr != null && speedAttr.getModifier(SPEED_MODIFIER) == null) {
                    speedAttr.addTransientModifier(new AttributeModifier(SPEED_MODIFIER, "Armor speed boost", 0.1 + (0.0005 * kaiju.getLevel()), AttributeModifier.Operation.ADDITION));
                }

                AttributeInstance attackAttr = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attackAttr != null && attackAttr.getModifier(DAMAGE_MODIFIER) == null) {
                    attackAttr.addTransientModifier(new AttributeModifier(DAMAGE_MODIFIER, "Armor attack boost", extraDamage, AttributeModifier.Operation.ADDITION));
                }

                AttributeInstance healthAttr = player.getAttribute(Attributes.MAX_HEALTH);
                if (healthAttr != null && healthAttr.getModifier(HEALTH_MODIFIER) == null) {
                    healthAttr.addTransientModifier(new AttributeModifier(HEALTH_MODIFIER, "Armor health boost", 20 + (40 * kaiju.getLevel() / 100), AttributeModifier.Operation.ADDITION));
                }

                AttributeInstance knockbackAttr = player.getAttribute(Attributes.ATTACK_KNOCKBACK);
                if (knockbackAttr != null && knockbackAttr.getModifier(KNOCKBACK_MODIFIER) == null) {
                    knockbackAttr.addTransientModifier(new AttributeModifier(KNOCKBACK_MODIFIER, "Armor knockback boost", 2.0D, AttributeModifier.Operation.ADDITION));
                }
            } else {
                // Remove modifiers when armor is removed
                removeModifier(player, Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
                removeModifier(player, Attributes.ATTACK_DAMAGE, DAMAGE_MODIFIER);
                removeModifier(player, Attributes.MAX_HEALTH, HEALTH_MODIFIER);
                removeModifier(player, Attributes.ATTACK_KNOCKBACK, KNOCKBACK_MODIFIER);
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
        // Check if player is wearing the custom armor
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            if (isWearingSpecialArmor(player)) {
                if (TwinSwordItem.isDualWielding(player)) {
                    String animationToPlay = "";

                    if (player.isSprinting()) {
                        animationToPlay = "sprint_ts";
                    } else {
                        animationToPlay = "idle_ts";
                    }

                    // Play the animation only if the state has changed and prevent immediate re-trigger
                    if (!animationToPlay.equals(currentAnimation)) {
                        playAnimation(player, animationToPlay);
                        currentAnimation = animationToPlay; // Update the current state
                    } else if (animationToPlay.equals("walk_ts") && currentAnimation.equals("walk_ts")) {
                        // Ensure the walk animation isn't played continuously
                        if (player.getDeltaMovement().lengthSqr() > 0.0001) {
                            playAnimation(player, "walk_ts");
                        }
                    }
                }
            int kaijuLevel = kaiju.getLevel();
            int wallRunDuration = kaijuLevel * 10 + 40;
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
                wallRunStartTime = 0; // End wall run
            }
            }
            // Check if the player is holding shift and pressing jump
            if(kaijuLevel > 4)
            if (player.isCrouching() && mc.options.keyJump.isDown()) {
                long currentTime = player.level().getGameTime(); // Get the current world time in ticks

                if (currentTime - lastJumpTime >= COOLDOWN_TICKS) { // Check cooldown
                    player.setDeltaMovement(player.getDeltaMovement().add(0, 0.3 + (.005 * kaiju.getLevel()), 0)); // Apply vertical boost
                    lastJumpTime = currentTime; // Update last jump time
                }
            }
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
}
