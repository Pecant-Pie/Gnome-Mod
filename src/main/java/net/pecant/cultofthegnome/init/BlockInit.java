package net.pecant.cultofthegnome.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.blocks.AltarBlock;
import net.pecant.cultofthegnome.blocks.StatueBlock;
import net.pecant.cultofthegnome.item.ModCreativeModeTab;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CultOfTheGnome.MOD_ID);

    // Gnome statue
    public static final RegistryObject<StatueBlock> STATUE = registerBlock("statue",
            () -> new StatueBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3f).requiresCorrectToolForDrops()
                    .noOcclusion()), ModCreativeModeTab.GNOME_TAB);

    // Altar
    public static final RegistryObject<AltarBlock> ALTAR = registerBlock("altar",
            () -> new AltarBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(3f).requiresCorrectToolForDrops()), ModCreativeModeTab.GNOME_TAB);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab){
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

}
