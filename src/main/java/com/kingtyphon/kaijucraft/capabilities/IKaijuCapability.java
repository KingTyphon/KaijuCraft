package com.kingtyphon.kaijucraft.capabilities;


import net.minecraft.nbt.CompoundTag;

public interface IKaijuCapability {
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
