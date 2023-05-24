package net.pecant.cultofthegnome.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.pecant.cultofthegnome.CultOfTheGnome;
import net.pecant.cultofthegnome.entities.GnomeEntity;
import net.pecant.cultofthegnome.init.BlockInit;

public class DepositItemGoal extends MoveToBlockGoal {
    private GnomeEntity gnome;
    public DepositItemGoal(GnomeEntity gnome, double speedModifier) {
        super(gnome, speedModifier, 10, 2);
        this.gnome = gnome;
    }


    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        if (state.is(BlockInit.ALTAR.get()))
            CultOfTheGnome.LOGGER.debug("found altar"); // DEBUG
        return state.is(BlockInit.ALTAR.get());
    }

    @Override
    public void tick() {
        super.tick();
        if (isReachedTarget() && !gnome.getMainHandItem().isEmpty()) {
            Block.popResource(gnome.level, this.blockPos.above(), gnome.getMainHandItem());
            gnome.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            CultOfTheGnome.LOGGER.debug("popped item"); // DEBUG
//            if (!this.gnome.getLevel().getBlockState(this.blockPos).isAir()) {
//                gnome.depositHeldItem(this.blockPos);
//            }
        }
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
