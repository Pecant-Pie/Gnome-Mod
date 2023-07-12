package net.pecant.cultofthegnome.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.blockentities.AltarBlockEntity;
import net.pecant.cultofthegnome.entities.GnomeEntity;
import net.pecant.cultofthegnome.init.BlockInit;

import javax.annotation.Nullable;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.Optional;

public class DepositItemGoal extends MoveToBlockGoal {
    private GnomeEntity gnome;
    public DepositItemGoal(GnomeEntity gnome, double speedModifier) {
        super(gnome, speedModifier, 10, 2);
        this.gnome = gnome;
    }


    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
//        if (state.is(BlockInit.ALTAR.get()))
//            CultOfTheGnome.LOGGER.debug("found altar"); // DEBUG
        return state.is(BlockInit.ALTAR.get());
    }

    @Override
    public void tick() {
        super.tick();
        if (isReachedTarget() && !gnome.getMainHandItem().isEmpty()) {

            AltarBlockEntity altar = null;
            if (gnome.getLevel().getBlockEntity(blockPos) instanceof AltarBlockEntity blockEntity) {
                altar = blockEntity;
            } else CultOfTheGnome.LOGGER.debug("Target is not an altar?? Its: " + gnome.getLevel().getBlockEntity(blockPos).toString());
            IItemHandler iItemHandler = getItemCapability(altar);

            if (iItemHandler != null) {
                ItemStack stackLeft = ItemHandlerHelper.insertItemStacked(iItemHandler, gnome.getMainHandItem(), false);
                if (stackLeft.getCount() != 0) {
                    Block.popResource(gnome.level, blockPos.above(), stackLeft);
                    CultOfTheGnome.LOGGER.debug("Altar full at pos: " + blockPos.toString()); // DEBUG
                }
            }
            else {
                Block.popResource(gnome.level, this.blockPos.above(), gnome.getMainHandItem());
                CultOfTheGnome.LOGGER.debug("Item handler missing at pos: " + blockPos.toString()); // DEBUG
            }
            // Remove the item!
            gnome.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    // this function has been copied from the Ars Nouveau source code,
    // as allowed by Section 3 of its license. (ten or fewer lines in length)
    public @Nullable IItemHandler getItemCapability(BlockEntity blockEntity) {
        if (blockEntity != null && blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
            var cap = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve();
            if (cap.isPresent()) {
                return cap.get();
            }
        }
        CultOfTheGnome.LOGGER.debug("No capability found for blockEntity: " + blockEntity.toString());
        return null;
    }


    @Override
    public boolean canUse() {
        return !gnome.getMainHandItem().isEmpty() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !gnome.getMainHandItem().isEmpty() && super.canContinueToUse();
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(10 + mob.getRandom().nextInt(30));
    }
}
