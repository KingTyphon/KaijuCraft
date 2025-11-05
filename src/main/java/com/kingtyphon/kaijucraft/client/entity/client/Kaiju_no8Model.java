package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.animations.Kaiju_no8Animations;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;


public class Kaiju_no8Model extends GeoModel<Kaiju_no8Entity> {

	@Override
	public ResourceLocation getModelResource(Kaiju_no8Entity kaijuNo8Entity) {
		return new ResourceLocation(KaijuCraft.MODID, "geo/kaiju_no8.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Kaiju_no8Entity kaijuNo8Entity) {
		return new ResourceLocation(KaijuCraft.MODID, "textures/entity/kaiju_no8.png");
	}

	@Override
	public ResourceLocation getAnimationResource(Kaiju_no8Entity kaijuNo8Entity) {
		return new ResourceLocation(KaijuCraft.MODID, "animations/kaiju_no8.json");
	}
//	@Override
//	public void setCustomAnimations(Kaiju_no8Entity animatable, long instanceId, AnimationState<Kaiju_no8Entity> animationState) {
//		CoreGeoBone head = getAnimationProcessor().getBone("head");
//
//		if (head != null) {
//			EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
//
//			head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
//			head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
//		}
//	}
}