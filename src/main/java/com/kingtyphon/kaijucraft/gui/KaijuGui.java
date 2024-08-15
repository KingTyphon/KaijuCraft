package com.kingtyphon.kaijucraft.gui;

import com.kingtyphon.kaijucraft.KaijuCraft;
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


    public KaijuGui() {
        super(Component.literal("Kaiju GUI"));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        // Render the textures scaled to the screen dimensions
        graphics.blit(BACKGROUND, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(SKILLPOINT, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(EXPBAR, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(PERDIS, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(RANKDIS, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(MINDSKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(MELEESKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
        graphics.blit(RANGESKILL, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);

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