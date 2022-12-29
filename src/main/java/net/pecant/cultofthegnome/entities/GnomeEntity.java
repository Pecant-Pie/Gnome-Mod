package net.pecant.cultofthegnome.entities;


import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;

public class GnomeEntity extends PathfinderMob {

    public GnomeEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }


    @Override
    protected void registerGoals() {

        // Remember, the number is the priority of the goal, 0 being the highest.
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    public static <T extends Mob> boolean canSpawn(EntityType<T> tEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return true; // TODO: add a helper function that searches for a gnome statue within 5 blocks and returns true if one is found.
    }

    public static AttributeSupplier.Builder getGnomeAttributes() {
        return Mob.createMobAttributes().add(ForgeMod.SWIM_SPEED.get(), 5);
    }
}
