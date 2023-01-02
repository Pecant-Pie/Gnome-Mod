package net.pecant.cultofthegnome.events;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.client.models.GnomeEntityModel;
import net.pecant.cultofthegnome.client.renderer.GnomeEntityRenderer;
import net.pecant.cultofthegnome.init.EntityInit;

@Mod.EventBusSubscriber(modid = CultOfTheGnome.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD,
value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.GNOME.get(), GnomeEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GnomeEntityModel.LAYER_LOCATION, GnomeEntityModel::createBodyLayer);
    }
}
