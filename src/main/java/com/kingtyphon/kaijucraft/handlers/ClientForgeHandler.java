package com.kingtyphon.kaijucraft.handlers;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.gui.KaijuGui;
import com.kingtyphon.kaijucraft.item.armor.CasualArmorItem;
import com.kingtyphon.kaijucraft.item.armor.FormalArmorItem;
import com.kingtyphon.kaijucraft.keybinds.KaijuKeybinds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.mojang.text2speech.Narrator.LOGGER;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeHandler {
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        if(KaijuKeybinds.INSTANCE.kaijuGui.consumeClick()){
                LOGGER.debug("Key for Kaiju GUI pressed");
                Minecraft.getInstance().setScreen(new KaijuGui());
            }
    }
    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
        boolean wearingCasualArmor = player.getInventory().armor.get(3).getItem() instanceof CasualArmorItem;
        boolean wearingFormalArmor = player.getInventory().armor.get(3).getItem() instanceof FormalArmorItem;

        if (wearingCasualArmor || wearingFormalArmor) {
            PlayerRenderer renderer = (PlayerRenderer) event.getRenderer();
            renderer.getModel().head.visible = true;
            renderer.getModel().hat.visible = true;
            renderer.getModel().body.visible = false;
            renderer.getModel().rightArm.visible = false;
            renderer.getModel().leftArm.visible = false;
            renderer.getModel().rightLeg.visible = false;
            renderer.getModel().leftLeg.visible = false;
        } else {
            PlayerRenderer renderer = (PlayerRenderer) event.getRenderer();
            renderer.getModel().head.visible = true;
            renderer.getModel().hat.visible = true;
            renderer.getModel().body.visible = true;
            renderer.getModel().rightArm.visible = true;
            renderer.getModel().leftArm.visible = true;
            renderer.getModel().rightLeg.visible = true;
            renderer.getModel().leftLeg.visible = true;
        }
    }
}
