package com.kingtyphon.kaijucraft.capabilities;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID)
public class WallrunProcedure {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Entity entity = event.player;
            if (entity instanceof Player) {
                executeWallrun(entity, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ());
            }
        }
    }

    public static void executeWallrun(Entity entity, LevelAccessor world, double x, double y, double z) {
        if (entity != null) {
            // Checking if auto-sprint is enabled
            if (isAutoSprintEnabled(entity)) {
                enableAutoSprint(entity);
            }

            if (isWallRunning(entity)) {
                handleWallrun(entity, world, x, y, z);
            }
        }
    }

    public static boolean isAutoSprintEnabled(Entity entity) {
        return entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, Direction.NORTH)
                .map(capability -> capability.getAutoSprint())
                .orElse(false);
    }

    public static void enableAutoSprint(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isSprinting()) {
                player.setSprinting(true);
            }
        }
    }

    public static boolean isWallRunning(Entity entity) {
        return entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, Direction.NORTH)
                .map(capability -> capability.getWallrunning() > 0)
                .orElse(false);
    }

    public static void handleWallrun(Entity entity, LevelAccessor world, double x, double y, double z) {
        // Check surrounding blocks to simulate wall-running logic
        if (checkWallCollision(world, x, y, z)) {
            // Apply movement based on wall-running
            Vec3 newVelocity = new Vec3(
                    Math.sin(Math.toRadians(entity.getYRot() + 180.0F)) * 0.3,
                    Math.max(entity.getDeltaMovement().y + 0.13, 0.08), // Ensures upward force prevents falling
                    Math.cos(Math.toRadians(entity.getYRot())) * 0.3
            );
            entity.setDeltaMovement(newVelocity);
        }
    }

    public static boolean checkWallCollision(LevelAccessor world, double x, double y, double z) {
        BlockPos[] adjacentPositions = {
                new BlockPos((int)(x + 0.6),(int) y,(int) z),
                new BlockPos((int)(x - 0.6), (int)y, (int)z),
                new BlockPos((int)x, (int)y, (int)(z + 0.6)),
                new BlockPos((int)x,(int) y, (int)(z - 0.6))
        };

        for (BlockPos pos : adjacentPositions) {
            if (world.getBlockState(pos).isSolid()) {
                return true;
            }
        }

        return false;
    }
}