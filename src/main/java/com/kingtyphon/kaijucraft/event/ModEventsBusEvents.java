package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.client.Kaiju_no8Model;
import com.kingtyphon.kaijucraft.entity.client.ModelLayers;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventsBusEvents {
    @SubscribeEvent
    public static void registerAttributes (EntityAttributeCreationEvent event){
        event.put(EntityInit.KAIJU_NO8.get(), Kaiju_no8Entity.createAttributes().build());
        event.put(EntityInit.LARVA.get(), LarvaEntity.createAttributes().build());
    }
}
