package com.kingtyphon.kaijucraft.item;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.init.BlockInit;
import com.kingtyphon.kaijucraft.init.ItemInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class KaijuCreativeModeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaijuCraft.MODID);

    public static final RegistryObject<CreativeModeTab> KAIJU_TAB = CREATIVE_MODE_TABS.register("kaiju_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(BlockInit.KAIJU_PARTS.get()))
                    .title(Component.translatable("creativetab.kaiju_tab"))
                    .displayItems((displayParameters, output) -> {
                        //Armor - Formal Outfit
                        output.accept(ItemInit.MDEFENSEFORMAL_CHESTPLATE.get());
                        output.accept(ItemInit.MDEFENSEFORMAL_LEGGINGS.get());
                        output.accept(ItemInit.MDEFENSEFORMAL_BOOTS.get());

                        // Armor - Casual Outfit
                        output.accept(ItemInit.MDEFENSECASUAL_CHESTPLATE.get());
                        output.accept(ItemInit.MDEFENSECASUAL_LEGGINGS.get());
                        output.accept(ItemInit.MDEFENSECASUAL_BOOTS.get());
                        output.accept(ItemInit.NHCLEANING_CHESTPLATE.get());
                        output.accept(ItemInit.NHCLEANING_LEGGINGS.get());
                        output.accept(ItemInit.NHCLEANING_BOOTS.get());
                        output.accept(ItemInit.GAS_MASK.get());
//                        output.accept(ItemInit.SKELLYKAIJU_HELMET.get());
//                        output.accept(ItemInit.SKELLYKAIJU_CHESTPLATE.get());
//                        output.accept(ItemInit.SKELLYKAIJU_LEGGINGS.get());
//                        output.accept(ItemInit.SKELLYKAIJU_BOOTS.get());

                        // Armor - Combat Outfit
                        output.accept(ItemInit.MCOMBAT_HELMET.get());
                        output.accept(ItemInit.MCOMBAT_CHESTPLATE.get());
                        output.accept(ItemInit.MCOMBAT_LEGGINGS.get());
                        output.accept(ItemInit.MCOMBAT_BOOTS.get());

                        output.accept(ItemInit.ENTRANCEEXAMLETTER.get());
                        output.accept(ItemInit.ACCEPTANCELETTER.get());
                        output.accept(ItemInit.KAIJU_NO8_SPAWN_EGG.get());
                        output.accept(ItemInit.TRICHONEPHILA_SPAWN_EGG.get());
                        output.accept(ItemInit.PRIMIGENIUS_SPAWN_EGG.get());
                        output.accept(ItemInit.LARVA_SPAWN_EGG.get());
                        output.accept(ItemInit.KAIJU_MUSCLE.get());
                        output.accept(ItemInit.ROTTEN_KAIJU_MUSCLE.get());
                        // Add other items here
                        output.accept(ItemInit.GLOCK17GEN4.get());
                        output.accept(ItemInit.SIGSAUERSHORTRIFLE.get());
                        output.accept(ItemInit.PNEUMATICCHAINSAW.get());
                        output.accept(ItemInit.CANNON.get());
                        output.accept(ItemInit.TWINSWORD.get());
                        output.accept(ItemInit.BATTLEAXE.get());
                        output.accept(BlockInit.KINGTYPHON_POP.get());

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}