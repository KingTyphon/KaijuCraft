package com.kingtyphon.kaijucraft.networking.packets;

import com.kingtyphon.kaijucraft.init.ItemInit;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;


public class GasMaskTogglePacket {
    public GasMaskTogglePacket() {}

    public GasMaskTogglePacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public static void handle(GasMaskTogglePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            ItemStack headSlotItem = player.getInventory().armor.get(3); // Helmet slot
            int gasMaskSlot = -1;

            // Check if the player is wearing a gas mask
            boolean isWearingGasMask = !headSlotItem.isEmpty() && headSlotItem.is(ItemInit.GAS_MASK.get());

            // Find gas mask in inventory
            if (!isWearingGasMask) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && stack.is(ItemInit.GAS_MASK.get())) {
                        gasMaskSlot = i;
                        break;
                    }
                }
            }

            // **Exit early if no gas mask is found anywhere**
            if (!isWearingGasMask && gasMaskSlot == -1) {
                return;
            }

            boolean removed = false;

            if (isWearingGasMask) {
                // Ensure inventory space before removing
                if (player.getInventory().getFreeSlot() != -1) {
                    player.getInventory().add(headSlotItem); // Move gas mask to inventory
                    player.getInventory().armor.set(3, ItemStack.EMPTY); // Remove from helmet slot
                    removed = true;
                }
            } else {
                ItemStack gasMask = player.getInventory().removeItem(gasMaskSlot, 1);

                if (!headSlotItem.isEmpty()) {
                    // Ensure space for swapping helmet
                    if (player.getInventory().getFreeSlot() != -1) {
                        player.getInventory().add(headSlotItem); // Store current helmet
                        player.getInventory().armor.set(3, gasMask); // Equip gas mask
                    }
                } else {
                    player.getInventory().armor.set(3, gasMask); // Equip gas mask
                }
            }

            // Sync inventory
            player.inventoryMenu.broadcastChanges();
            player.inventoryMenu.slotsChanged(player.getInventory());

            // Notify clients
            ModMessages.sendToAll(new SyncGasMaskPacket(player.getId(), removed));
        });
        ctx.get().setPacketHandled(true);
    }

}
