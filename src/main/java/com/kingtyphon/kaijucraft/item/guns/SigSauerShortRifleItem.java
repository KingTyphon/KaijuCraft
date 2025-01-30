package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.GunData;
import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.entity.animations.GunAnimation3rdPerson;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.kosmx.playerAnim.api.TransformType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class SigSauerShortRifleItem extends Item {
    public SigSauerShortRifleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000; // Similar to a bow, allows holding down the button
    }
//    protected void handleMuzzleFlash(Level level, LivingEntity shooter) {
//        if (level instanceof ServerLevel serverLevel) {
//            Vec3 particlePosition = shooter.getEyePosition();
//
//            // Shift to the side (taking into consideration which hand the gun is in)
//            Vec3 lookVector = shooter.getLookAngle();
//            Vec3 sideVector = lookVector.cross(new Vec3(0, 1, 0.2));
//            double sideOffset = shooter.getUsedItemHand() == InteractionHand.MAIN_HAND ? 0.5d : -0.5d;
//            particlePosition = particlePosition.add(sideVector.scale(sideOffset));
//
//            // Adjust forward position based on FOV
//            double fov = 70d;
//            if (shooter instanceof Player) {
//                fov = GunData.playerFOV;
//            }
//
//            double fovOffset = 1.75d - (fov / 150.0d);
//            particlePosition = particlePosition.add(lookVector.x * fovOffset * 0.5d, lookVector.y * fovOffset * 0.5d, lookVector.z * fovOffset * 0.5d);
////put your own particle here
////            serverLevel.sendParticles(ParticleTypesRegistry.MUZZLE_FLASH_PARTICLE.get(),
////                    particlePosition.x, particlePosition.y, particlePosition.z,
////                    3,
////                    serverLevel.getRandom().nextGaussian() * 0.05d,
////                    serverLevel.getRandom().nextGaussian() * 0.025d,
////                    serverLevel.getRandom().nextGaussian() * 0.05d,
////                    0.01d);
//
//            int smokeParticles = 3 ;
//            for (int j = 0; j < smokeParticles; ++j) {
//                serverLevel.sendParticles(ParticleTypes.SMOKE,
//                        particlePosition.x, particlePosition.y, particlePosition.z,
//                        3,
//                        serverLevel.getRandom().nextGaussian() * 0.05d,
//                        serverLevel.getRandom().nextGaussian() * 0.025d,
//                        serverLevel.getRandom().nextGaussian() * 0.05d,
//                        0.01d);
//            }
//        }
//    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.isUsingItem() && entityLiving.getUsedItemHand() == hand) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW; // Only when right-clicking
                }
                return HumanoidModel.ArmPose.ITEM;
            }
        });
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kapability ->{
            if(kapability.getLevel() >= 5){

            }
            else{
                player.sendSystemMessage(Component.literal("You are not strong enough to hold this."));
            }

        });
        // Start "aiming" with the item
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(itemStack);
    }
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity)
    {
        return true;
    }
}
