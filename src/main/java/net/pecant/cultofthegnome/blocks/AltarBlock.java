package net.pecant.cultofthegnome.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.pecant.cultofthegnome.blockentities.AltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {

    public AltarBlock(Properties properties) {super(properties);}

    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 16, 16);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity bEntity = level.getBlockEntity(pos);
            if (bEntity instanceof AltarBlockEntity altar) {
                NetworkHooks.openScreen(((ServerPlayer) player), altar, pos);
            } else {
                throw new IllegalStateException("The container provider is missing!");
            }
            return InteractionResult.CONSUME;
        }
    }

    public void onRemove(BlockState removedState, Level level, BlockPos pos, BlockState state, boolean flag) {
        if (!removedState.is(state.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof AltarBlockEntity) {
                Containers.dropContents(level, pos, (AltarBlockEntity)blockentity);
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(removedState, level, pos, state, flag);
        }
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

}
