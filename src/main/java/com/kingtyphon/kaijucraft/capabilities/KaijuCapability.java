package com.kingtyphon.kaijucraft.capabilities;

import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;

public class KaijuCapability implements IKaijuCapability{

    private float energy;
    private float maxEnergy;
    private int level;
    private int xp;
    private int maxXp;
    private int SP;
    private int mind;
    private int range;
    private int melee;
    private boolean percentageRandomizerFlag;
    private boolean transform;
    private boolean autoSprint = false;
    private double wallrunning = 0.0;
    private double wallside = 0.0;
    private boolean runningwall = false;
    private boolean dash = false;



    // Methods for handling wallrun capabilities (add to your existing methods)
    public void syncWallrunVariables(Entity entity) {
        if (entity instanceof Player) {
            // Sync the new values for wallrun here, if needed
        }
    }

    // Getter and Setter for Wallrun variables
    public boolean getAutoSprint() {
        return autoSprint;
    }

    public void setAutoSprint(boolean autoSprint) {
        this.autoSprint = autoSprint;
    }

    public double getWallrunning() {
        return wallrunning;
    }

    public void setWallrunning(double wallrunning) {
        this.wallrunning = wallrunning;
    }

    public double getWallside() {
        return wallside;
    }

    public void setWallside(double wallside) {
        this.wallside = wallside;
    }

    public boolean isRunningwall() {
        return runningwall;
    }

    public void setRunningwall(boolean runningwall) {
        this.runningwall = runningwall;
    }

    public boolean isDash() {
        return dash;
    }

    public void setDash(boolean dash) {
        this.dash = dash;
    }

    @Override
    public void levelUp() {
        int[] percentageThresholds = {2, 5, 8, 11, 14, 18, 22, 26, 34, 42, 45, 51, 54, 57, 59, 61, 64, 69, 75, 78, 81, 84, 87, 93, 95, 98};

        if(this.level < 99){
            if(this.xp == maxXp || this.xp >= maxXp ) {
            this.maxXp = 100 + (150*level);
            this.xp = 0;
            this.level +=1;
            for (int threshold : percentageThresholds) {
                if (this.level == threshold) {
                    setSP(getSP() + 1);
                    break; // Exit the loop once a match is found
                }
            }
        }
        }
    }

    @Override
    public float getEnergy() {
        return energy;
    }
    @Override
    public int getMind() {
        return mind;
    }
    @Override
    public int getRange() {
        return range;
    }
    @Override
    public int getMelee() {
        return melee;
    }

    @Override
    public int getSP(){return SP;}

    @Override
    public boolean percentageRandomizerCheck() {
        return percentageRandomizerFlag;
    }

    @Override
    public void setMind(int mind) {
        this.mind = mind;
    }

    @Override
    public void setMelee(int melee) {
        this.melee = melee;
    }

    @Override
    public void setRange(int range) {
        this.range = range;
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
    public void setSP(int sp){ this.SP = sp;}

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
    public void setPercentRandomizer(boolean set) {
        percentageRandomizerFlag = set;
    }

    @Override
    public void setTransformed(boolean tranform){
        this.transform = tranform;
    }
    @Override
    public boolean isTransformed(){
        return this.transform;
    }


    @Override
    public void copyFrom(IKaijuCapability player) {
        this.energy = player.getEnergy();
        this.maxEnergy = player.getMaxEnergy();
        this.level= player.getLevel();
        this.xp =  player.getXP();
        this.maxXp = player.getMaxXp();
        this.SP = player.getSP();
        this.mind = player.getMind();
        this.range = player.getRange();
        this.melee = player.getMelee();
        this.percentageRandomizerFlag = player.percentageRandomizerCheck();
        this.transform = player.isTransformed();
        this.autoSprint = player.getAutoSprint();
        this.wallrunning = player.getWallrunning();
        this.wallside = player.getWallside();
        this.runningwall = player.isRunningwall();
        this.dash = player.isDash();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.energy = nbt.getFloat("Energy");
        this.maxEnergy = nbt.getFloat("MaxEnergy");
        this.level = nbt.getInt("Level");
        this.xp = nbt.getInt("XP");
        this.maxXp = nbt.getInt("MaxXp");
        this.SP = nbt.getInt("SP");
        this.mind = nbt.getInt("Mind");
        this.range = nbt.getInt("Range");
        this.melee = nbt.getInt("Melee");
        this.percentageRandomizerFlag = nbt.getBoolean("PRF");
        this.transform = nbt.getBoolean("Transform");
        this.autoSprint = nbt.getBoolean("AutoSprint");
        this.wallrunning = nbt.getDouble("WallRunning");
        this.wallside = nbt.getDouble("WallSide");
        this.runningwall = nbt.getBoolean("RunningWall");
        this.dash = nbt.getBoolean("Dash");
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Energy",this.energy);
        tag.putFloat("MaxEnergy", this.maxEnergy);
        tag.putInt("Level", this.level);
        tag.putInt("XP", this.xp);
        tag.putInt("MaxXp", this.maxXp);
        tag.putInt("SP", this.SP);
        tag.putInt("Mind", this.mind);
        tag.putInt("Range", this.range);
        tag.putInt("Melee", this.melee);
        tag.putBoolean("PRF", this.percentageRandomizerFlag);
        tag.putBoolean("Transform", this.transform);
        tag.putBoolean("AutoSprint", this.autoSprint);
        tag.putDouble("WallRunning",this.wallrunning);
        tag.putDouble("WallSide", this.wallside);
        tag.putBoolean("RunningWall", this.runningwall);
        tag.putBoolean("Dash", this.dash);

        return tag;
    }


}
