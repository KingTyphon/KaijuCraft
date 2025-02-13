package com.kingtyphon.kaijucraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RecoilManager {
    private static final RandomSource random = RandomSource.create();

    private static double gunRecoilNormal;
    private static double gunRecoilAngle;
    private float gunRecoilRandom;
    private float cameraRecoil;
    private float progressCameraRecoil;

    public void applyRecoil(ItemStack gunStack, float recoilAngle, float recoilDurationOffset) {
        this.cameraRecoil = recoilAngle;
        this.progressCameraRecoil = 0F;
        this.gunRecoilRandom = random.nextFloat();

        // Get cooldown progress for scaling recoil effect
        ItemCooldowns tracker = Minecraft.getInstance().player.getCooldowns();
        float cooldown = tracker.getCooldownPercent(gunStack.getItem(), Minecraft.getInstance().getFrameTime());
        cooldown = cooldown >= recoilDurationOffset ?
                (cooldown - recoilDurationOffset) / (1.0F - recoilDurationOffset) : 0.0F;

        // Apply recoil scaling formula
        if (cooldown >= 0.8F) {
            float amount = 1.0F * ((1.0F - cooldown) / 0.2F);
            this.gunRecoilNormal = 1 - (--amount) * amount * amount * amount;
        } else {
            float amount = (cooldown / 0.8F);
            this.gunRecoilNormal = amount < 0.5 ? 2 * amount * amount : -1 + (4 - 2 * amount) * amount;
        }

        this.gunRecoilAngle = recoilAngle;
    }

    public void updateRecoil(float deltaTime) {
        if (cameraRecoil <= 0) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        float recoilStep = this.cameraRecoil * deltaTime * 0.15F;
        float recoilProgress = this.progressCameraRecoil / this.cameraRecoil;
        float nextProgress = (this.progressCameraRecoil + recoilStep) / this.cameraRecoil;

        float pitch = mc.player.getXRot();

        // Adjust camera movement smoothly based on recoil progression
        if (recoilProgress < 0.2F) {
            mc.player.setXRot(pitch - ((nextProgress - recoilProgress) / 0.2F) * this.cameraRecoil);
        } else {
            mc.player.setXRot(pitch - ((nextProgress - recoilProgress) / 0.8F) * this.cameraRecoil);
        }

        this.progressCameraRecoil += recoilStep;

        if (this.progressCameraRecoil >= this.cameraRecoil) {
            this.cameraRecoil = 0;
            this.progressCameraRecoil = 0;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            updateRecoil(0.05F); // Approximate deltaTime for smooth updates
        }
    }

    public double getGunRecoilNormal() { return gunRecoilNormal; }
    public double getGunRecoilAngle() { return gunRecoilAngle; }
    public float getGunRecoilRandom() { return gunRecoilRandom; }
}