package net.pecant.cultofthegnome.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab GNOME_TAB = new CreativeModeTab("gnometab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.HAT.get());
        }
    };
}
