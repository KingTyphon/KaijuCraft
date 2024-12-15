package com.kingtyphon.kaijucraft.commands;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.sun.jdi.connect.Connector;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetRandomizerFlagCommand  {
    public SetRandomizerFlagCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("setRandomizerFlag")
            .requires(commandSource -> commandSource.hasPermission(2)) // Permission level 2: OPs
            .then(Commands.argument("flag", BoolArgumentType.bool())
                    .then(Commands.argument("player", EntityArgument.player()).executes(context -> {
                                Boolean flag = BoolArgumentType.getBool(context, "flag");
                                ServerPlayer selectedPlayer = (ServerPlayer) EntityArgument.getEntity(context, "player");
                                return setFlag(selectedPlayer, flag);
                            })
                    )
            ));
}
private static int setFlag(ServerPlayer player, boolean flag) {
    player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
        capability.setPercentRandomizer(flag);
        ModMessages.send(new KaijuPacket(capability),player);
        if(flag == true){
            player.sendSystemMessage(Component.translatable("You are apart of the Japanese Anti-Kaiju Defense Force").withStyle(ChatFormatting.GREEN));
        }else{
            player.sendSystemMessage(Component.translatable("You are no longer apart of the Japanese Anti-Kaiju Defense Force").withStyle(ChatFormatting.RED));
        }
    });
    return 1; // Command succeeded
}}