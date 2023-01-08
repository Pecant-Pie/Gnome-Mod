package net.pecant.cultofthegnome.events;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.entities.GnomeEntity;
import net.pecant.cultofthegnome.init.EntityInit;

@Mod.EventBusSubscriber(modid = CultOfTheGnome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {

    // This event is used for adding spawn placements for an entity
    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
//        event.enqueueWork(() -> {
//            SpawnPlacements.register(EntityInit.GNOME.get(), SpawnPlacements.Type.ON_GROUND,
//                    Heightmap.Types.WORLD_SURFACE, GnomeEntity::canSpawn);
//        });
    }

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.GNOME.get(), GnomeEntity.getGnomeAttributes().build());
    }


}
