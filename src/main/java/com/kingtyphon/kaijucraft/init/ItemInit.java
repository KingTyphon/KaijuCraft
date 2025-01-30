package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.animations.GunAnimation3rdPerson;
import com.kingtyphon.kaijucraft.item.PneumaticChainsawItem;
import com.kingtyphon.kaijucraft.item.armor.*;
import com.kingtyphon.kaijucraft.item.exam.AcceptanceLetterItem;
import com.kingtyphon.kaijucraft.item.exam.EntranceExamItem;
import com.kingtyphon.kaijucraft.item.guns.Glock17Gen4;
import com.kingtyphon.kaijucraft.item.guns.SigSauerShortRifleItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, KaijuCraft.MODID);

    //Armor
    public static final RegistryObject<Item> MDEFENSEFORMAL_LEGGINGS = ITEMS.register("mdefenseformal_leggings", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSEFORMAL_CHESTPLATE = ITEMS.register("mdefenseformal_chestplate", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSEFORMAL_BOOTS = ITEMS.register("mdefenseformal_boots", ()-> new FormalArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> MDEFENSECASUAL_LEGGINGS = ITEMS.register("mdefensecasual_leggings", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSECASUAL_CHESTPLATE = ITEMS.register("mdefensecasual_chestplate", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MDEFENSECASUAL_BOOTS = ITEMS.register("mdefensecasual_boots", ()-> new CasualArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> MCOMBAT_HELMET = ITEMS.register("mcombat_helmet", ()-> new CombatArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> MCOMBAT_LEGGINGS = ITEMS.register("mcombat_leggings", ()-> new CombatArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> MCOMBAT_CHESTPLATE = ITEMS.register("mcombat_chestplate", ()-> new CombatArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> MCOMBAT_BOOTS = ITEMS.register("mcombat_boots", ()-> new CombatArmorItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.BOOTS, new Item.Properties()));

    public static final RegistryObject<Item> NHCLEANING_CHESTPLATE = ITEMS.register("nhcleaning_chestplate", ()-> new NHCleaningItem(KaijuArmorMaterial.KAIJU, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    //Guns
    public static final RegistryObject<Item> GLOCK17GEN4 = ITEMS.register("glock17gen4", ()-> new Glock17Gen4(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SIGSAUERSHORTRIFLE = ITEMS.register("sigsauershortrifle", () -> new SigSauerShortRifleItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> PNEUMATICCHAINSAW = ITEMS.register("pneumatic_chainsaw", () ->(new PneumaticChainsawItem(new Item.Properties().stacksTo(1))));
    //Spawn Eggs
    public static final RegistryObject<Item> KAIJU_NO8_SPAWN_EGG = ITEMS.register("kaiju_no8_spawn_egg", ()-> new Kaiju_No8_SpawnEgg(new Item.Properties()));
    public static final RegistryObject<Item> LARVA_SPAWN_EGG = ITEMS.register("larva_spawn_egg", ()-> new LarvaSpawnEgg(new Item.Properties()));

    //Letters
    public static final RegistryObject<Item> ACCEPTANCELETTER = ITEMS.register("acceptanceletter", ()-> new AcceptanceLetterItem(new Item.Properties()));
    public static final RegistryObject<Item> ENTRANCEEXAMLETTER = ITEMS.register("entranceexamletter", ()-> new EntranceExamItem(new Item.Properties()));


    public static void register(IEventBus bus){ITEMS.register(bus);}
}
