package com.kingtyphon.kaijucraft.client.effects;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.item.melee.TwinSwordItem;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.client.fx.FX;
import com.lowdragmc.photon.client.fx.FXEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@OnlyIn(Dist.CLIENT)
public class PowerPlayerEffect extends FXEffect {
    public static Map<Entity, List<com.lowdragmc.photon.client.fx.EntityEffect>> CACHE = new HashMap();
    public final Entity entity;

    public PowerPlayerEffect(FX fx, Level level, Entity entity) {
        super(fx, level);
        this.entity = entity;
    }

    public boolean updateEmitter(IParticleEmitter emitter) {
            AtomicInteger level = new AtomicInteger();
        this.entity.getCapability(KaijuProvider.KAIJU_CAPABILITY, null).ifPresent(kapability -> {
            level.set(kapability.getLevel());
        });
        if (!this.entity.isAlive() || level.get() < 90 || !TwinSwordItem.isDualWielding((LivingEntity) this.entity)) {
            emitter.remove(this.forcedDeath);
            return this.forcedDeath;
        } else {
            emitter.updatePos(new Vector3f((float) (this.entity.getX() + this.xOffset), (float) (this.entity.getY() + this.yOffset), (float) (this.entity.getZ() + this.zOffset)));
            return false;
        }

    }

    public void start() {
        if (this.entity.isAlive()) {
            this.emitters.clear();
            this.emitters.addAll(this.fx.generateEmitters());
            if (!this.emitters.isEmpty()) {
                Iterator iter;
                if (!this.allowMulti) {
                    List<PowerPlayerEffect> effects = (List)CACHE.computeIfAbsent(this.entity, (p) -> {
                        return new ArrayList();
                    });
                    iter = effects.iterator();

                    while(iter.hasNext()) {
                        PowerPlayerEffect effect = (PowerPlayerEffect)iter.next();
                        boolean removed = false;
                        if (effect.emitters.stream().noneMatch((e) -> {
                            return e.self().isAlive();
                        })) {
                            iter.remove();
                            removed = true;
                        }

                        if (effect.fx.equals(this.fx) && !removed) {
                            return;
                        }
                    }

                    effects.add(this);
                }

                Vector3f realPos = this.entity.getPosition(0.0F).toVector3f().add((float)this.xOffset, (float)this.yOffset, (float)this.zOffset);
                iter = this.emitters.iterator();

                while(iter.hasNext()) {
                    IParticleEmitter emitter = (IParticleEmitter)iter.next();
                    if (!emitter.isSubEmitter()) {
                        emitter.reset();
                        emitter.self().setDelay(this.delay);
                        emitter.emmitToLevel(this, this.level, (double)realPos.x, (double)realPos.y, (double)realPos.z, this.xRotation, this.yRotation, this.zRotation);
                    }
                }

            }
        }
    }
}
