package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.animations.GunAnimation3rdPerson;
import com.kingtyphon.kaijucraft.entity.client.Kaiju_no8Model;
import com.kingtyphon.kaijucraft.entity.client.ModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventsBusClientEvents {

    @SubscribeEvent
    public static void registerLayer (EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModelLayers.KAIJU_NO8_LAYER, Kaiju_no8Model::createBodyLayer);
    }
    }
