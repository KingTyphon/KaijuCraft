package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.Kaiju_no8Entity;
import com.kingtyphon.kaijucraft.entity.kaiju.LarvaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KaijuCraft.MODID);

    public static final RegistryObject<EntityType<LarvaEntity>> LARVA =
            ENTITY_TYPES.register("larva",()-> EntityType.Builder.of(LarvaEntity::new, MobCategory.CREATURE).sized(1, 2).build("larva"));

    public static final RegistryObject<EntityType<Kaiju_no8Entity>> KAIJU_NO8 =
            ENTITY_TYPES.register("kaiju_no8",()-> EntityType.Builder.of(Kaiju_no8Entity::new, MobCategory.CREATURE)
                    .sized(1, 2).build("kaiju_no8"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
