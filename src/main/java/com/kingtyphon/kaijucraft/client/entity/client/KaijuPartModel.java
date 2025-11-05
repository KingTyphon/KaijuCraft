package com.kingtyphon.kaijucraft.client.entity.client;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class KaijuPartModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "kaijuparts_converted"), "main");
	private final ModelPart bone;

	public KaijuPartModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 122).addBox(-12.5F, -18.5F, -12.5F, 25.0F, 18.55F, 25.0F, new CubeDeformation(0.0F))
		.texOffs(0, 61).addBox(-19.5F, -15.325F, -19.5F, 26.0F, 15.35F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(143, 0).addBox(-23.0F, -5.8F, -24.0F, 26.0F, 5.775F, 25.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-7.75F, -9.625F, -22.5F, 25.0F, 9.675F, 46.0F, new CubeDeformation(0.0F))
		.texOffs(101, 169).addBox(-24.0F, -7.375F, -2.4F, 22.0F, 7.375F, 22.0F, new CubeDeformation(0.0F))
		.texOffs(101, 122).addBox(-10.8F, -12.55F, -15.5F, 33.0F, 12.55F, 24.0F, new CubeDeformation(0.0F))
		.texOffs(125, 61).addBox(2.0F, -6.725F, -10.5F, 22.0F, 6.775F, 28.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}