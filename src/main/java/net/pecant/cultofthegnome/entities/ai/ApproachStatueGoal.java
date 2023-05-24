package net.pecant.cultofthegnome.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.pecant.cultofthegnome.entities.GnomeEntity;

public class ApproachStatueGoal extends MoveToBlockGoal {

    private final GnomeEntity gnome;
    private final int maxDistance;
    private final double returnDistance;

    public ApproachStatueGoal(GnomeEntity gnome, double speedModifier, int maxDistance, double returnDistance) {
        super(gnome, speedModifier, 32, 32);
        this.gnome = gnome;
        this.maxDistance = maxDistance;
        this.returnDistance = returnDistance;
    }

    @Override
    protected boolean isValidTarget(LevelReader p_25619_, BlockPos p_25620_) {
        return true;
    }

    @Override
    public double acceptedDistance() {
        return returnDistance;
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return blockPos.above();
    }

    @Override
    public boolean canUse() {
        if (gnome.getRandom().nextInt(20) == 0) {
            int dist = gnome.blockPosition().distManhattan(gnome.getStatuePos()); // get distance from statue
            return gnome.getRandom().nextInt(dist * dist) > 100 || !gnome.getMainHandItem().isEmpty();
        }
        else return false;
//        return (gnome.getRandom().nextInt(50) != 0) && // Works 1 out of every 50 ticks
//                gnome.blockPosition().distManhattan(gnome.getStatuePos()) > maxDistance || // If the gnome is too far
//                !gnome.getMainHandItem().isEmpty(); // or if the gnome is holding an item
    }

    @Override
    public void start() {
        super.start();
        this.blockPos = gnome.getStatuePos();
    }

}
