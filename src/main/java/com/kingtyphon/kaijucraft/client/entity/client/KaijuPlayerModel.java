package com.kingtyphon.kaijucraft.client.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class KaijuPlayerModel extends PlayerModel<AbstractClientPlayer> {
    public static final ModelLayerLocation KAIJU_PLAYER =
            new ModelLayerLocation(new ResourceLocation("kaijucraft", "kaiju_player"), "main");
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

    // Constructor where the parts are initialized directly
    public KaijuPlayerModel(ModelPart root, boolean slim) {
        super(root, slim); // call vanilla PlayerModel constructor

        this.Kafka = root.getChild("Kafka");
        this.Head = Kafka.getChild("Head");
        this.Skull = Head.getChild("Skull");
        this.UpperHead = Head.getChild("UpperHead");
        this.TopTeeth = Head.getChild("TopTeeth");
        this.Jaw = Head.getChild("Jaw");
        this.BottomTeeth = Head.getChild("BottomTeeth");
        this.Tongue = Head.getChild("Tongue");
        this.T2 = Head.getChild("T2");
        this.Kneck = root.getChild("Kneck");
        this.Body = root.getChild("Body");
        this.LowerBody = Body.getChild("LowerBody");
        this.UpperBody = Body.getChild("UpperBody");
        this.RightArm = root.getChild("RightArm");
        this.Shoulder = RightArm.getChild("Shoulder");
        this.Arm = RightArm.getChild("Arm");
        this.Bicep = Arm.getChild("Bicep");
        this.Forearm = Arm.getChild("Forearm");
        this.Hand = Forearm.getChild("Hand");
        this.LeftArm = root.getChild("LeftArm");
        this.Shoulder2 = LeftArm.getChild("Shoulder2");
        this.Arm2 = LeftArm.getChild("Arm2");
        this.Bicep2 = Arm2.getChild("Bicep2");
        this.Forearm2 = Arm2.getChild("Forearm2");
        this.Hand2 = Forearm2.getChild("Hand2");
        this.RightLeg = root.getChild("RightLeg");
        this.Thigh = RightLeg.getChild("Thigh");
        this.LowerLeg = RightLeg.getChild("LowerLeg");
        this.Shin = LowerLeg.getChild("Shin");
        this.Foot = LowerLeg.getChild("Foot");
        this.LeftLeg = root.getChild("LeftLeg");
        this.Thigh2 = LeftLeg.getChild("Thigh2");
        this.LowerLeg2 = LeftLeg.getChild("LowerLeg2");
        this.Shin2 = LowerLeg2.getChild("Shin2");
        this.Foot2 = LowerLeg2.getChild("Foot2");
    }

    // Method to create the model layers
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        // Define parts using PartDefinition methods and the appropriate offsets
        PartDefinition Kafka = partdefinition.addOrReplaceChild("Kafka", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, 0.0F));
        PartDefinition Head = Kafka.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        // Add other parts here (Head, Body, Arms, etc.)
        // e.g., Head.addOrReplaceChild("Skull", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        // Return the body layer (adjust texture size as needed)
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(AbstractClientPlayer player,
                          float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {
        // Head follows camera
        this.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.Head.xRot = headPitch * ((float)Math.PI / 180F);

        // Arms + legs use vanilla swing logic
        this.RightArm.xRot = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 2.0F * limbSwingAmount * 0.5F;
        this.LeftArm.xRot  = (float) Math.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;

        this.RightLeg.xRot = (float) Math.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.LeftLeg.xRot  = (float) Math.cos(limbSwing * 0.6662F + Math.PI) * 1.4F * limbSwingAmount;

        // Sneak / swim checks
        if (player.isCrouching()) {
            this.Body.xRot = 0.5F;
        } else {
            this.Body.xRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        // Call render for each part (head, body, arms, etc.)
        Kafka.render(poseStack, vertexConsumer, light, overlay, red, green, blue, alpha);
    }


}
