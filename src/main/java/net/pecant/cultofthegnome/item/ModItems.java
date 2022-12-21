package net.pecant.cultofthegnome.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pecant.cultofthegnome.CultOfTheGnome;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CultOfTheGnome.MOD_ID);


    // RED GNOME HAT
    public static final RegistryObject<Item> HAT = ITEMS.register("hat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.GNOME_TAB)));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
