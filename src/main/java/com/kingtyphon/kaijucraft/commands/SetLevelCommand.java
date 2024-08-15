package com.kingtyphon.kaijucraft.commands;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetLevelCommand {

    public SetLevelCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("setKaijuLevel")
            .requires(commandSource -> commandSource.hasPermission(2)) // Permission level 2: OPs
            .then(Commands.argument("level", IntegerArgumentType.integer(1))
                    .executes(context -> {
                        int level = IntegerArgumentType.getInteger(context, "level");
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        return setKaijuLevel(player, level);
                    })
            )
    );}
    private static int setKaijuLevel(ServerPlayer player, int level) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            capability.setLevel(level);
            player.sendSystemMessage(Component.literal("Current Level set to " + level));
        });
        return 1; // Command succeeded
    }
}

