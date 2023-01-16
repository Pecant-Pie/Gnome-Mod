package net.pecant.cultofthegnome.entities;


import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.pecant.cultofthegnome.blockentities.StatueBlockEntity;
import net.pecant.cultofthegnome.init.BlockInit;
import net.pecant.cultofthegnome.init.ItemInit;

public class GnomeEntity extends PathfinderMob {

    private static final Ingredient FOLLOW_ITEMS = Ingredient.of(Items.GOLD_INGOT, Items.IRON_INGOT, Items.DIAMOND, Items.COPPER_INGOT);
    private static final String statueTag = "STATUE_POS"; // CHANGING THIS WILL BREAK OLD GNOMES
    private StatueBlockEntity statue;
    private BlockPos statuePos;

    public GnomeEntity(EntityType<? extends GnomeEntity> type, Level level) {
        super(type, level);
        this.statue = null;
    }


    @Override
    protected void registerGoals() {

        // Remember, the number is the priority of the goal, 0 being the highest.
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2D, FOLLOW_ITEMS, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder getGnomeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20f).add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public boolean setStatue(BlockEntity blockEntity, BlockPos pos) {
        if (blockEntity instanceof StatueBlockEntity statue) {
            this.statue = statue;
            this.statuePos = pos;
            statue.addGnome(this);
            return true;
        }
        else return false;
    }

    @Override
    public void onRemovedFromWorld() {
        if (this.statue != null)
            statue.removeGnome(this);
        super.onRemovedFromWorld();
    }

    // TODO: MAKE GNOMES REMEMBER THEIR STATUE ON UNLOAD/LOAD
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString(statueTag, statuePos.getX() + ", " + statuePos.getY() + ", " + statuePos.getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        String tag = nbt.getString(statueTag);

        if (tag != null && tag.matches("/-?[0-9]+, -?[0-9]+, -?[0-9]+/")) {

            String[] coords = tag.split(", ");
            BlockPos pos = new BlockPos(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(2));


            if (level.getBlockState(pos).is(BlockInit.STATUE.get()) && level.getBlockEntity(pos) instanceof StatueBlockEntity statue) {
                this.statuePos = pos;
                this.statue = statue;
            }
            else {
                this.kill();
            }
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource p_31464_, int p_31465_, boolean p_31466_) {
        super.dropCustomDeathLoot(p_31464_, p_31465_, p_31466_);

        ItemEntity itementity = this.spawnAtLocation(ItemInit.HAT.get());
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }
    }

}
