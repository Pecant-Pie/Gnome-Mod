package net.pecant.cultofthegnome.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

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
    public StatueBlock(Properties prop) {
        super(prop);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.STATUE.get().create(pos, state);
    }

//    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
//        return EntityBlock.super.getTicker(p_153212_, p_153213_, p_153214_);
//    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {

            ItemStack heldItem = player.getItemInHand(hand);
            // Check if the held item is a hat, the player is not still invulnerable from being hit, and that it's not maxed out on gnomes
            if (heldItem.is(ItemInit.HAT.get()) && getGnomes(state) < MAX_GNOMES && !player.isInvulnerable()) {

                if (!player.isCreative()) {
                    heldItem.shrink(1);
                }

                // Steal some life away to give to the gnome
                player.hurt(DamageSource.MAGIC, 2);

                // Summon a gnome on top of the statue block
                var gnome = new GnomeEntity(EntityInit.GNOME.get(), level);
                Vec3 hitVector = hitResult.getLocation();
                gnome.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);

                if (addGnome(state, level, pos, gnome)) {
                    level.addFreshEntity(gnome);
                }
                return InteractionResult.CONSUME;
            }
            else
                return InteractionResult.SUCCESS;
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
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

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(GNOMES);
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
