package com.kingtyphon.kaijucraft.common.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class MobilityKeybindOnKeyPressedProcedure {
    public MobilityKeybindOnKeyPressedProcedure() {
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity != null) {
            entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction) null).ifPresent(capability -> {
                double wallside = capability.getWallside(); // Assuming you have a getter for `wallside`

                if (wallside == 1.0 && world.getBlockState(BlockPos.containing(x + 0.6, y, z)).isSolid()) {
                    entity.setDeltaMovement(new Vec3(-0.4, 0.5, entity.getDeltaMovement().z()));
                }

                if (wallside == 2.0 && world.getBlockState(BlockPos.containing(x - 0.6, y, z)).isSolid()) {
                    entity.setDeltaMovement(new Vec3(0.4, 0.5, entity.getDeltaMovement().z()));
                }

                if (wallside == 3.0 && world.getBlockState(BlockPos.containing(x, y, z - 0.6)).isSolid()) {
                    entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), 0.5, 0.4));
                }

                if (wallside == 4.0 && world.getBlockState(BlockPos.containing(x, y, z + 0.6)).isSolid()) {
                    entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x(), 0.5, -0.4));
                }
            });
        }
    }
}