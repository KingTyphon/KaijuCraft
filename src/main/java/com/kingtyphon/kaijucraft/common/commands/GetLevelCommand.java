package com.kingtyphon.kaijucraft.common.commands;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetLevelCommand {

    public GetLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("getKaijuLevel")
                .requires(commandSource -> commandSource.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.entity())
                        .executes(context -> {
                            ServerPlayer selectedPlayer = (ServerPlayer) EntityArgument.getEntity(context, "player");
                            return getKaijuLevel(selectedPlayer);
                        })
        ));
    }

    private static int getKaijuLevel(ServerPlayer player) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            int level = capability.getLevel();
            player.sendSystemMessage(Component.translatable("Current Level:" + level));
        });
        return 1; // Command succeeded
    }
}
