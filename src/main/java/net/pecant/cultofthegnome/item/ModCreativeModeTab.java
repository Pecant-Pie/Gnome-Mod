package net.pecant.cultofthegnome.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.pecant.cultofthegnome.init.ItemInit;

public class ModCreativeModeTab {
    public static final CreativeModeTab GNOME_TAB = new CreativeModeTab("gnometab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.HAT.get());
        }
    };
}
