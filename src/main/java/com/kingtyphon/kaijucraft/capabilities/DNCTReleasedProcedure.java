package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class DNCTReleasedProcedure {
    public DNCTReleasedProcedure(){

    }
    public static void execute(Entity entity) {
        if (entity != null) {
            boolean _setval = false;
            entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, (Direction)null).ifPresent((capability) -> {
                capability.setRunningwall(_setval);
                capability.syncWallrunVariables(entity);
            });
        }
    }
}