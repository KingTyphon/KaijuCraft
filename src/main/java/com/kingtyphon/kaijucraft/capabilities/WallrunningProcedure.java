package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class WallrunningProcedure {
    public WallrunningProcedure() {
    }

    public static boolean execute(Entity entity) {
        if (entity == null) {
            return false;
        } else {
            return entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction) null)
                    .map(cap -> cap.getWallrunning() < 100.0) // Compare the stored value
                    .orElse(false);       }
    }
}
