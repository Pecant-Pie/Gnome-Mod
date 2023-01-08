package net.pecant.cultofthegnome.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.pecant.cultofthegnome.init.BlockEntityInit;

public class StatueBlockEntity extends BlockEntity {
    public StatueBlockEntity(BlockPos pos, BlockState state) {

        super(BlockEntityInit.STATUE.get(), pos, state);
    }
}
