package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.entity.kaiju.PhaneroplusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class PhaneroplusModel extends GeoModel<PhaneroplusEntity> {

    @Override
    public ResourceLocation getModelResource(PhaneroplusEntity kaijuNo8Entity) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/phaneroplus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PhaneroplusEntity kaijuNo8Entity) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/phaneroplus.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PhaneroplusEntity kaijuNo8Entity) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/phaneroplus.json");
    }
}
