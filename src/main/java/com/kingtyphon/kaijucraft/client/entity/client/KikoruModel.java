package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.npc.Kikoru;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class KikoruModel extends GeoModel<Kikoru> {
    @Override
    public ResourceLocation getModelResource(Kikoru entity) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/kikoru.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Kikoru entity) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/kikoru.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Kikoru entity) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/kikoru.json");
    }
}
