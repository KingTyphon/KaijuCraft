package com.kingtyphon.kaijucraft.item.exam;

import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntranceExamItem extends Item {
    public EntranceExamItem(Properties pProperties) {
        super(pProperties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        pPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kapability ->{
            //This enables the flag that allows them to get their percentage
        });
        return super.use(pLevel, pPlayer, pUsedHand);

    }
}
