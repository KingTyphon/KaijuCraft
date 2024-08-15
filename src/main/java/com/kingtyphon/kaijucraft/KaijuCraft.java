package com.kingtyphon.kaijucraft;


import com.kingtyphon.kaijucraft.handlers.ClientForgeHandler;
import com.kingtyphon.kaijucraft.handlers.KeyInputHandler;
import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.item.KaijuCreativeModeTab;
import com.kingtyphon.kaijucraft.keybinds.KaijuKeybinds;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KaijuCraft.MODID)
public class KaijuCraft
{
    public static final String MODID = "kaijucraft";
    private static final Logger LOGGER = LogUtils.getLogger();

    public KaijuCraft()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(modEventBus);
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


    }

    private void onClientSetup(FMLClientSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new ClientForgeHandler());
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());


    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
        }@SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KaijuKeybinds.INSTANCE.kaijuGui);

    }
    }

}
