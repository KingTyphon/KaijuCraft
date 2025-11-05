package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CustomHealthRenderer {
    private static final ResourceLocation HEALTH_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/gui/healthbar/health.png");
    private static final ResourceLocation FRAME_TEXTURE = new ResourceLocation(KaijuCraft.MODID, "textures/gui/healthbar/frame.png");
    public static boolean isWearingSpecialArmor(Player player) {
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET); // Change slot if needed
        return !chest.isEmpty() && chest.getItem() == ItemInit.MCOMBAT_CHESTPLATE.get() && !legs.isEmpty() && legs.getItem() == ItemInit.MCOMBAT_LEGGINGS.get() && !feet.isEmpty() && feet.getItem() == ItemInit.MCOMBAT_BOOTS.get();
    }
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kaiju -> {
            if(isWearingSpecialArmor(player) && !player.isCreative()) {
                int screenWidth = event.getWindow().getGuiScaledWidth();
                int screenHeight = event.getWindow().getGuiScaledHeight();
                int health = (int) player.getHealth();
                int maxHealth = (int) player.getMaxHealth();

                int barWidth = 155; // Original texture width
                int barHeight = 24; // Original texture height
                float scale = 0.5f; // Scale down to 50%


                int xPos = (screenWidth / 2) - (barWidth / 2) - 13;
                int yPos = screenHeight - 42;

                int filledWidth = (int) ((health / (float) maxHealth) * barWidth);

                GuiGraphics gui = event.getGuiGraphics();
                PoseStack poseStack = gui.pose();
                poseStack.pushPose();
                poseStack.scale(scale, scale, 1.0f); // Scale everything down

                int scaledX = (int) (xPos / scale);
                int scaledY = (int) (yPos / scale);

                RenderSystem.setShaderTexture(0, FRAME_TEXTURE);
                gui.blit(FRAME_TEXTURE, scaledX, scaledY, 0, 0, barWidth, barHeight, barWidth, barHeight);

                RenderSystem.setShaderTexture(0, HEALTH_TEXTURE);
                gui.blit(HEALTH_TEXTURE, scaledX, scaledY, 0, 0, filledWidth, barHeight, barWidth, barHeight);

                poseStack.popPose();
            }}); // Reset scale
    }
    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type() && isWearingSpecialArmor(player)) {
            event.setCanceled(true); // Cancel vanilla health bar rendering
        }
        if (event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type() && isWearingSpecialArmor(player)) {
            event.setCanceled(true); // Cancel vanilla health bar rendering
        }


}}
