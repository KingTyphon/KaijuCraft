package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.client.entity.client.KaijuPartModel;
import com.kingtyphon.kaijucraft.client.entity.client.ModelLayers;
import com.kingtyphon.kaijucraft.client.entity.client.PrimigeniusModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventsBusClientEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModelLayers.KAIJUPARTS, KaijuPartModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayers.PRIMIGENIUS, PrimigeniusModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            LivingEntityRenderer<Player, EntityModel<Player>> renderer = (LivingEntityRenderer<Player, EntityModel<Player>>) event.getSkin(skin);
            if (renderer != null) {
                renderer.addLayer(new SlingRenderLayer(renderer));

            }

        }
    }
}
