package com.kingtyphon.kaijucraft.client.entity.client;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.TrichonephilaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TrichonephilaModel  extends GeoModel<TrichonephilaEntity> {
    @Override
    public ResourceLocation getModelResource(TrichonephilaEntity trichonephilaEntity) {
        return new ResourceLocation(KaijuCraft.MODID, "geo/trichonephila.geo.json");

    }

    @Override
    public ResourceLocation getTextureResource(TrichonephilaEntity trichonephilaEntity) {
        return new ResourceLocation(KaijuCraft.MODID, "textures/entity/trichonephila.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TrichonephilaEntity trichonephilaEntity) {
        return new ResourceLocation(KaijuCraft.MODID, "animations/trichonephila.json");
    }
}
