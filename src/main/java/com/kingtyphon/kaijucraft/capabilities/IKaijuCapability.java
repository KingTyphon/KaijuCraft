package com.kingtyphon.kaijucraft.capabilities;


import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public interface IKaijuCapability {
    public void syncWallrunVariables(Entity entity);
    public void setReloading(boolean isReloading);
    public void setShooting(boolean isShooting);
    public boolean isShooting();
    public boolean isReloading();
    public boolean getAutoSprint();
    public void setAutoSprint(boolean autoSprint);
    public double getWallrunning();
    public Kaiju_no8Entity getKaijuEntity();
    public void setKaijuEntity(Kaiju_no8Entity entity);
    public void setWallrunning(double wallrunning);
    public double getWallside();
    public void setWallside(double wallside);
    public boolean isRunningwall();
    public void setRunningwall(boolean runningwall);
    public boolean isDash();
    public void setDash(boolean dash);
    public int getMind();
    public int getMelee();
    public int getRange();
    public void levelUp();
    public float getEnergy();
    public int getXP();
    public int getLevel();
    public int getMaxXp();
    public float getMaxEnergy();
    public int getSP();
    public boolean percentageRandomizerCheck();
    public void setMind(int mind);
    public void setMelee(int melee);
    public void setRange(int range);
    public void useEnergy(int energy);
    public void setMaxXp(int maxXp);
    public void setXP(int xp);
    public void setLevel(int level);
    public void setEnergy(float energy);
    public void setMaxEnergy(float maxEnergy);
    public void setSP(int sp);
    public void copyFrom(IKaijuCapability player);
    public void deserializeNBT(CompoundTag nbtData);
    public CompoundTag serializeNBT();
    public void setPercentRandomizer(boolean set);
    public void setTransformed(boolean tranformed);
    public boolean isTransformed();
}
