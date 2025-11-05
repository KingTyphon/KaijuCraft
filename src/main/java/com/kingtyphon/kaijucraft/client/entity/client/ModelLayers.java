package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayers {
    public static final ModelLayerLocation KAIJU_NO8_LAYER = new ModelLayerLocation(
            new ResourceLocation(KaijuCraft.MODID, "kaiju_no8_layer"), "main");
    public static final ModelLayerLocation KAIJUPARTS = new ModelLayerLocation( new ResourceLocation(KaijuCraft.MODID, "kaijupart"), "main");
    public static final ModelLayerLocation PRIMIGENIUS = new ModelLayerLocation(new ResourceLocation(KaijuCraft.MODID, "primigenius"), "main");
}
