package com.kingtyphon.kaijucraft.item;

import com.kingtyphon.kaijucraft.KaijuCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KaijuCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaijuCraft.MODID);

    public static final RegistryObject<CreativeModeTab> KAIJU_TAB = CREATIVE_MODE_TABS.register("kaiju_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemInit.MDEFENSEFORMAL_CHESTPLATE.get()))
                    .title(Component.translatable("creativetab.kaiju_tab"))
                    .displayItems((displayParameters, output) -> {
                        // Armor - Formal Outfit
                        output.accept(ItemInit.MDEFENSEFORMAL_CHESTPLATE.get());
                        output.accept(ItemInit.MDEFENSEFORMAL_LEGGINGS.get());
                        output.accept(ItemInit.MDEFENSEFORMAL_BOOTS.get());

                        // Armor - Casual Outfit
                        output.accept(ItemInit.MDEFENSECASUAL_CHESTPLATE.get());
                        output.accept(ItemInit.MDEFENSECASUAL_LEGGINGS.get());
                        output.accept(ItemInit.MDEFENSECASUAL_BOOTS.get());

                        // Add other items here
                        // Example:
                        // output.accept(ItemInit.SOME_OTHER_ITEM.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}