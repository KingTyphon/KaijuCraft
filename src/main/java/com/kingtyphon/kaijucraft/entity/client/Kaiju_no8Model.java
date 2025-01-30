package com.kingtyphon.kaijucraft.entity.client;

import com.kingtyphon.kaijucraft.entity.animations.Kaiju_no8Animations;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
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


public class Kaiju_no8Model<T extends Entity> extends HierarchicalModel<T> {

	private final ModelPart Kafka;
	private final ModelPart Head;
	private final ModelPart Skull;
	private final ModelPart UpperHead;
	private final ModelPart TopTeeth;
	private final ModelPart Jaw;
	private final ModelPart BottomTeeth;
	private final ModelPart Tongue;
	private final ModelPart T2;
	private final ModelPart Kneck;
	private final ModelPart Body;
	private final ModelPart LowerBody;
	private final ModelPart UpperBody;
	private final ModelPart RightArm;
	private final ModelPart Shoulder;
	private final ModelPart Arm;
	private final ModelPart Bicep;
	private final ModelPart Forearm;
	private final ModelPart Hand;
	private final ModelPart LeftArm;
	private final ModelPart Shoulder2;
	private final ModelPart Arm2;
	private final ModelPart Bicep2;
	private final ModelPart Forearm2;
	private final ModelPart Hand2;
	private final ModelPart RightLeg;
	private final ModelPart Thigh;
	private final ModelPart LowerLeg;
	private final ModelPart Shin;
	private final ModelPart Foot;
	private final ModelPart LeftLeg;
	private final ModelPart Thigh2;
	private final ModelPart LowerLeg2;
	private final ModelPart Shin2;
	private final ModelPart Foot2;

	public Kaiju_no8Model(ModelPart root) {

		this.Kafka = root.getChild("Kafka");
		this.Head = this.Kafka.getChild("Head");
		this.Skull = this.Head.getChild("Skull");
		this.UpperHead = this.Skull.getChild("UpperHead");
		this.TopTeeth = this.UpperHead.getChild("TopTeeth");
		this.Jaw = this.Skull.getChild("Jaw");
		this.BottomTeeth = this.Jaw.getChild("BottomTeeth");
		this.Tongue = this.Jaw.getChild("Tongue");
		this.T2 = this.Tongue.getChild("T2");
		this.Kneck = this.Head.getChild("Kneck");
		this.Body = this.Kafka.getChild("Body");
		this.LowerBody = this.Body.getChild("LowerBody");
		this.UpperBody = this.Body.getChild("UpperBody");
		this.RightArm = this.Kafka.getChild("RightArm");
		this.Shoulder = this.RightArm.getChild("Shoulder");
		this.Arm = this.RightArm.getChild("Arm");
		this.Bicep = this.Arm.getChild("Bicep");
		this.Forearm = this.Arm.getChild("Forearm");
		this.Hand = this.Forearm.getChild("Hand");
		this.LeftArm = this.Kafka.getChild("LeftArm");
		this.Shoulder2 = this.LeftArm.getChild("Shoulder2");
		this.Arm2 = this.LeftArm.getChild("Arm2");
		this.Bicep2 = this.Arm2.getChild("Bicep2");
		this.Forearm2 = this.Arm2.getChild("Forearm2");
		this.Hand2 = this.Forearm2.getChild("Hand2");
		this.RightLeg = this.Kafka.getChild("RightLeg");
		this.Thigh = this.RightLeg.getChild("Thigh");
		this.LowerLeg = this.RightLeg.getChild("LowerLeg");
		this.Shin = this.LowerLeg.getChild("Shin");
		this.Foot = this.LowerLeg.getChild("Foot");
		this.LeftLeg = this.Kafka.getChild("LeftLeg");
		this.Thigh2 = this.LeftLeg.getChild("Thigh2");
		this.LowerLeg2 = this.LeftLeg.getChild("LowerLeg2");
		this.Shin2 = this.LowerLeg2.getChild("Shin2");
		this.Foot2 = this.LowerLeg2.getChild("Foot2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Kafka = partdefinition.addOrReplaceChild("Kafka", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition Head = Kafka.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition Skull = Head.addOrReplaceChild("Skull", CubeListBuilder.create(), PartPose.offset(0.0F, -6.5F, 0.75F));

		PartDefinition UpperHead = Skull.addOrReplaceChild("UpperHead", CubeListBuilder.create().texOffs(28, 0).addBox(-3.5F, -4.8401F, -6.2738F, 7.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(26, 29).addBox(-2.5F, -5.2651F, -5.9488F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 29).addBox(-3.0F, -0.8401F, -6.0238F, 6.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.1599F, 0.7738F));

		PartDefinition Head_r1 = UpperHead.addOrReplaceChild("Head_r1", CubeListBuilder.create().texOffs(10, 95).addBox(-1.5F, -0.5F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.95F, -4.8874F, -5.2641F, -0.2279F, 0.1039F, 0.595F));

		PartDefinition Head_r2 = UpperHead.addOrReplaceChild("Head_r2", CubeListBuilder.create().texOffs(22, 95).addBox(-1.3F, -1.925F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.15F, -5.0874F, -5.0891F, -0.6278F, -0.0239F, 0.4179F));

		PartDefinition Head_r3 = UpperHead.addOrReplaceChild("Head_r3", CubeListBuilder.create().texOffs(16, 95).addBox(-0.9516F, -1.5816F, -0.8832F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.95F, -4.1874F, -5.0891F, -0.2279F, -0.1039F, -0.595F));

		PartDefinition Head_r4 = UpperHead.addOrReplaceChild("Head_r4", CubeListBuilder.create().texOffs(26, 95).addBox(-0.4313F, -2.9746F, -1.2833F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.95F, -4.1874F, -5.0891F, -0.6278F, 0.0239F, -0.4179F));

		PartDefinition Head_r5 = UpperHead.addOrReplaceChild("Head_r5", CubeListBuilder.create().texOffs(0, 114).addBox(0.0F, -5.0F, -2.0F, 0.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.7168F, -1.4225F, 1.5272F, 0.0F, 0.0F));

		PartDefinition Head_r6 = UpperHead.addOrReplaceChild("Head_r6", CubeListBuilder.create().texOffs(36, 88).addBox(3.0F, -5.0F, 1.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.1151F, 0.1262F, 0.1745F, 0.0F, 0.0F));

		PartDefinition TopTeeth = UpperHead.addOrReplaceChild("TopTeeth", CubeListBuilder.create().texOffs(42, 68).addBox(3.05F, -27.0F, -6.25F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(28, 68).addBox(-3.05F, -27.0F, -6.25F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(44, 91).addBox(-3.0F, -27.0F, -6.3F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 26.1599F, 0.2262F));

		PartDefinition Jaw = Skull.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(50, 29).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.25F));

		PartDefinition cube_r1 = Jaw.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(115, 119).addBox(-2.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 1.45F, -0.575F, 0.3491F, 0.0F, 0.0F));

		PartDefinition BottomTeeth = Jaw.addOrReplaceChild("BottomTeeth", CubeListBuilder.create().texOffs(14, 68).addBox(3.025F, -27.0F, -6.25F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 68).addBox(-3.025F, -27.0F, -6.25F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(44, 88).addBox(-3.0F, -27.0F, -6.275F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 25.0F, 4.25F));

		PartDefinition Tongue = Jaw.addOrReplaceChild("Tongue", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.0304F, -0.0324F, -1.0036F, 0.0F, 0.0F));

		PartDefinition cube_r2 = Tongue.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(34, 123).addBox(-1.0F, 0.0F, -0.5F, 2.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.25F, -0.35F, 0.7854F, 0.0F, 0.0F));

		PartDefinition T2 = Tongue.addOrReplaceChild("T2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.6071F, -0.7071F));

		PartDefinition cube_r3 = T2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 127).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.3571F, 0.3571F, 0.7854F, 0.0F, 0.0F));

		PartDefinition Kneck = Head.addOrReplaceChild("Kneck", CubeListBuilder.create().texOffs(76, 29).addBox(-2.5F, -5.93F, -3.6702F, 5.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.32F, 0.9202F));

		PartDefinition Head_r7 = Kneck.addOrReplaceChild("Head_r7", CubeListBuilder.create().texOffs(52, 14).addBox(-5.0F, -3.0F, -3.0F, 8.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -6.255F, 0.4298F, 0.1745F, 0.0F, 0.0F));

		PartDefinition Head_r8 = Kneck.addOrReplaceChild("Head_r8", CubeListBuilder.create().texOffs(0, 78).addBox(4.0F, -3.3F, -0.75F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(24, 14).addBox(-0.5F, -3.0F, -3.0F, 9.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -4.18F, 1.0798F, 0.0436F, 0.0F, 0.0F));

		PartDefinition Head_r9 = Kneck.addOrReplaceChild("Head_r9", CubeListBuilder.create().texOffs(56, 68).addBox(5.0F, -3.475F, -1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(56, 0).addBox(0.0F, -3.0F, -3.0F, 10.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -1.18F, 1.0798F, -0.1745F, 0.0F, 0.0F));

		PartDefinition Body = Kafka.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

		PartDefinition LowerBody = Body.addOrReplaceChild("LowerBody", CubeListBuilder.create().texOffs(32, 60).addBox(0.0F, 0.0F, 1.5F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(0, 21).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(1, 14).addBox(-3.5F, 1.3F, -1.5F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(80, 14).addBox(-3.0F, 1.35F, -2.05F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition UpperBody = Body.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -8.775F, -2.25F, 9.0F, 9.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(32, 52).addBox(0.0F, -7.775F, 1.75F, 0.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.775F, -0.25F));

		PartDefinition Body_r1 = UpperBody.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(12, 88).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.675F, -5.975F, -2.25F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Body_r2 = UpperBody.addOrReplaceChild("Body_r2", CubeListBuilder.create().texOffs(24, 88).addBox(-2.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.675F, -5.975F, -2.25F, 0.0F, 0.0F, 0.0873F));

		PartDefinition RightArm = Kafka.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(-4.5F, -7.0F, 0.0F));

		PartDefinition Shoulder = RightArm.addOrReplaceChild("Shoulder", CubeListBuilder.create(), PartPose.offset(0.75F, 0.25F, -0.975F));

		PartDefinition RightArm_r1 = Shoulder.addOrReplaceChild("RightArm_r1", CubeListBuilder.create().texOffs(61, 41).addBox(-2.5F, -3.0F, -1.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, -0.2F, 0.0F, 0.0F, 0.0873F));

		PartDefinition Arm = RightArm.addOrReplaceChild("Arm", CubeListBuilder.create(), PartPose.offset(-3.0F, 2.0F, 0.0F));

		PartDefinition Bicep = Arm.addOrReplaceChild("Bicep", CubeListBuilder.create(), PartPose.offset(0.75F, -0.75F, -0.975F));

		PartDefinition RightArm_r2 = Bicep.addOrReplaceChild("RightArm_r2", CubeListBuilder.create().texOffs(59, 95).addBox(-1.3774F, 3.4122F, -1.4F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 52).addBox(-1.8609F, 0.0821F, -1.9F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5041F, 0.043F, 0.675F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Forearm = Arm.addOrReplaceChild("Forearm", CubeListBuilder.create(), PartPose.offset(0.05F, 3.55F, 0.0F));

		PartDefinition RightArm_r3 = Forearm.addOrReplaceChild("RightArm_r3", CubeListBuilder.create().texOffs(0, 52).addBox(-2.5F, -3.0F, -0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7F, 3.05F, -1.7F, 0.0F, 0.0F, -0.0436F));

		PartDefinition Hand = Forearm.addOrReplaceChild("Hand", CubeListBuilder.create(), PartPose.offset(0.7F, -4.3F, -0.775F));

		PartDefinition RightArm_r4 = Hand.addOrReplaceChild("RightArm_r4", CubeListBuilder.create().texOffs(26, 78).addBox(-2.5F, -3.0F, 0.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.95F, 11.525F, -1.425F, 0.0F, 0.0F, -0.1309F));

		PartDefinition LeftArm = Kafka.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(4.5F, -7.0F, 0.0F));

		PartDefinition Shoulder2 = LeftArm.addOrReplaceChild("Shoulder2", CubeListBuilder.create(), PartPose.offset(-0.75F, 0.25F, -0.975F));

		PartDefinition LeftArm_r1 = Shoulder2.addOrReplaceChild("LeftArm_r1", CubeListBuilder.create().texOffs(61, 41).mirror().addBox(-2.5F, -3.0F, -1.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.0F, 0.0F, -0.2F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Arm2 = LeftArm.addOrReplaceChild("Arm2", CubeListBuilder.create(), PartPose.offset(3.0F, 2.0F, 0.0F));

		PartDefinition Bicep2 = Arm2.addOrReplaceChild("Bicep2", CubeListBuilder.create(), PartPose.offset(-0.75F, -0.75F, -0.975F));

		PartDefinition LeftArm_r2 = Bicep2.addOrReplaceChild("LeftArm_r2", CubeListBuilder.create().texOffs(59, 95).mirror().addBox(-1.6226F, 3.4122F, -1.4F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(16, 52).mirror().addBox(-2.1391F, 0.0821F, -1.9F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5041F, 0.043F, 0.675F, 0.0F, 0.0F, -0.0436F));

		PartDefinition Forearm2 = Arm2.addOrReplaceChild("Forearm2", CubeListBuilder.create(), PartPose.offset(-0.05F, 3.55F, 0.0F));

		PartDefinition LeftArm_r3 = Forearm2.addOrReplaceChild("LeftArm_r3", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.5F, -3.0F, -0.5F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.7F, 3.05F, -1.7F, 0.0F, 0.0F, 0.0436F));

		PartDefinition Hand2 = Forearm2.addOrReplaceChild("Hand2", CubeListBuilder.create(), PartPose.offset(-0.7F, -4.3F, -0.775F));

		PartDefinition LeftArm_r4 = Hand2.addOrReplaceChild("LeftArm_r4", CubeListBuilder.create().texOffs(26, 78).mirror().addBox(-0.5F, -3.0F, 0.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.95F, 11.525F, -1.425F, 0.0F, 0.0F, 0.1309F));

		PartDefinition RightLeg = Kafka.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.45F, 2.5F, 0.5F, 0.0F, 0.0F, 0.0873F));

		PartDefinition Thigh = RightLeg.addOrReplaceChild("Thigh", CubeListBuilder.create().texOffs(20, 39).addBox(-2.2385F, -2.0114F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.1999F, -0.4742F, -0.5F));

		PartDefinition LowerLeg = RightLeg.addOrReplaceChild("LowerLeg", CubeListBuilder.create(), PartPose.offset(0.444F, 5.34F, -0.1F));

		PartDefinition Shin = LowerLeg.addOrReplaceChild("Shin", CubeListBuilder.create(), PartPose.offset(-0.0427F, 0.0088F, -0.4F));

		PartDefinition LeftLeg_r1 = Shin.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(58, 57).addBox(-5.75F, -3.4F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0321F, 3.0602F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Foot = LowerLeg.addOrReplaceChild("Foot", CubeListBuilder.create(), PartPose.offset(0.3726F, 5.751F, 0.1F));

		PartDefinition LeftLeg_r2 = Foot.addOrReplaceChild("LeftLeg_r2", CubeListBuilder.create().texOffs(0, 88).addBox(-5.0F, -3.1F, -0.65F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(40, 78).addBox(-5.0F, -1.1F, -3.65F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.7167F, 2.1681F, -1.425F, 0.0F, 0.0F, -0.0873F));

		PartDefinition LeftLeg_r3 = Foot.addOrReplaceChild("LeftLeg_r3", CubeListBuilder.create().texOffs(0, 95).addBox(-5.0F, -1.9578F, -1.8751F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.7167F, 2.1681F, -1.425F, 0.3927F, 0.0F, -0.0873F));

		PartDefinition LeftLeg = Kafka.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offsetAndRotation(2.45F, 2.5F, 0.5F, 0.0F, 0.0F, -0.0873F));

		PartDefinition Thigh2 = LeftLeg.addOrReplaceChild("Thigh2", CubeListBuilder.create().texOffs(0, 39).addBox(-2.7615F, -2.0114F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1999F, -0.4742F, -0.5F));

		PartDefinition LowerLeg2 = LeftLeg.addOrReplaceChild("LowerLeg2", CubeListBuilder.create(), PartPose.offset(-0.3531F, 5.4483F, -0.1F));

		PartDefinition Shin2 = LowerLeg2.addOrReplaceChild("Shin2", CubeListBuilder.create(), PartPose.offset(-0.0482F, -0.0995F, -0.4F));

		PartDefinition RightLeg_r1 = Shin2.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(42, 57).addBox(1.75F, -3.4F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0321F, 3.0602F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition Foot2 = LowerLeg2.addOrReplaceChild("Foot2", CubeListBuilder.create(), PartPose.offset(-0.4635F, 5.6426F, 0.1F));

		PartDefinition RightLeg_r2 = Foot2.addOrReplaceChild("RightLeg_r2", CubeListBuilder.create().texOffs(68, 78).addBox(2.0F, -3.1F, -0.65F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(40, 78).addBox(2.0F, -1.1F, -3.675F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.7167F, 2.1681F, -1.425F, 0.0F, 0.0F, 0.0873F));

		PartDefinition RightLeg_r3 = Foot2.addOrReplaceChild("RightLeg_r3", CubeListBuilder.create().texOffs(56, 88).addBox(2.0F, -1.9578F, -1.8751F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.7167F, 2.1681F, -1.425F, 0.3927F, 0.0F, 0.0873F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);
		this.animate(((Kaiju_no8Entity) entity).roarAnimationState, Kaiju_no8Animations.Roar, ageInTicks, 1f);
		this.animateWalk(Kaiju_no8Animations.Walk, limbSwing, limbSwingAmount, 1.5f,1f);
		this.animate(((Kaiju_no8Entity) entity).idleAnimationState, Kaiju_no8Animations.Idle, ageInTicks, 1f);
		this.animate(((Kaiju_no8Entity) entity).attackAnimationState, Kaiju_no8Animations.Melee1, ageInTicks, 1f);
		this.animate(((Kaiju_no8Entity) entity).runAnimationState, Kaiju_no8Animations.Sprint, ageInTicks, 1.5f);
	}
	private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
		pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0F, 30.0F);
		pHeadPitch = Mth.clamp(pHeadPitch, -25.0F, 45.0F);

		this.Head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.Head.xRot = pHeadPitch * ((float)Math.PI / 180F);
	}


	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Kafka.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return Kafka;
	}
}