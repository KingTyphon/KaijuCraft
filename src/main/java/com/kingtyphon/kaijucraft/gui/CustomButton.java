package com.kingtyphon.kaijucraft.gui;

import com.kingtyphon.kaijucraft.util.ColorUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class CustomButton extends ImageButton {
    private ResourceLocation normal;
    public boolean selected = false;
    public boolean visible = true;

    public int hoverColor = ColorUtils.colorToInt(1.0F, 0.75F, 0.75F, 0.75F);
    public int normalColor = ColorUtils.colorToInt(1.0F, 1.0F, 1.0F, 1.0F);
    public int selectedColor = ColorUtils.colorToInt(1.0F, 0.0F, 1.0F, 0.0F);

    public CustomButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                        ResourceLocation texture, int textureWidth, int textureHeight, OnPress onPress) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffTex, texture, textureWidth, textureHeight, onPress);
        this.setNormal(texture);
    }

    public void setNormal(ResourceLocation loc) {
        this.normal = loc;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (!this.isVisible()) {
            return;
        }
        super.playDownSound(soundManager);
    }

    public void renderTexture(GuiGraphics guiGraphics, int x, int y, int xTexStart, int yTexStart, int width, int height) {
        ResourceLocation currentTexture = this.normal;
        int color = this.selected ? this.selectedColor : (this.isHovered() ? this.hoverColor : this.normalColor);

        float r = ColorUtils.getRed(color);
        float g = ColorUtils.getGreen(color);
        float b = ColorUtils.getBlue(color);
        float a = ColorUtils.getAlpha(color);

        guiGraphics.setColor(r, g, b, a);
        guiGraphics.blit(currentTexture, x, y, xTexStart, yTexStart, width, height);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); // Reset color
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.renderTexture(guiGraphics, this.getX(), this.getY(), this.xTexStart, this.yTexStart, this.width, this.height);
    }
}
