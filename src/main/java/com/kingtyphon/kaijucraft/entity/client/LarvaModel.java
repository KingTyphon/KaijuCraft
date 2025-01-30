package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class LarvaModel extends GeoModel<LarvaEntity> {
    @Override
    public ResourceLocation getModelResource(LarvaEntity entity) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/larva.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LarvaEntity entity) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/larva.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LarvaEntity entity) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/larva.json");
    }
    @Override
    public void setCustomAnimations(LarvaEntity animatable, long instanceId, AnimationState<LarvaEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
