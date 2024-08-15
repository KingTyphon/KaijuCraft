package com.kingtyphon.kaijucraft.networking;

import com.kingtyphon.kaijucraft.capabilities.IKaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class KaijuHelper {
    private int level;
    public static ServerPlayer getPlayer(NetworkEvent.Context context){
        return context.getSender();
    }
}
