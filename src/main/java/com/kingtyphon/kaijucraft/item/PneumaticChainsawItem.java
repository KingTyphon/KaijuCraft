package com.kingtyphon.kaijucraft.item;

import com.kingtyphon.kaijucraft.entity.kaiju.KaijuPartEntity;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kingtyphon.kaijucraft.event.ModEvents.setPlayerEmoting;

public class PneumaticChainsawItem extends Item {

    private static final AtomicInteger resetDelay = new AtomicInteger(0);
    private static final int RESET_DELAY_TICKS = 40; // Delay in ticks (20 ticks = 1 second)
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Keeps it running while holding the button
    }
    public PneumaticChainsawItem(Properties properties) {
        super(properties);
    }
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean isOn = stack.getOrCreateTag().getBoolean("on");

        if (!isOn) { // Prevents sound spam
            stack.getOrCreateTag().putBoolean("on", true); // Activate chainsaw

            if (world.isClientSide) {
                // Play looping sound on the client side
                RandomSource random = world.getRandom();
                SimpleSoundInstance soundInstance = new SimpleSoundInstance(
                        KaijuSounds.CHAINSAW_ON.get().getLocation(),       // Sound event
                        SoundSource.PLAYERS,                  // Sound source
                        10.0F,                                 // Volume
                        1.0F,                                 // Pitch
                        random,true, 0, SoundInstance.Attenuation.LINEAR,// Random source
                        player.getX(), player.getY(), player.getZ(), false  // Position
                );

                Minecraft.getInstance().getSoundManager().play(soundInstance);
            }
        }

        // **Get the direction the player is facing**
        Vec3 direction = player.getViewVector(1.0F); // Gets the forward direction
        BlockPos playerPos = player.blockPosition();
        // **Calculate the search range in front of the player**
        double range = 1.0; // Set the range to 1 block ahead of the player
        AABB searchBox = new AABB(playerPos.offset((int) (direction.x * range), (int) (direction.y * range), (int) (direction.z * range)))
                .inflate(1.0); // Inflates slightly in all directions

        // **Detect KaijuPartEntities within the search box**
        List<Entity> entities = world.getEntities(player, searchBox, entity -> entity instanceof KaijuPartEntity);

        if (!entities.isEmpty()) {
            if (!world.isClientSide) {
                // For each KaijuPartEntity detected
                for (Entity entity : entities) {
                    if (entity instanceof KaijuPartEntity kaijuPart) {
                        // Kill the KaijuPartEntity
                        kaijuPart.kill();
                        // Give Kaiju Muscle item to the player
                        player.addItem(new ItemStack(ItemInit.KAIJU_MUSCLE.get()));
                    }
                }
            }
        }

        player.startUsingItem(hand); // Keeps item active while holding
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player)) return;

        boolean isOn = stack.getOrCreateTag().getBoolean("on");

        if (isOn) {
            stack.getOrCreateTag().putBoolean("on", false); // Deactivate when released

            if (world.isClientSide) {
                // Stop the looping sound
                Minecraft.getInstance().getSoundManager().stop(KaijuSounds.CHAINSAW_ON.get().getLocation(), SoundSource.PLAYERS);
            }
        }
    }

}