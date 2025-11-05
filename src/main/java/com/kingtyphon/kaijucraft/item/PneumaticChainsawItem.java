package com.kingtyphon.kaijucraft.item;

import com.kingtyphon.kaijucraft.entity.kaiju.KaijuPartEntity;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
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

        if (!isOn) { // first time activating
            stack.getOrCreateTag().putBoolean("on", true);

            if (world.isClientSide) {
                RandomSource random = world.getRandom();
                SimpleSoundInstance soundInstance = new SimpleSoundInstance(
                        KaijuSounds.CHAINSAW_ON.get().getLocation(),
                        SoundSource.PLAYERS,
                        10.0F,
                        1.0F,
                        random, true, 0, SoundInstance.Attenuation.LINEAR,
                        player.getX(), player.getY(), player.getZ(), false
                );
                Minecraft.getInstance().getSoundManager().play(soundInstance);
            }
        }

        // tells MC we’re holding right click, so onUseTick will start being called
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void onUseTick(Level world, LivingEntity user, ItemStack stack, int count) {
        if (!(user instanceof Player player)) return;
        if (world.isClientSide) return;

        // Look direction
        Vec3 direction = player.getViewVector(1.0F);
        BlockPos playerPos = player.blockPosition();
        double range = 1.0;

        AABB searchBox = new AABB(
                playerPos.offset((int)(direction.x * range),
                        (int)(direction.y * range),
                        (int)(direction.z * range))
        ).inflate(1.0);

        List<Entity> entities = world.getEntities(player, searchBox, e -> e instanceof KaijuPartEntity);

        for (Entity entity : entities) {
            if (entity instanceof KaijuPartEntity kaijuPart) {
                int grindProgress = kaijuPart.getPersistentData().getInt("GrindProgress");
                grindProgress++;

                if (grindProgress >= 20) { // 1 second at 20tps
                    kaijuPart.reduceHitPoints(player);
                    grindProgress = 0;
                }

                kaijuPart.getPersistentData().putInt("GrindProgress", grindProgress);
            }
        }
    }


    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean isSelected) {
        if (!(entity instanceof Player player)) return;
        if (world.isClientSide) return;

        CompoundTag tag = stack.getOrCreateTag();
        boolean isOn = tag.getBoolean("on");

        if (!isOn || !isSelected) return; // Only attack if item is held and turned on

        if (player.tickCount % 10 == 0) { // Every 10 ticks = 0.5s

            Vec3 direction = player.getViewVector(1.0F);
            BlockPos playerPos = player.blockPosition();
            double range = 2.0;

            AABB searchBox = new AABB(playerPos.offset(
                    (int)(direction.x * range),
                    (int)(direction.y * range),
                    (int)(direction.z * range)))
                    .inflate(1.0);

            List<Entity> targets = world.getEntitiesOfClass(Entity.class, searchBox, e ->
                    !(e instanceof KaijuPartEntity) &&
                            e != player &&
                            e.isAlive());
            for (Entity target : targets) {
                target.hurt(player.damageSources().genericKill(), 4.0F); // Example damage
            }
        }
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