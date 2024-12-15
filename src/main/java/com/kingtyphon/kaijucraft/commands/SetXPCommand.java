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

public class SetXPCommand {
    public SetXPCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setKaijuXP")
                .requires(commandSource -> commandSource.hasPermission(2)) // Permission level 2: OPs
                .then(Commands.argument("xp", IntegerArgumentType.integer(1))
                        .then(Commands.argument("player", EntityArgument.player()).executes(context -> {
                                    int xp = IntegerArgumentType.getInteger(context, "xp");
                                    ServerPlayer selectedPlayer = (ServerPlayer) EntityArgument.getEntity(context, "player");
                                    return setKaijuXP(selectedPlayer, xp);
                                })
                        )
                ));
    }
    private static int setKaijuXP(ServerPlayer player, int xp) {
        player.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(capability -> {
            capability.setXP(xp);
            //CompoundTag nbtdata = capability.serializeNBT();
            ModMessages.send(new KaijuPacket(capability),player);
            player.sendSystemMessage(Component.translatable("Current XP set to " + xp).withStyle(ChatFormatting.DARK_GRAY));
        });
        return 1; // Command succeeded
    }
}
