package com.kingtyphon.kaijucraft.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KaijuProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static final Capability<IKaijuCapability> KAIJU_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    private final IKaijuCapability instance = new KaijuCapability();
    private final LazyOptional<IKaijuCapability> lazyInstance = LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == KAIJU_CAPABILITY ? lazyInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Energy", instance.getEnergy());
        tag.putFloat("MaxEnergy", instance.getMaxEnergy());
        tag.putInt("Level", instance.getLevel());
        tag.putInt("XP", instance.getXP());
        tag.putInt("MaxXp", instance.getMaxXp());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        instance.setEnergy(nbt.getFloat("Energy"));
        instance.setMaxEnergy(nbt.getFloat("MaxEnergy"));
        instance.setLevel(nbt.getInt("Level"));
        instance.setXP(nbt.getInt("XP"));
        instance.setMaxXp(nbt.getInt("MaxXp"));
    }
}
