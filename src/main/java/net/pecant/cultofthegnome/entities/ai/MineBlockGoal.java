package net.pecant.cultofthegnome.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.pecant.cultofthegnome.entities.GnomeEntity;

import java.util.List;

public class MineBlockGoal extends MoveToBlockGoal {

    private final GnomeEntity gnome;
    private final double reachDistance;
    private final TagKey<Block> tag;
    private final ItemStack tool;
    private final double breakTime;


    public MineBlockGoal(GnomeEntity gnome, double speedModifier, TagKey<Block> tag, double reachDistance, int searchDistance, int verticalSearchDistance, double breakTime) {
        // Last two numbers here are horizontal and vertical distance they will look for the block
        super(gnome, speedModifier, searchDistance, verticalSearchDistance);
        this.gnome = gnome;
        this.tag = tag;
        this.reachDistance = reachDistance;
        this.tool = ItemStack.EMPTY;
        this.breakTime = breakTime;
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos blockPos) {
        BlockState state = level.getBlockState(blockPos);
        return state.is(tag);
    }

    @Override
    public boolean canUse() {
        return (gnome.getMainHandItem().isEmpty() && super.canUse());
    }


    @Override
    public double acceptedDistance() {
        return reachDistance;
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return reducedTickDelay(40 + mob.getRandom().nextInt(60));
    }

    @Override
    public void tick() { // TODO: Make the gnomes take a certain amount of time to break the block
        super.tick();
        if (isReachedTarget()) {
            if (!this.gnome.getLevel().getBlockState(this.blockPos).isAir()) {
                breakBlock(this.gnome);
            }
        }
    }


    // use gnome.setHandItems()
    public void breakBlock(GnomeEntity gnome) {
        ServerLevel level = gnome.getLevel() instanceof ServerLevel ? (ServerLevel) gnome.getLevel() : null;
        if (level != null) {
            // Get the drops from the block
            BlockState blockToBreak = level.getBlockState(this.blockPos);
            List<ItemStack> drops = blockToBreak.getBlock().getDrops(blockToBreak, level, this.blockPos, null, this.gnome, this.tool);

            // Then remove the block (I honestly don't know what true does here)
            level.removeBlock(this.blockPos, true);

            if (!drops.isEmpty()) {

                // Only pick up the item if the hand is empty
                if (gnome.getMainHandItem().isEmpty()) {
                    // First stack in the drops list goes into the gnomes hand
                    ItemStack pickup = drops.remove(0);

                    // Gnomes are very precise miners, so double the drops
                    pickup.setCount(pickup.getCount() * 2);
                    gnome.setItemInHand(InteractionHand.MAIN_HAND, pickup);
                }
                // Everything else drops normally from the block.
                drops.forEach((drop) -> {
                    Block.popResource(level, this.blockPos, drop);
                });
            }
        }
    }



}
