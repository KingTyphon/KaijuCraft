package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.mojang.logging.LogUtils;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class AnimationPacket {
    private final int usingPlayer;
    private static final Logger LOGGER = LogUtils.getLogger();

    public AnimationPacket(int usingPlayer) {
        this.usingPlayer = usingPlayer;
    }
    public AnimationPacket(FriendlyByteBuf buf) {
        this.usingPlayer = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(usingPlayer);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {

        supplier.get().enqueueWork(() -> {
            NetworkEvent.Context context = supplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            Player clientPlayer = Minecraft.getInstance().player;
            if (clientPlayer != null) {
                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) clientPlayer).get(new ResourceLocation(KaijuCraft.MODID, "animation"));

            if (animation != null) {
                //You can set an animation from anywhere ON THE CLIENT
                //Do not attempt to do this on a server, that will only fail

                animation.setAnimation(new KeyframeAnimationPlayer(PlayerAnimationRegistry.getAnimation(new ResourceLocation("kaijucraft", "idle"))));
                //You might use  animation.replaceAnimationWithFade(); to create fade effect instead of sudden change
                //See javadoc for details
            }
            }
        }

            //Kaiju_no8Entity entity = EntityInit.KAIJU_NO8.get().create(level);

        });
        return true;
    }

}
