package net.pecant.cultofthegnome.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.blockentities.AltarBlockEntity;
import net.pecant.cultofthegnome.blockentities.StatueBlockEntity;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CultOfTheGnome.MOD_ID);

    public static final RegistryObject<BlockEntityType<StatueBlockEntity>> STATUE = BLOCK_ENTITIES.register("statue",
            () -> BlockEntityType.Builder.of(StatueBlockEntity::new, BlockInit.STATUE.get()).build(null));

    public static final RegistryObject<BlockEntityType<AltarBlockEntity>> ALTAR = BLOCK_ENTITIES.register("altar",
            () -> BlockEntityType.Builder.of(AltarBlockEntity::new, BlockInit.ALTAR.get()).build(null));
}
