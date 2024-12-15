package com.kingtyphon.kaijucraft.gui;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.lowdragmc.lowdraglib.syncdata.managed.IManagedVar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;

public class KaijuGui extends Screen {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(KaijuCraft.MODID, "textures/gui/background.png");
    private static final ResourceLocation SKILLPOINT = new ResourceLocation(KaijuCraft.MODID, "textures/gui/skillpointcounter.png");
    private static final ResourceLocation EXPBAR = new ResourceLocation(KaijuCraft.MODID, "textures/gui/combatpower/expbar/expbar.png");
    private static final ResourceLocation PERDIS = new ResourceLocation(KaijuCraft.MODID, "textures/gui/combatpower/percentage_display.png");
    private static final ResourceLocation RANKDIS = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/rank_display.png");
    private static final ResourceLocation MINDSKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/mindmastery/mindskillexpbase.png");
    private static final ResourceLocation MELEESKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/meleemastery/meleeskillexpbase.png");
    private static final ResourceLocation RANGESKILL = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbase.png");
    private static final ResourceLocation OFDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/ranks/officerdisplay.png");
    private static final ResourceLocation PLDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/ranks/platoonleaderdisplay.png");
    private static final ResourceLocation DCDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/ranks/divisoncaptain.png");
    private static final ResourceLocation VCDISPLAY = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rankdisplay/ranks/vicecaptaindisplay.png");
    private static final ResourceLocation FULLEXPBAR = new ResourceLocation(KaijuCraft.MODID, "textures/gui/combatpower/expbar/fullexpbar.png");
    //Buttons
    private static final ResourceLocation MELEEBUTTON = new ResourceLocation(KaijuCraft.MODID, "textures/gui/meleemastery/meleeskillexpbutton.png");
    private static final ResourceLocation MINDBUTTON = new ResourceLocation(KaijuCraft.MODID, "textures/gui/mindmastery/mindskillexpbutton.png");
    private static final ResourceLocation RANGEBUTTON = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/rangeskillexpbutton.png");



    private static ResourceLocation TENSDIS = null;
    private static ResourceLocation ONESDIS;
    private static ResourceLocation TENSDISSP = null;
    private static ResourceLocation ONESDISSP;

    private static ResourceLocation MINDEXP;
    private static ResourceLocation RANGEXP;
    private static ResourceLocation MELEEXP;


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
            if(kapability.getSP()>0 && kapability.getMind() < 16 && kapability.getMelee() < 16 && kapability.getRange() < 16){
                graphics.blit(MELEEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
                graphics.blit(MINDBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
                graphics.blit(RANGEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);

            }
            if((kapability.getSP()>0 && kapability.getMind() < 16 && kapability.getMelee() < 16 && kapability.getRange() >= 16)){
                graphics.blit(MELEEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
                graphics.blit(MINDBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getSP()>0 && kapability.getMind() < 16 && kapability.getMelee() >= 16 && kapability.getRange() < 16){
                graphics.blit(MINDBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
                graphics.blit(RANGEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);

            }
            if(kapability.getSP()>0 && kapability.getMind() >= 16 && kapability.getMelee() < 16 && kapability.getRange() < 16){
                graphics.blit(MELEEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
                graphics.blit(RANGEBUTTON, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);

            }
            int xp = kapability.getXP();
            int maxXp = kapability.getMaxXp();

// Calculate the Y position to start drawing from the texture (bottom of the XP bar)
            renderPlayerModel(graphics, 225, 200, 70, mouseX, mouseY);
// Assuming `currentXP` is the player's current XP and `maxXP`v is the max XP for the bar
            int barHeight = 80; // Height of the display area for the XP bar
            int fullTextureHeight = 267; // Total height of the texture
            float percentageFilled =((float) xp/ maxXp);
// Calculate how much of the bar should be filled based on the current XP
            int filledHeight = (int) (percentageFilled * barHeight);

// Calculate the starting Y position in the texture to render only the filled portion from the bottom
            int textureYStart = fullTextureHeight - filledHeight;

// Render the filled portion of the XP bar from bottom to top
            graphics.blit(FULLEXPBAR, 0, this.height - filledHeight, this.width, filledHeight, 0, textureYStart, 256, filledHeight, 256, fullTextureHeight);
            if(kapability.getLevel() < 41 && kapability.getLevel() > 19){
                graphics.blit(OFDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 71 && kapability.getLevel() > 40){
                graphics.blit(PLDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 81 && kapability.getLevel() > 70) {
                graphics.blit(VCDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(kapability.getLevel() < 101 && kapability.getLevel() > 80) {
                graphics.blit(DCDISPLAY, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            getLevelDisplay(kapability.getLevel());
            getSPDisplay(kapability.getSP());

            //Displays the certain amount of levels that it has
            int mind = kapability.getMind();
            int melee = kapability.getMelee();
            int range = kapability.getRange();
            handleStatDisplay(mind, melee, range);
            if(mind >0){
            graphics.blit(MINDEXP, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(melee >0){
            graphics.blit(MELEEXP, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }
            if(range >0){
            graphics.blit(RANGEXP, 0, 0, this.width, this.height, 0, 0, 256, 256, 256, 256);
            }

        });
        if(TENSDIS != null){
        graphics.blit(TENSDIS, 0 ,0, this.width, this.height, 0, 0, 256, 256, 256,256);}
        graphics.blit(ONESDIS, 0 ,0, this.width, this.height, 0, 0, 256, 256, 256,256);
        if(TENSDISSP != null){
        graphics.blit(TENSDISSP, 0 ,0, this.width, this.height, 0, 0, 256, 256, 256,256);
        }
        graphics.blit(ONESDISSP, 0 ,0, this.width, this.height, 0, 0, 256, 256, 256,256);
        // Call parent render method for additional rendering, such as widgets
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    private void handleStatDisplay(int mind, int melee, int range){
        if(mind >0){
        MINDEXP = new ResourceLocation(KaijuCraft.MODID, "textures/gui/mindmastery/expbar/" + mind +".png");
        }
        if(melee >0){
        MELEEXP = new ResourceLocation(KaijuCraft.MODID, "textures/gui/meleemastery/expbar/" + melee +".png");
        }
        if(range >0){
        RANGEXP = new ResourceLocation(KaijuCraft.MODID, "textures/gui/rangemastery/expbar/rangeskillexp" + range +".png");
        }
    }
    private void getLevelDisplay(int level){
        int tens = (level / 10) * 10;
        int ones = level % 10;
        if(level >9)
        {String tensResourcePath = "textures/gui/levelnumbers/" + tens + ".png";
            TENSDIS = new ResourceLocation(KaijuCraft.MODID, tensResourcePath);}
        String onesResourcePath = "textures/gui/levelnumbers/" + ones + ".png";

        // Example: You can combine them or return them separately



        ONESDIS = new ResourceLocation(KaijuCraft.MODID, onesResourcePath);
    }
    private void getSPDisplay(int sp){
        int tens = (sp / 10) * 10;
        int ones = sp % 10;
        if(tens>0) {
            String tensResourcePath = "textures/gui/spnumbers/" + tens + ".png";
            TENSDISSP = new ResourceLocation(KaijuCraft.MODID, tensResourcePath);
        }

        String onesResourcePath = "textures/gui/spnumbers/" + ones + ".png";
        ONESDISSP = new ResourceLocation(KaijuCraft.MODID, onesResourcePath);
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
    public void renderPlayerModel(GuiGraphics graphics, int x, int y, int scale, int mouseX, int mouseY) {
        Player player = Minecraft.getInstance().player;

        // Calculate rotation based on the mouse position relative to the model's position
        float rotationX = (float) (x - mouseX) * 0.5f;
        float rotationY = (float) (y - mouseY) * 0.5f;

        // Render the player model
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, x, y, scale, rotationX, rotationY, player);
    }
}