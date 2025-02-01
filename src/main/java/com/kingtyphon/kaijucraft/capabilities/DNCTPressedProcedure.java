package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class DNCTPressedProcedure {

    public DNCTPressedProcedure() {}

    public static void execute(Entity entity) {
        if (entity != null) {
            boolean _setval = true;

            // Accessing KaijuCapability for the entity
            IKaijuCapability capability = entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, Direction.NORTH).orElse(new KaijuCapability());

            // Setting the runningwall state
            capability.setRunningwall(_setval);
            capability.syncWallrunVariables(entity); // Sync with the player
        }
    }
}