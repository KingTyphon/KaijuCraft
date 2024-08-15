package com.kingtyphon.kaijucraft.commands;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetLevelCommand {
    public GetLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("getKaijuLevel").requires(commandSource -> commandSource.hasPermission(2)).executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            return getKaijuLevel(player);
                        })
        );
    }

    private static int getKaijuLevel(ServerPlayer player) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            int level = capability.getLevel();
            player.sendSystemMessage(Component.literal("Current Level:" + level));
        });
        return 1; // Command succeeded
    }
}
