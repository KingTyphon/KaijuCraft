package com.kingtyphon.kaijucraft.common.capabilities;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;


public class DashOnKeyPressedProcedure {
    public static void execute(LevelAccessor world, Entity entity) {
        if (entity == null) return;

        entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction) null).ifPresent(capability -> {
            if (!capability.isDash() && entity.onGround()) {
                capability.setDash(true);
                capability.syncWallrunVariables(entity);

                Vec3 movement = entity.getDeltaMovement();
                entity.setDeltaMovement(new Vec3(
                        (movement.x * 7.0 - movement.x * 6.0) * 8.0,
                        movement.y + 0.5,
                        (movement.z * 7.0 - movement.z * 6.0) * 8.0
                ));

                KaijuCraft.queueServerWork(25, () -> {
                    entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction) null)
                            .ifPresent(cap -> {
                                cap.setDash(false);
                                cap.syncWallrunVariables(entity);
                            });
                });
            }
        });
    }
}