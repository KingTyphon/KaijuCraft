package com.kingtyphon.kaijucraft.gui;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class KaijuGui extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(KaijuCraft.MODID, "textures/gui/background.png");
    private static final ResourceLocation SKILLPOINT = new ResourceLocation(KaijuCraft.MODID, "textures/gui/skillpointcounter.png");
    private static final ResourceLocation EXPBAR = new ResourceLocation(KaijuCraft.MODID, "textures/gui/combatpower/expbar/expbar.png");
    private static final ResourceLocation PERDIS = new ResourceLocation(KaijuCraft.MODID, "textures/gui/combatpower/percentage_display.png");
    private static final ResourceLocation RANKDIS = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/rank_display.png");
    private static final ResourceLocation MINDSKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/mindmastery/mindskillexpbase.png");
    private static final ResourceLocation MELEESKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/meleemastery/meleeskillexpbase.png");
    private static final ResourceLocation RANGESKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");
    private static final ResourceLocation OFDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");
    private static final ResourceLocation PLDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");
    private static final ResourceLocation DCDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");
    private static final ResourceLocation VCDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");


    public KaijuGui() {
        super(Component.literal("Kaiju GUI"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int level = 0;
        // Render the textures scaled to the screen dimensions
        graphics.blit(BACKGROUND, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(SKILLPOINT, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(EXPBAR, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(PERDIS, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(RANKDIS, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(MINDSKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(MELEESKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(RANGESKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);

        //Checks Capability for Rank Display
        mc.player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kapability ->{
            if(kapability.getLevel() < 41 && kapability.getLevel() > 19){
                graphics.blit(OFDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 71 && kapability.getLevel() > 40){
                graphics.blit(PLDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 81 && kapability.getLevel() > 70) {
                graphics.blit(VCDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 99 && kapability.getLevel() > 80) {
                graphics.blit(DCDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }

            });
        // Call parent render method for additional rendering, such as widgets
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {

    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }
}