package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class AutoSprintOnKeyPressedProcedure{
    public AutoSprintOnKeyPressedProcedure() {
        }
    public static void execute(Entity entity) {
        if (entity != null) {
            boolean _setval;

            // Accessing KaijuCapability for the entity
            IKaijuCapability capability =  entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, Direction.NORTH).orElse(new KaijuCapability());

            // Check and toggle autoSprint value
            if (!capability.getAutoSprint()) {
                _setval = true;
                capability.setAutoSprint(_setval);
                capability.syncWallrunVariables(entity); // Sync with the player
            } else {
                _setval = false;
                capability.setAutoSprint(_setval);
                capability.syncWallrunVariables(entity); // Sync with the player
            }
        }
    }
}

