package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.commands.*;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.networking.KaijuHelper;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
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
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;
import org.joml.Vector3d;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(KaijuCraft.MODID, "kaiju_capability"), new KaijuProvider());
            }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.getEntity().level().isClientSide) {  // Ensure this runs on the server side
            event.getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
                CompoundTag nbt = capability.serializeNBT();
                ModMessages.send(new KaijuPacket(nbt), (ServerPlayer) event.getEntity());
            });
        }
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
    private static boolean isJumping(Player player) {
        return player.getDeltaMovement().y > 0; // If the Y velocity is positive, the player is moving up
    }
    private static boolean isNearWall(Player player) {
        BlockPos playerPos = player.blockPosition();
        Level level = player.level();

        // Check blocks in front and to the sides of the player to detect walls
        // Check if a block exists on the left or right side, and above
        BlockPos front = playerPos.offset(player.getDirection().getNormal().getX(), 0, player.getDirection().getNormal().getZ());
        BlockPos left = playerPos.offset(player.getDirection().getNormal().getX(), 0, player.getDirection().getNormal().getZ()).below();
        BlockPos right = playerPos.offset(player.getDirection().getNormal().getX(), 0, player.getDirection().getNormal().getZ()).below();
        return level.getBlockState(front).isSolid() || level.getBlockState(left).isSolid() || level.getBlockState(right).isSolid();
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (!(event.player instanceof ServerPlayer player)) return;
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

            int kaijuLevel = kaiju.getLevel();
            int wallRunDuration = kaijuLevel * 10 + 40; // Duration increases with Kaiju level

            // Check if player is against a wall and jumping
            if (isNearWall(player) && isJumping(player) && player.onGround() == false) {
                // Player is jumping near a wall, start or continue the wall run
                if (wallRunStartTime == 0 || (mc.level.getGameTime() - wallRunStartTime) < wallRunDuration) {
                    wallRunStartTime = mc.level.getGameTime(); // Start wall run

                    // Apply wall run effect (e.g., prevent falling, keep velocity)
                    Vec3 direction = player.getLookAngle();
                    Vec3 velocity = player.getDeltaMovement();
                    player.setDeltaMovement(velocity.add(direction.x * 0.1, 0.0, direction.z * 0.1)); // Small horizontal boost

                    // Keep the player from falling
                    if (player.getDeltaMovement().y < 0) {
                        player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
                    }
                }
            }
            // Stop the wall run if player stops jumping or loses wall contact
            else {
                wallRunStartTime = 0; // End wall run
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
            // Check if the player is holding shift and pressing jump
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
    private static boolean isWearingSpecialArmor(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET); // Change slot if needed
        return !chest.isEmpty() && chest.getItem() == ItemInit.MCOMBAT_CHESTPLATE.get() && !legs.isEmpty() && legs.getItem() == ItemInit.MCOMBAT_LEGGINGS.get() && !feet.isEmpty() && feet.getItem() == ItemInit.MCOMBAT_BOOTS.get();
    }

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
