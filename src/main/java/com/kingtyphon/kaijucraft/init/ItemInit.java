package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.item.armor.CasualArmorItem;
import com.kingtyphon.kaijucraft.item.armor.FormalArmorItem;
import com.kingtyphon.kaijucraft.item.armor.KaijuArmorMaterial;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KaijuCraft.MODID);

    public static final RegistryObject<Item> MDEFENSEFORMAL_LEGGINGS = ITEMS.register("mdefenseformal_leggings", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSEFORMAL_CHESTPLATE = ITEMS.register("mdefenseformal_chestplate", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSEFORMAL_BOOTS = ITEMS.register("mdefenseformal_boots", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> MDEFENSECASUAL_LEGGINGS = ITEMS.register("mdefensecasual_leggings", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSECASUAL_CHESTPLATE = ITEMS.register("mdefensecasual_chestplate", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSECASUAL_BOOTS = ITEMS.register("mdefensecasual_boots", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.BOOTS, new Item.Properties()));

    //Guns
    public static final RegistryObject<Item> GLOCK17GEN4 = ITEMS.register("glock17gen4", ()-> new Glock17Gen4(new Item.Properties()));


    public static void register(IEventBus bus){ITEMS.register(bus);}
}
