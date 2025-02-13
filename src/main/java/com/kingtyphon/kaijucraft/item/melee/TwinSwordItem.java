package com.kingtyphon.kaijucraft.item.melee;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TwinSwordItem extends Item {
    public TwinSwordItem(Properties pProperties) {
        super(pProperties);
    }
    public static boolean isDualWielding(LivingEntity entity) {
        ItemStack mainHand = entity.getMainHandItem();
        ItemStack offHand = entity.getOffhandItem();
        return mainHand.getItem() instanceof TwinSwordItem && offHand.getItem() instanceof TwinSwordItem;
    }
}
