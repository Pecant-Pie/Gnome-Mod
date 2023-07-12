package net.pecant.cultofthegnome.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.blockentities.StatueBlockEntity;
import net.pecant.cultofthegnome.entities.GnomeEntity;
import net.pecant.cultofthegnome.init.BlockEntityInit;
import net.pecant.cultofthegnome.init.EntityInit;
import net.pecant.cultofthegnome.init.ItemInit;

import org.jetbrains.annotations.Nullable;

public class StatueBlock extends Block implements EntityBlock {
    public static final int MAX_GNOMES = 5;
    public static final int MIN_GNOMES = 0;

    public static final IntegerProperty GNOMES = IntegerProperty.create("gnomes", MIN_GNOMES, MAX_GNOMES);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = Block.box(4,0,4,12,16,12);
    public StatueBlock(Properties prop) {
        super(prop);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.STATUE.get().create(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GNOMES);
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        ItemStack heldItem = player.getItemInHand(hand);
        // Check if the held item is a hat, the player is not still invulnerable from being hit, and that it's not maxed out on gnomes
        if (heldItem.is(ItemInit.HAT.get()) && getGnomes(state) < MAX_GNOMES) {
            // Steal some life away to give to the gnome
            boolean hurt = player.hurt(DamageSource.MAGIC, 2) || player.isCreative();

            if (!level.isClientSide) {

                if (!player.isCreative() && hurt) {
                    heldItem.shrink(1);
                }
            }

            if (hurt) {
                // Summon a gnome on top of the statue block
                var gnome = new GnomeEntity(EntityInit.GNOME.get(), level);
                Vec3 hitVector = hitResult.getLocation();
                gnome.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

                if (addGnome(state, level, pos, gnome)) {
                    level.addFreshEntity(gnome);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else return InteractionResult.FAIL;
        }
        else {
            return InteractionResult.PASS;
        }

    }

    // Increments the blockstate gnome count and adds the Statue block entity to the gnome's 'statue' field
    // Also adds the gnome to the block entity's list within the setStatue method
    public boolean addGnome(BlockState state, Level level, BlockPos pos, GnomeEntity gnome) {
        // sets the gnome statue stuff within the if condition
        if (getGnomes(state) < MAX_GNOMES && gnome.setStatue(level.getBlockEntity(pos), pos))
        {
            gnome.setId(getGnomes(state)); // sets the gnome id to the previous count
            level.setBlock(pos, state.setValue(GNOMES, getGnomes(state) + 1), 3);
            return true;
        }
        else {
            return false;
        }
    }

    public int getGnomes(BlockState state) {
        return state.getValue(GNOMES);
    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);

        if (!level.isClientSide()) {

        if (level.getBlockEntity(pos) instanceof StatueBlockEntity statue) {
            statue.cullGnomes();
        }


        }


    }

}
