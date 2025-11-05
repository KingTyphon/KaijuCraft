package com.kingtyphon.kaijucraft.init;

import com.kingtyphon.kaijucraft.KaijuCraft;
import com.kingtyphon.kaijucraft.entity.kaiju.*;
import com.kingtyphon.kaijucraft.entity.npc.Kikoru;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, KaijuCraft.MODID);
    public static final RegistryObject<EntityType<PhaneroplusEntity>> PHANEROPLUS =
            ENTITY_TYPES.register("phaneroplus",
                    () -> EntityType.Builder.<PhaneroplusEntity>of(PhaneroplusEntity::new, MobCategory.MONSTER).sized(2.75F,3.0F)
                            .build("phaneroplus"));
    public static final RegistryObject<EntityType<LarvaEntity>> LARVA =
            ENTITY_TYPES.register("larva",()-> EntityType.Builder.of(LarvaEntity::new, MobCategory.CREATURE).sized(1, 1).build("larva"));
    public static final RegistryObject<EntityType<TrichonephilaEntity>> TRICHONEPHILA =
            ENTITY_TYPES.register("trichonephila",()-> EntityType.Builder.of(TrichonephilaEntity::new, MobCategory.CREATURE).sized(3.0F,4.0F).build("trichonephila"));
    public static final RegistryObject<EntityType<Kikoru>> KIKORU =
            ENTITY_TYPES.register("kikoru",()-> EntityType.Builder.of(Kikoru::new, MobCategory.CREATURE).sized(0.75F,2.0F).build("kikoru"));

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
    public static final RegistryObject<EntityType<OrganWeakSpotEntity>> ORGAN_WEAK_SPOT =
            ENTITY_TYPES.register("organ_weak_spot",
                    () -> EntityType.Builder.<OrganWeakSpotEntity>of(OrganWeakSpotEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F)
                            .build("organ_weak_spot"));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}
