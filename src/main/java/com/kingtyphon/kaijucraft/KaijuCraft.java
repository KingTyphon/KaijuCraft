package com.kingtyphon.kaijucraft;


import com.kingtyphon.kaijucraft.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.entity.animations.GunAnimation3rdPerson;
import com.kingtyphon.kaijucraft.entity.client.Kaiju_no8Renderer;
import com.kingtyphon.kaijucraft.entity.client.LarvaRenderer;
import com.kingtyphon.kaijucraft.event.CustomRenderRegistry;
import com.kingtyphon.kaijucraft.handlers.KeyInputHandler;
import com.kingtyphon.kaijucraft.init.EntityInit;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.KaijuCreativeModeTab;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import com.kingtyphon.kaijucraft.keybinds.KaijuKeybinds;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.sound.KaijuSounds;
import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.geckolib.GeckoLib;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.kingtyphon.kaijucraft.event.ModEvents.isWearingSpecialArmor;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KaijuCraft.MODID)
public class KaijuCraft
{
    public static final String MODID = "kaijucraft";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue();

    public KaijuCraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        ItemInit.register(modEventBus);
        EntityInit.register(modEventBus);
        KaijuSounds.register(modEventBus);
        // Register the commonSetup method for modloading
        GeckoLib.initialize();
        KaijuCreativeModeTab.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

        ModMessages.register();

    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        //MinecraftForge.EVENT_BUS.register(new ClientForgeHandler());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(new ResourceLocation(MODID, "animation"), 42, KaijuCraft::registerPlayerAnimation);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onRegisterModelPredicates(RegisterClientReloadListenersEvent event) {
            ItemProperties.register(ItemInit.PNEUMATICCHAINSAW.get(),
                    new ResourceLocation("kaijucraft", "on"),
                    (stack, world, entity, seed) -> stack.getOrCreateTag().getBoolean("on") ? 1.0F : 0.0F);
        }
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(EntityInit.KAIJU_NO8.get(), Kaiju_no8Renderer::new);
            EntityRenderers.register(EntityInit.LARVA.get(), LarvaRenderer::new);
            CustomRenderRegistry.init();
        }
        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(IKaijuCapability.class);
        }
    }
    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        //This will be invoked for every new player
        return new ModifierLayer<>();
    }
    public static void queueServerWork(int tick, Runnable action) {
        workQueue.add(new AbstractMap.SimpleEntry(action, tick));
    }
    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList();
            workQueue.forEach((work) -> {
                work.setValue((Integer)work.getValue() - 1);
                if ((Integer)work.getValue() == 0) {
                    actions.add(work);
                }

            });
            actions.forEach((e) -> {
                ((Runnable)e.getKey()).run();
            });
            workQueue.removeAll(actions);
        }

    }

}
