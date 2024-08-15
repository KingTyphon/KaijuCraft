package com.kingtyphon.kaijucraft.capabilities;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IKaijuCapability {
    public void levelUp();
    public float getEnergy();
    public int getXP();
    public int getLevel();
    public int getMaxXp();
    public float getMaxEnergy();
    public void useEnergy(int energy);
    public void setMaxXp(int maxXp);
    public void setXP(int xp);
    public void setLevel(int level);
    public void setEnergy(float energy);
    public void setMaxEnergy(float maxEnergy);
    public void copyFrom(IKaijuCapability player);
}
