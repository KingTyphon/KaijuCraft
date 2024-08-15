package com.kingtyphon.kaijucraft.event;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.commands.GetLevelCommand;
import com.kingtyphon.kaijucraft.commands.SetLevelCommand;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

@Mod.EventBusSubscriber(modid = KaijuCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) {
        new GetLevelCommand(event.getDispatcher());
       new SetLevelCommand(event.getDispatcher());

        ConfigCommand.register(event.getDispatcher());}

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(KaijuCraft.MODID, "kaiju_capability"), new KaijuProvider());
            }
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getOriginal().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(oldCap -> {
                event.getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(newCap -> {
                    newCap.copyFrom(oldCap); // Copy data from old to new capability
                });
            });
        }
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Sync capability data with client
        event.getEntity().getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            CompoundTag nbt = capability.serializeNBT();
            ModMessages.send(new KaijuPacket(nbt), (ServerPlayer) event.getEntity());
        });
    }
}
