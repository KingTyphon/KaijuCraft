package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class MobilityKeybindOnKeyReleasedProcedure {
    public MobilityKeybindOnKeyReleasedProcedure() {
    }

    public static void execute(Entity entity) {
        if (entity != null) {
            entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction) null).ifPresent(capability -> {
                capability.setRunningwall(false);
                capability.syncWallrunVariables(entity);
            });
        }
    }
}
