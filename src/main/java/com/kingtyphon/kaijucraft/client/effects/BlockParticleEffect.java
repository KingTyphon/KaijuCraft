package com.kingtyphon.kaijucraft.client.effects;

import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public abstract class BlockParticleEffect extends FXEffect {
    private final Vec3 position;

    public BlockParticleEffect(FX fx, Level level, Vec3 position) {
        super(fx, level);
        this.position = position;
    }

    @Override
    public void start() {
        if (this.position != null) {
            this.emitters.clear();
            this.emitters.addAll(this.fx.generateEmitters());
            Iterator<IParticleEmitter> iter = this.emitters.iterator();

            while (iter.hasNext()) {
                IParticleEmitter emitter = iter.next();
                if (!emitter.isSubEmitter()) {
                    emitter.reset();
                    emitter.self().setDelay(this.delay);
                    emitter.emmitToLevel(this, this.level, this.position.x, this.position.y, this.position.z, this.xRotation, this.yRotation, this.zRotation);
                }
            }
        }
    }
}