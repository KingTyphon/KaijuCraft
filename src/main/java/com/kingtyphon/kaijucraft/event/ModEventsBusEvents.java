package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.entity.kaiju.*;
import com.kingtyphon.kaijucraft.entity.npc.Kikoru;
import com.kingtyphon.kaijucraft.init.EntityInit;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.kingtyphon.kaijucraft.KaijuCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventsBusEvents {
    @SubscribeEvent
    public static void registerAttributes (EntityAttributeCreationEvent event){
        event.put(EntityInit.KAIJU_NO8.get(), Kaiju_no8Entity.createAttributes().build());
        event.put(EntityInit.LARVA.get(), LarvaEntity.createAttributes().build());
        event.put(EntityInit.PRIMIGENIUS.get(), PrimigeniusEntity.createAttributes().build());
        event.put(EntityInit.TRICHONEPHILA.get(), TrichonephilaEntity.createAttributes().build());
        event.put(EntityInit.KIKORU.get(), Kikoru.createAttributes().build());
        event.put(EntityInit.PHANEROPLUS.get(), PhaneroplusEntity.createAttributes().build());
    }
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        SpawnPlacements.register(EntityInit.PRIMIGENIUS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE,PrimigeniusEntity::canSpawn);
    }
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        //Set the player construct callback. It can be a lambda function.
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animation"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animationmelee"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animationaxe"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animationcannon"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animationglock"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                new ResourceLocation(MODID, "animationsigsauer"),
                42,
                ModEventsBusEvents::registerPlayerAnimation);
    }

    //This method will set your mods animation into the library.
    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }
}
