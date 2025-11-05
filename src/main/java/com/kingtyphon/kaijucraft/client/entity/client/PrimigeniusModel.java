package com.kingtyphon.kaijucraft.client.entity.client;// Made with Blockbench 4.12.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.kingtyphon.kaijucraft.entity.animations.PrimigeniusAnimations;

import com.kingtyphon.kaijucraft.entity.kaiju.PrimigeniusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PrimigeniusModel<T extends Entity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	//public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KaijuCraft.MODID, "primigenius"), "main");
	private final ModelPart Yoju;
	private final ModelPart UpperBody;
	private final ModelPart Head;
	private final ModelPart Skull;
	private final ModelPart Jaw;
	private final ModelPart Tounge;
	private final ModelPart T1;
	private final ModelPart Kneck;
	private final ModelPart Body;
	private final ModelPart Upper;
	private final ModelPart Core;
	private final ModelPart Groin;
	private final ModelPart RightArm;
	private final ModelPart Bicep;
	private final ModelPart Forearm;
	private final ModelPart Hand;
	private final ModelPart Thumb;
	private final ModelPart Finger;
	private final ModelPart Finger1;
	private final ModelPart Finger2;
	private final ModelPart LeftArm;
	private final ModelPart Bicep2;
	private final ModelPart Forearm2;
	private final ModelPart Hand2;
	private final ModelPart Thumb2;
	private final ModelPart Finger3;
	private final ModelPart Finger4;
	private final ModelPart Finger5;
	private final ModelPart Lower;
	private final ModelPart RightLeg;
	private final ModelPart LeftLeg;
	private final ModelPart Tail;
	private final ModelPart Seg;
	private final ModelPart Seg2;
	private final ModelPart Seg3;
	private final ModelPart Seg4;
	private final ModelPart Seg5;
	private final ModelPart Seg6;

	public PrimigeniusModel(ModelPart root) {
		this.Yoju = root.getChild("Yoju");
		this.UpperBody = this.Yoju.getChild("UpperBody");
		this.Head = this.UpperBody.getChild("Head");
		this.Skull = this.Head.getChild("Skull");
		this.Jaw = this.Head.getChild("Jaw");
		this.Tounge = this.Jaw.getChild("Tounge");
		this.T1 = this.Tounge.getChild("T1");
		this.Kneck = this.Head.getChild("Kneck");
		this.Body = this.UpperBody.getChild("Body");
		this.Upper = this.Body.getChild("Upper");
		this.Core = this.Body.getChild("Core");
		this.Groin = this.Body.getChild("Groin");
		this.RightArm = this.UpperBody.getChild("RightArm");
		this.Bicep = this.RightArm.getChild("Bicep");
		this.Forearm = this.RightArm.getChild("Forearm");
		this.Hand = this.Forearm.getChild("Hand");
		this.Thumb = this.Hand.getChild("Thumb");
		this.Finger = this.Hand.getChild("Finger");
		this.Finger1 = this.Hand.getChild("Finger1");
		this.Finger2 = this.Hand.getChild("Finger2");
		this.LeftArm = this.UpperBody.getChild("LeftArm");
		this.Bicep2 = this.LeftArm.getChild("Bicep2");
		this.Forearm2 = this.LeftArm.getChild("Forearm2");
		this.Hand2 = this.Forearm2.getChild("Hand2");
		this.Thumb2 = this.Hand2.getChild("Thumb2");
		this.Finger3 = this.Hand2.getChild("Finger3");
		this.Finger4 = this.Hand2.getChild("Finger4");
		this.Finger5 = this.Hand2.getChild("Finger5");
		this.Lower = this.Yoju.getChild("Lower");
		this.RightLeg = this.Lower.getChild("RightLeg");
		this.LeftLeg = this.Lower.getChild("LeftLeg");
		this.Tail = this.Lower.getChild("Tail");
		this.Seg = this.Tail.getChild("Seg");
		this.Seg2 = this.Tail.getChild("Seg2");
		this.Seg3 = this.Tail.getChild("Seg3");
		this.Seg4 = this.Tail.getChild("Seg4");
		this.Seg5 = this.Tail.getChild("Seg5");
		this.Seg6 = this.Tail.getChild("Seg6");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Yoju = partdefinition.addOrReplaceChild("Yoju", CubeListBuilder.create(), PartPose.offset(-17.75F, -31.25F, -16.25F));

		PartDefinition UpperBody = Yoju.addOrReplaceChild("UpperBody", CubeListBuilder.create(), PartPose.offset(17.75F, 23.25F, 31.25F));

		PartDefinition Head = UpperBody.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -23.7798F, -36.3726F, 0.0873F, 0.0F, 0.0F));

		PartDefinition Skull = Head.addOrReplaceChild("Skull", CubeListBuilder.create(), PartPose.offset(3.0F, 9.7266F, -30.5956F));

		PartDefinition cube_r1 = Skull.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(154, 127).addBox(-14.5F, -31.0F, -1.65F, 23.0F, 16.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Skull.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(294, 38).addBox(-11.5F, -6.1444F, -6.8881F, 23.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -24.9853F, 31.2988F, -1.0908F, 0.0F, 0.0F));

		PartDefinition cube_r3 = Skull.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(294, 16).addBox(-11.5F, -12.675F, 0.0F, 23.0F, 20.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -23.2302F, 19.774F, -0.6109F, 0.0F, 0.0F));

		PartDefinition cube_r4 = Skull.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 270).addBox(-11.5F, -5.0F, -9.5F, 23.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -29.024F, 35.8049F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r5 = Skull.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(381, 12).mirror().addBox(-11.5F, 11.0F, -3.5F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(381, 12).addBox(11.5F, 11.0F, -3.5F, 0.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(85, 91).addBox(-11.5F, -5.0F, -9.5F, 23.0F, 16.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -28.1952F, 16.823F, 0.0436F, 0.0F, 0.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.2968F, -21.7383F, 0.0436F, 0.0F, 0.0F));

		PartDefinition cube_r6 = Jaw.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(95, 50).addBox(-10.0F, -5.0F, -10.5F, 20.0F, 10.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.074F, -1.953F, 0.1745F, 0.0F, 0.0F));

		PartDefinition cube_r7 = Jaw.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(234, 181).addBox(-7.5F, 3.25F, 0.25F, 15.0F, 7.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.707F, -16.7977F, 0.7418F, 0.0F, 0.0F));

		PartDefinition Tounge = Jaw.addOrReplaceChild("Tounge", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r8 = Tounge.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(427, 13).addBox(-10.0F, -5.15F, -1.5F, 20.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0741F, -1.953F, 0.1745F, 0.0F, 0.0F));

		PartDefinition T1 = Tounge.addOrReplaceChild("T1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.333F, -4.3576F));

		PartDefinition cube_r9 = T1.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(430, 24).addBox(-10.0F, -5.15F, -10.5F, 20.0F, 0.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.7411F, 2.4046F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Kneck = Head.addOrReplaceChild("Kneck", CubeListBuilder.create(), PartPose.offset(0.0F, -2.2392F, -25.6989F));

		PartDefinition cube_r10 = Kneck.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 129).addBox(-8.5F, -15.0F, 6.0F, 17.0F, 17.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

		PartDefinition Body = UpperBody.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Upper = Body.addOrReplaceChild("Upper", CubeListBuilder.create(), PartPose.offset(0.0F, -22.3501F, -30.5538F));

		PartDefinition cube_r11 = Upper.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(57, 224).addBox(0.0F, -7.4003F, -1.3447F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.25F, -9.5164F, 14.4462F, -0.7903F, -0.1794F, -0.4539F));

		PartDefinition cube_r12 = Upper.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(57, 224).mirror().addBox(0.0F, -7.4003F, -1.3447F, 0.0F, 12.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(11.25F, -9.5164F, 14.4462F, -0.7903F, 0.1794F, 0.4539F));

		PartDefinition cube_r13 = Upper.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 203).mirror().addBox(0.0F, -6.7231F, -21.9734F, 0.0F, 12.0F, 23.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.25F, -9.5164F, 14.4462F, -0.4235F, -0.233F, -0.4733F));

		PartDefinition cube_r14 = Upper.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 203).addBox(0.0F, -6.7231F, -21.9734F, 0.0F, 12.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.25F, -9.5164F, 14.4462F, -0.4235F, 0.233F, 0.4733F));

		PartDefinition cube_r15 = Upper.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-17.5F, -10.5F, -10.0F, 35.0F, 26.0F, 23.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.48F, 0.0F, 0.0F));

		PartDefinition cube_r16 = Upper.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(154, 159).addBox(-13.5F, -9.3F, -17.0F, 27.0F, 19.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition Core = Body.addOrReplaceChild("Core", CubeListBuilder.create(), PartPose.offset(0.0F, -13.7826F, -16.0175F));

		PartDefinition cube_r17 = Core.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(228, 250).mirror().addBox(0.0F, -5.5F, -6.0F, 0.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-10.25F, -13.3564F, 8.866F, -0.4636F, -0.2527F, -0.4636F));

		PartDefinition cube_r18 = Core.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(228, 250).addBox(0.0F, -5.5F, -6.0F, 0.0F, 13.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.25F, -13.3564F, 8.866F, -0.4636F, 0.2527F, 0.4636F));

		PartDefinition cube_r19 = Core.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 91).addBox(-14.5F, -9.5F, -6.0F, 29.0F, 24.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition Groin = Body.addOrReplaceChild("Groin", CubeListBuilder.create(), PartPose.offset(0.0F, -3.3053F, -6.7836F));

		PartDefinition cube_r20 = Groin.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 50).addBox(-12.5F, -8.5F, -13.0F, 25.0F, 18.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition RightArm = UpperBody.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-17.75F, -23.25F, -31.25F, 0.0F, 0.0F, -0.1309F));

		PartDefinition Bicep = RightArm.addOrReplaceChild("Bicep", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -1.016F));

		PartDefinition cube_r21 = Bicep.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(117, 0).addBox(-16.25F, -14.0F, -8.0F, 17.0F, 29.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2218F, 5.6849F, 0.0F, -0.225F, 0.0294F, 0.1276F));

		PartDefinition Forearm = RightArm.addOrReplaceChild("Forearm", CubeListBuilder.create(), PartPose.offset(-9.5478F, 18.7683F, -5.25F));

		PartDefinition cube_r22 = Forearm.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(170, 82).addBox(-5.5F, -16.0F, 3.25F, 14.0F, 25.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8701F, 1.825F, -15.2293F, -0.7486F, 0.0294F, 0.1276F));

		PartDefinition Hand = Forearm.addOrReplaceChild("Hand", CubeListBuilder.create(), PartPose.offset(-2.7146F, 15.9663F, -14.4793F));

		PartDefinition cube_r23 = Hand.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(231, 105).addBox(-4.5F, 7.0F, 4.25F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1555F, -14.1413F, -0.75F, -0.7486F, 0.0294F, 0.1276F));

		PartDefinition Thumb = Hand.addOrReplaceChild("Thumb", CubeListBuilder.create(), PartPose.offset(0.6352F, 1.2373F, -8.1376F));

		PartDefinition cube_r24 = Thumb.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(70, 250).addBox(4.75F, -4.25F, -2.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.3729F, 2.439F, -1.75F, -0.529F, -0.0597F, 0.0316F));

		PartDefinition Finger = Hand.addOrReplaceChild("Finger", CubeListBuilder.create(), PartPose.offset(-4.9809F, 1.7587F, -7.6376F));

		PartDefinition cube_r25 = Finger.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(245, 22).addBox(-1.5F, -4.5F, -1.25F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7568F, 1.9176F, -2.25F, -0.7181F, -0.2354F, -0.1649F));

		PartDefinition Finger1 = Hand.addOrReplaceChild("Finger1", CubeListBuilder.create(), PartPose.offset(-6.4809F, 4.5087F, -4.8877F));

		PartDefinition cube_r26 = Finger1.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(201, 253).addBox(-1.5F, -4.5F, 3.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7432F, -0.8324F, -5.0F, -0.7181F, -0.2354F, -0.1649F));

		PartDefinition Finger2 = Hand.addOrReplaceChild("Finger2", CubeListBuilder.create(), PartPose.offset(-6.2309F, 7.5087F, -1.6127F));

		PartDefinition cube_r27 = Finger2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(214, 253).addBox(-1.5F, -4.5F, 7.25F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4932F, -3.8324F, -8.275F, -0.7181F, -0.2354F, -0.1649F));

		PartDefinition LeftArm = UpperBody.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(17.75F, -23.25F, -31.25F, 0.0F, 0.0F, 0.1309F));

		PartDefinition Bicep2 = LeftArm.addOrReplaceChild("Bicep2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -1.016F));

		PartDefinition cube_r28 = Bicep2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(117, 0).mirror().addBox(-0.75F, -14.0F, -8.0F, 17.0F, 29.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.2218F, 5.6849F, 0.0F, -0.225F, -0.0294F, -0.1276F));

		PartDefinition Forearm2 = LeftArm.addOrReplaceChild("Forearm2", CubeListBuilder.create(), PartPose.offset(9.5478F, 18.7683F, -5.25F));

		PartDefinition cube_r29 = Forearm2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(170, 82).mirror().addBox(-8.5F, -16.0F, 3.25F, 14.0F, 25.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.8701F, 1.825F, -15.2293F, -0.7486F, -0.0294F, -0.1276F));

		PartDefinition Hand2 = Forearm2.addOrReplaceChild("Hand2", CubeListBuilder.create(), PartPose.offset(2.7146F, 15.9663F, -14.4793F));

		PartDefinition cube_r30 = Hand2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(231, 105).mirror().addBox(-7.5F, 7.0F, 4.25F, 12.0F, 11.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1555F, -14.1413F, -0.75F, -0.7486F, -0.0294F, -0.1276F));

		PartDefinition Thumb2 = Hand2.addOrReplaceChild("Thumb2", CubeListBuilder.create(), PartPose.offset(-0.6352F, 1.2373F, -8.1376F));

		PartDefinition cube_r31 = Thumb2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(70, 250).mirror().addBox(-7.75F, -4.25F, -2.5F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.3729F, 2.439F, -1.75F, -0.529F, 0.0597F, -0.0316F));

		PartDefinition Finger3 = Hand2.addOrReplaceChild("Finger3", CubeListBuilder.create(), PartPose.offset(4.9809F, 1.7587F, -7.6376F));

		PartDefinition cube_r32 = Finger3.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(245, 22).mirror().addBox(-1.5F, -4.5F, -1.25F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.7568F, 1.9176F, -2.25F, -0.7181F, 0.2354F, 0.1649F));

		PartDefinition Finger4 = Hand2.addOrReplaceChild("Finger4", CubeListBuilder.create(), PartPose.offset(6.4809F, 4.5087F, -4.8877F));

		PartDefinition cube_r33 = Finger4.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(201, 253).mirror().addBox(-1.5F, -4.5F, 3.0F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7432F, -0.8324F, -5.0F, -0.7181F, 0.2354F, 0.1649F));

		PartDefinition Finger5 = Hand2.addOrReplaceChild("Finger5", CubeListBuilder.create(), PartPose.offset(6.2309F, 7.5087F, -1.6127F));

		PartDefinition cube_r34 = Finger5.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(214, 253).mirror().addBox(-1.5F, -4.5F, 7.25F, 3.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4932F, -3.8324F, -8.275F, -0.7181F, 0.2354F, 0.1649F));

		PartDefinition Lower = Yoju.addOrReplaceChild("Lower", CubeListBuilder.create(), PartPose.offset(17.75F, 55.25F, 16.25F));

		PartDefinition RightLeg = Lower.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(241, 54).mirror().addBox(-12.5F, 29.0F, -17.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, -36.0F, 14.0F));

		PartDefinition cube_r35 = RightLeg.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 239).mirror().addBox(-4.5F, -7.0F, -3.0F, 9.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, 15.4688F, -10.1817F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r36 = RightLeg.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(124, 174).mirror().addBox(-5.0F, -14.0F, -5.0F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(234, 198).mirror().addBox(-6.0F, -9.0F, -6.0F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, 32.0F, -9.975F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r37 = RightLeg.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(228, 222).mirror().addBox(-6.5F, -20.275F, -1.0F, 13.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, 10.3606F, -10.4954F, -1.0559F, 0.0F, 0.0F));

		PartDefinition cube_r38 = RightLeg.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(186, 36).mirror().addBox(-7.0F, -19.525F, -6.0F, 14.0F, 25.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-8.0F, 10.3606F, -10.4954F, -0.7069F, 0.0F, 0.0F));

		PartDefinition LeftLeg = Lower.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(241, 54).addBox(2.5F, 29.0F, -17.0F, 10.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, -36.0F, 14.0F));

		PartDefinition cube_r39 = LeftLeg.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(0, 239).addBox(-4.5F, -7.0F, -3.0F, 9.0F, 14.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 15.4688F, -10.1817F, 0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r40 = LeftLeg.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(124, 174).addBox(-3.0F, -14.0F, -5.0F, 8.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(234, 198).addBox(-4.0F, -9.0F, -6.0F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 32.0F, -9.975F, -0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r41 = LeftLeg.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(228, 222).addBox(-6.5F, -20.275F, -1.0F, 13.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 10.3606F, -10.4954F, -1.0559F, 0.0F, 0.0F));

		PartDefinition cube_r42 = LeftLeg.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(186, 36).addBox(-7.0F, -19.525F, -6.0F, 14.0F, 25.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 10.3606F, -10.4954F, -0.7069F, 0.0F, 0.0F));

		PartDefinition Tail = Lower.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(0.0F, -32.5F, 11.0F));

		PartDefinition Seg = Tail.addOrReplaceChild("Seg", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition cube_r43 = Seg.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(186, 0).addBox(-5.5F, -7.5F, -12.0F, 11.0F, 17.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 1.0F, -1.1345F, 0.0F, 0.0F));

		PartDefinition cube_r44 = Seg.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(0, 167).addBox(-7.5F, -9.5521F, -6.3618F, 15.0F, 17.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.175F, 2.0F, -0.3491F, 0.0F, 0.0F));

		PartDefinition Seg2 = Tail.addOrReplaceChild("Seg2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition cube_r45 = Seg2.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(179, 190).addBox(-6.5F, -7.5F, -13.0F, 13.0F, 17.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.425F, 23.75F, -0.1745F, 0.0F, 0.0F));

		PartDefinition Seg3 = Tail.addOrReplaceChild("Seg3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition cube_r46 = Seg3.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(179, 222).addBox(-5.5F, -7.5F, -10.0F, 11.0F, 17.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.8F, 33.35F, -0.0436F, 0.0F, 0.0F));

		PartDefinition Seg4 = Tail.addOrReplaceChild("Seg4", CubeListBuilder.create().texOffs(227, 75).addBox(-4.5F, -2.45F, 34.6F, 9.0F, 16.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition Seg5 = Tail.addOrReplaceChild("Seg5", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition cube_r47 = Seg5.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(231, 153).addBox(-3.5F, -7.5F, -10.0F, 7.0F, 14.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.925F, 56.75F, 0.0175F, 0.0F, 0.0F));

		PartDefinition Seg6 = Tail.addOrReplaceChild("Seg6", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0197F, 0.0164F));

		PartDefinition cube_r48 = Seg6.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(139, 229).addBox(-2.5F, -7.5F, -10.0F, 5.0F, 11.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.675F, 65.25F, 0.0611F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);
		this.animate(((PrimigeniusEntity) entity).roarAnimationState, PrimigeniusAnimations.roar, ageInTicks, 1f);
		this.animateWalk(PrimigeniusAnimations.walk, limbSwing, limbSwingAmount, 1.5f,1f);
		this.animate(((PrimigeniusEntity) entity).idleAnimationState, PrimigeniusAnimations.idle, ageInTicks, 1f);
		this.animate(((PrimigeniusEntity) entity).attackAnimationState, PrimigeniusAnimations.sweepattack, ageInTicks, 1f);
	}

	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Yoju.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
	@Override
	public ModelPart root() {
		return Yoju;
	}
}