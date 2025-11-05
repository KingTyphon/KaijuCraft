package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.common.block.KaijuPartsBlock;
import com.kingtyphon.kaijucraft.common.block.KingTyphonPopBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static net.minecraft.world.item.Items.registerBlock;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, KaijuCraft.MODID);
    public static final RegistryObject<Block> KAIJU_PARTS = registerBlock("kaiju_parts",
            () -> new KaijuPartsBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.SLIME_BLOCK)));
    public static final RegistryObject<Block> KINGTYPHON_POP = registerBlock("kingtyphon_pop",
            () -> new KingTyphonPopBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.FUNGUS)));

private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
    RegistryObject<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn);
    return toReturn;
}

private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
    return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
}

public static void register(IEventBus eventBus) {
    BLOCKS.register(eventBus);
}
}
