package net.pecant.cultofthegnome;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.pecant.cultofthegnome.init.BlockEntityInit;
import net.pecant.cultofthegnome.init.BlockInit;
import net.pecant.cultofthegnome.init.EntityInit;
import net.pecant.cultofthegnome.init.ItemInit;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CultOfTheGnome.MOD_ID)
public class CultOfTheGnome
{
    public static final String MOD_ID = "cultofthegnome";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CultOfTheGnome()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        EntityInit.ENTITIES.register(modEventBus);
        BlockEntityInit.BLOCK_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
