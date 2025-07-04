package com.kingtyphon.kaijucraft.item.guns;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class Glock17Gen4Model extends GeoModel<Glock17Gen4> {
    @Override
    public ResourceLocation getModelResource(Glock17Gen4 glock17Gen4) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/glock17.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Glock17Gen4 glock17Gen4) {
        boolean regular = glock17Gen4.getBulletType().equals("Regular");
        String type = glock17Gen4.getBulletType();

        if(!regular && glock17Gen4.getBulletType().equals("Explosive")){
        return new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17_explode.png");
        } else if (!regular && glock17Gen4.getBulletType().equals("Ice")) {
            return new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17_ice.png");
        } else {
            return new ResourceLocation(KaijuCraft.MODID, "textures/item/glock17.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Glock17Gen4 glock17Gen4) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/glock17.json");
    }
}
