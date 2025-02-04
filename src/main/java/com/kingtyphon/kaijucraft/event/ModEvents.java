package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.commands.*;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static long lastJumpTime = 0; // Tracks the last time the player jumped
    private static final int COOLDOWN_TICKS = 20;
    private static long wallRunStartTime = 0; // Tracks when the wall run starts
    private static final int MAX_WALL_RUN_TIME = 100;




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
    public static void onPlayerRender(RenderPlayerEvent event){
        event.getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(kapability ->{
            boolean isTransformed = kapability.isTransformed();
            if(isTransformed == true){
            event.setCanceled(isTransformed);

            }
        });
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
            float extraDamage = kaiju.getLevel() * .1F; // Example: 2 damage per Kaiju level
            player.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(extraDamage);
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

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        Player player = mc.player;
        // Check if player is wearing the custom armor
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
        if (isWearingSpecialArmor(player)) {
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
