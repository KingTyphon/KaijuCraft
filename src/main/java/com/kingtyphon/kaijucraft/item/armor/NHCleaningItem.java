package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.function.Consumer;

public class NHCleaningItem extends ArmorItem {
    public NHCleaningItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer){
        consumer.accept(new IClientItemExtensions() {
            private UniformArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original){
                if(this.renderer == null)
                    this.renderer = new UniformArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }




    public class NHCleaningModel<T extends LivingEntity> extends HumanoidModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(KaijuCraft.MODID, "/textures/armor/nhcleaningmodel"), "main");
    private final ModelPart bipedHeadwear;
    private final ModelPart hood;
    private final ModelPart Glass;
    private final ModelPart bipedBody;
    private final ModelPart armorBody;
    private final ModelPart bipedRightArm;
    private final ModelPart armorRightArm;
    private final ModelPart bipedLeftArm;
    private final ModelPart armorLeftArm;
    private final ModelPart bipedRightLeg;
    private final ModelPart armorRightLeg;
    private final ModelPart armorRightBoot;
    private final ModelPart bipedLeftLeg;
    private final ModelPart armorLeftLeg;
    private final ModelPart armorLeftBoot;

    public NHCleaningModel(ModelPart root) {
        super(root);
        this.bipedHeadwear = root.getChild("bipedHeadwear");
        this.hood = this.bipedHeadwear.getChild("hood");
        this.Glass = this.hood.getChild("Glass");
        this.bipedBody = root.getChild("bipedBody");
        this.armorBody = this.bipedBody.getChild("armorBody");
        this.bipedRightArm = root.getChild("bipedRightArm");
        this.armorRightArm = this.bipedRightArm.getChild("armorRightArm");
        this.bipedLeftArm = root.getChild("bipedLeftArm");
        this.armorLeftArm = this.bipedLeftArm.getChild("armorLeftArm");
        this.bipedRightLeg = root.getChild("bipedRightLeg");
        this.armorRightLeg = this.bipedRightLeg.getChild("armorRightLeg");
        this.armorRightBoot = this.armorRightLeg.getChild("armorRightBoot");
        this.bipedLeftLeg = root.getChild("bipedLeftLeg");
        this.armorLeftLeg = this.bipedLeftLeg.getChild("armorLeftLeg");
        this.armorLeftBoot = this.bipedLeftLeg.getChild("armorLeftBoot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bipedHeadwear = partdefinition.addOrReplaceChild("bipedHeadwear", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -0.3F, 0.0F, 0.0001F, 0.0F, 0.0015F));

        PartDefinition hood = bipedHeadwear.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(1, 1).addBox(-4.0068F, -8.5F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Glass = hood.addOrReplaceChild("Glass", CubeListBuilder.create().texOffs(46, 25).addBox(-4.0068F, -1.0F, -0.8F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.5F, -3.55F));

        PartDefinition HatLayer_r1 = Glass.addOrReplaceChild("HatLayer_r1", CubeListBuilder.create().texOffs(45, 11).addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0432F, 7.5F, 3.2F, 0.0F, -1.5708F, 0.0F));

        PartDefinition HatLayer_r2 = Glass.addOrReplaceChild("HatLayer_r2", CubeListBuilder.create().texOffs(45, 11).mirror().addBox(-4.0F, -8.5F, -4.0F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-0.0318F, 7.5F, 3.2F, 0.0F, 1.5708F, 0.0F));

        PartDefinition bipedBody = partdefinition.addOrReplaceChild("bipedBody", CubeListBuilder.create(), PartPose.offset(0.0F, 24.6F, -0.5F));

        PartDefinition armorBody = bipedBody.addOrReplaceChild("armorBody", CubeListBuilder.create().texOffs(1, 43).addBox(-5.0F, -0.4F, -2.5F, 10.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.6F, 0.5F));

        PartDefinition bipedRightArm = partdefinition.addOrReplaceChild("bipedRightArm", CubeListBuilder.create(), PartPose.offset(-1.55F, 24.4F, 2.0F));

        PartDefinition armorRightArm = bipedRightArm.addOrReplaceChild("armorRightArm", CubeListBuilder.create().texOffs(1, 19).addBox(-2.95F, -2.4F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.9F, -22.4F, -2.0F));

        PartDefinition bipedLeftArm = partdefinition.addOrReplaceChild("bipedLeftArm", CubeListBuilder.create(), PartPose.offset(1.55F, 24.4F, 2.0F));

        PartDefinition armorLeftArm = bipedLeftArm.addOrReplaceChild("armorLeftArm", CubeListBuilder.create().texOffs(21, 19).mirror().addBox(-2.05F, -2.4F, -2.5F, 5.0F, 13.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.9F, -22.4F, -2.0F));

        PartDefinition bipedRightLeg = partdefinition.addOrReplaceChild("bipedRightLeg", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition armorRightLeg = bipedRightLeg.addOrReplaceChild("armorRightLeg", CubeListBuilder.create().texOffs(1, 66).addBox(-2.8F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -12.0F, 0.0F));

        PartDefinition armorRightBoot = armorRightLeg.addOrReplaceChild("armorRightBoot", CubeListBuilder.create().texOffs(54, 66).addBox(-2.525F, 7.15F, -2.15F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 94).addBox(-2.525F, 10.15F, -3.15F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(54, 91).addBox(-0.875F, 7.15F, -2.15F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(56, 79).addBox(-0.875F, 10.15F, -3.15F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(34, 77).addBox(-0.875F, 7.15F, -0.65F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 66).addBox(-2.525F, 7.15F, -0.65F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.025F, 0.0F, 0.0F));

        PartDefinition bipedLeftLeg = partdefinition.addOrReplaceChild("bipedLeftLeg", CubeListBuilder.create(), PartPose.offset(1.925F, 12.0F, 0.0F));

        PartDefinition armorLeftLeg = bipedLeftLeg.addOrReplaceChild("armorLeftLeg", CubeListBuilder.create().texOffs(1, 80).addBox(-2.225F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorLeftBoot = bipedLeftLeg.addOrReplaceChild("armorLeftBoot", CubeListBuilder.create().texOffs(54, 66).mirror().addBox(-0.475F, 7.15F, -2.15F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 94).mirror().addBox(-0.475F, 10.15F, -3.15F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(54, 91).mirror().addBox(-2.125F, 7.15F, -2.15F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 79).mirror().addBox(-2.125F, 10.15F, -3.15F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 77).mirror().addBox(-2.125F, 7.15F, -0.65F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(34, 66).mirror().addBox(-0.475F, 7.15F, -0.65F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 67, 99);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bipedHeadwear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedBody.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedRightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        bipedLeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
}

