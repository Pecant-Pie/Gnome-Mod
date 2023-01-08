package net.pecant.cultofthegnome.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.entities.GnomeEntity;

import static net.minecraftforge.registries.ForgeRegistries.*;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ENTITY_TYPES, CultOfTheGnome.MOD_ID);

//    public static final MobCategory gnomes = new MobCategory("gnomes", 10, true, true, 128);
    public static final RegistryObject<EntityType<GnomeEntity>> GNOME = ENTITIES.register("gnome", () -> EntityType.Builder.of(GnomeEntity::new,
        MobCategory.MISC).sized(0.25f,0.625f).build(CultOfTheGnome.MOD_ID + ":gnome"));

}
