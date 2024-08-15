package com.kingtyphon.kaijucraft.datagen;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper){
        super(output, KaijuCraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){
        simpleItem(ItemInit.MDEFENSEFORMAL_BOOTS);
        simpleItem(ItemInit.MDEFENSEFORMAL_LEGGINGS);
        simpleItem(ItemInit.MDEFENSEFORMAL_CHESTPLATE);

        simpleItem(ItemInit.MDEFENSECASUAL_BOOTS);
        simpleItem(ItemInit.MDEFENSECASUAL_LEGGINGS);
        simpleItem(ItemInit.MDEFENSECASUAL_CHESTPLATE);


    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0", new ResourceLocation(KaijuCraft.MODID,"item/" + item.getId().getPath()));
    }
}
