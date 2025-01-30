package com.kingtyphon.kaijucraft.item.armor;

import com.kingtyphon.kaijucraft.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class Kaiju_No8_SpawnEgg extends Item {

    public Kaiju_No8_SpawnEgg(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (!level.isClientSide) { // Server-side logic
            // Perform ray tracing to detect the targeted block
            BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos().relative(hitResult.getDirection());

                // Spawn the custom entity
                var entityType = EntityInit.KAIJU_NO8.get();
                var entity = entityType.spawn((ServerLevel) level, itemstack, player, blockPos, MobSpawnType.SPAWN_EGG, true, false);

                if (entity != null) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1); // Consume one item
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
                    return InteractionResultHolder.consume(itemstack);
                }
            }
        }
        return InteractionResultHolder.pass(itemstack);
    }
}
