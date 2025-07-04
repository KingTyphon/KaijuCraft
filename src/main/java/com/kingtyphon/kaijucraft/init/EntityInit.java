package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.*;
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
            ENTITY_TYPES.register("larva",()-> EntityType.Builder.of(LarvaEntity::new, MobCategory.CREATURE).sized(1, 1).build("larva"));
    public static final RegistryObject<EntityType<TrichonephilaEntity>> TRICHONEPHILA =
            ENTITY_TYPES.register("trichonephila",()-> EntityType.Builder.of(TrichonephilaEntity::new, MobCategory.CREATURE).sized(3.0F,4.0F).build("trichonephila"));

    public static final RegistryObject<EntityType<Kaiju_no8Entity>> KAIJU_NO8 =
            ENTITY_TYPES.register("kaiju_no8",()-> EntityType.Builder.of(Kaiju_no8Entity::new, MobCategory.CREATURE)
                    .sized(1, 2).build("kaiju_no8"));

    public static final RegistryObject<EntityType<KaijuPartEntity>> KAIJU_PART =
            ENTITY_TYPES.register("kaijupart",
                    () -> EntityType.Builder.<KaijuPartEntity>of(KaijuPartEntity::new, MobCategory.MONSTER)
                            .build("kaijupart"));

    public static final RegistryObject<EntityType<PrimigeniusEntity>> PRIMIGENIUS =
            ENTITY_TYPES.register("primigenius",
                    () -> EntityType.Builder.<PrimigeniusEntity>of(PrimigeniusEntity::new, MobCategory.MONSTER).sized(3.0F,4.0F)
                            .build("primigenius"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
