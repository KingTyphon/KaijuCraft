package com.kingtyphon.kaijucraft.common.commands;

import com.kingtyphon.kaijucraft.common.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetStatCommand {
    public SetStatCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setStat")
                .requires(commandSource -> commandSource.hasPermission(2)) // Permission level 2: OPs
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("stat", StringArgumentType.string())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(context -> {
                                            String stat = StringArgumentType.getString(context, "stat");
                                            ServerPlayer selectedPlayer = EntityArgument.getPlayer(context, "player");
                                            int amount = IntegerArgumentType.getInteger(context, "amount");
                                            return setStat(selectedPlayer, stat, amount);
                                        })
                                )
                        )
                )
        );
    }

    private static int setStat(ServerPlayer player, String stat, int amount) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            switch (stat.toLowerCase()) {
                case "melee":
                    capability.setMelee(amount);
                    player.sendSystemMessage(Component.translatable("Your Melee is currently: " + capability.getMelee()).withStyle(ChatFormatting.GREEN));
                    break;
                case "mind":
                    capability.setMind(amount);
                    player.sendSystemMessage(Component.translatable("Your Mind is currently: " + capability.getMind()).withStyle(ChatFormatting.GREEN));
                    break;
                case "range":
                    capability.setRange(amount);
                    player.sendSystemMessage(Component.translatable("Your Range is currently: " + capability.getRange()).withStyle(ChatFormatting.GREEN));
                    break;
                default:
                    player.sendSystemMessage(Component.translatable("Invalid stat: " + stat).withStyle(ChatFormatting.RED));
                    break; // Command failed
            }
            // Sync the capability to the client
            ModMessages.send(new KaijuPacket(capability), player);
        });
        return 1; // Command succeeded
    }
}