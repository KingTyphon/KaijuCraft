package com.kingtyphon.kaijucraft.commands;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetSkillPointCommand{

    public SetSkillPointCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setSkillPoint")
                .requires(commandSource -> commandSource.hasPermission(2)) // Permission level 2: OPs
                .then(Commands.argument("SP", IntegerArgumentType.integer(1))
                        .then(Commands.argument("player", EntityArgument.player()).executes(context -> {
                                    int SP = IntegerArgumentType.getInteger(context, "SP");
                                    ServerPlayer selectedPlayer = (ServerPlayer) EntityArgument.getEntity(context, "player");
                                    return setSkillPoint(selectedPlayer, SP);
                                })
                        )
                ));
    }
    private static int setSkillPoint(ServerPlayer player, int sp) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            capability.setSP(sp);
            ModMessages.send(new KaijuPacket(capability),player);
            player.sendSystemMessage(Component.translatable("Current Skill Points set to " + sp).withStyle(ChatFormatting.DARK_GREEN));
        });
        return 1; // Command succeeded
    }
}