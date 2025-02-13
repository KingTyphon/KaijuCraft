package com.kingtyphon.kaijucraft.sound;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KaijuSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS=
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KaijuCraft.MODID);

    public static final RegistryObject<SoundEvent> KAIJU_NO8_PUNCH1 = registerSoundEvents("kaiju_no8_punch1");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_GROWL1 = registerSoundEvents("kaiju_no8_growl1");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_GROWL2 = registerSoundEvents("kaiju_no8_growl2");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_GROWL3 = registerSoundEvents("kaiju_no8_growl3");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_GRUNT1 = registerSoundEvents("kaiju_no8_grunt1");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_RAGE1 = registerSoundEvents("kaiju_no8_rage1");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_ROAR1 = registerSoundEvents("kaiju_no8_roar1");
    public static final RegistryObject<SoundEvent> KAIJU_NO8_ROAR2 = registerSoundEvents("kaiju_no8_roar2");
    public static final RegistryObject<SoundEvent> CHAINSAW_ON = registerSoundEvents("chainsaw_sound");
    public static final RegistryObject<SoundEvent> GUNSHOTG17 = registerSoundEvents("gunshot");



    private static RegistryObject<SoundEvent> registerSoundEvents(String name){
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(KaijuCraft.MODID, name)));
    }
    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}

