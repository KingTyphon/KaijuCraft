package com.kingtyphon.kaijucraft.client;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;

public class BodyHeadSyncModifier extends AbstractModifier {
    private static final String HEAD = "head";
    private static final String LEFT_ARM = "leftArm";
    private static final String RIGHT_ARM = "rightArm";
    private static final String BODY = "body";

    private Vec3f lastHeadRotation = new Vec3f(0, 0, 0);

    @Override
    public void setupAnim(float tickDelta) {
        super.setupAnim(tickDelta);
    }

    @Override
    public @NotNull Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f original) {
        if (modelName.equals(HEAD)) {
            lastHeadRotation = original;
            return original;
        }

        // Apply part of head rotation to arms
        if (modelName.equals(BODY) ) {
            // Example: Apply only X-axis (pitch) of head to arms
            float pitchInfluence = 0.5f; // 50% of the head pitch applied
            return new Vec3f(
                    original.getX() + lastHeadRotation.getX() * pitchInfluence,
                    original.getY(),
                    original.getZ()
            );
        }

        return original;
    }
}