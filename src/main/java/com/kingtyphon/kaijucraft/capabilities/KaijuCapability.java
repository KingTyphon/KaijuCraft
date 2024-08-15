package com.kingtyphon.kaijucraft.capabilities;

public class KaijuCapability implements IKaijuCapability{

    private float energy;
    private float maxEnergy;
    private int level;
    private int xp;
    private int maxXp;


    @Override
    public void levelUp() {
        if(this.xp == maxXp || this.xp >= maxXp) {
            this.maxXp = 100 + (150*level);
            this.xp = 0;
            this.level +=1;
        }
    }

    @Override
    public float getEnergy() {
        return energy;
    }

    @Override
    public int getXP() {
        return xp;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxXp() {
        return maxXp;
    }

    @Override
    public float getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public void useEnergy(int energy) {
        this.energy = this.energy - energy;
    }

    @Override
    public void setMaxXp(int maxXp) {
        this.maxXp = maxXp;
    }

    @Override
    public void setXP(int xp) {
        this.xp = xp;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setEnergy(float energy) {
        this.energy = energy;
    }

    @Override
    public void setMaxEnergy(float maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    @Override
    public void copyFrom(IKaijuCapability player) {
        this.energy = player.getEnergy();
        this.maxEnergy = player.getMaxEnergy();
        this.level= player.getLevel();
        this.xp =  player.getXP();
        this.maxXp = player.getMaxXp();
    }
}
