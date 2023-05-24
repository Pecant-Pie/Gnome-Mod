package net.pecant.cultofthegnome.entities;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import net.pecant.cultofthegnome.blockentities.StatueBlockEntity;
import net.pecant.cultofthegnome.entities.ai.ApproachStatueGoal;
import net.pecant.cultofthegnome.entities.ai.DepositItemGoal;
import net.pecant.cultofthegnome.entities.ai.MineBlockGoal;
import net.pecant.cultofthegnome.init.BlockInit;
import net.pecant.cultofthegnome.init.ItemInit;

public class GnomeEntity extends PathfinderMob {

    private static final Ingredient FOLLOW_ITEMS = Ingredient.of(Items.GOLD_INGOT, Items.IRON_INGOT, Items.DIAMOND, Items.COPPER_INGOT);
    private static final String statueTag = "STATUE_POS"; // CHANGING THIS WILL BREAK OLD GNOMES
    private StatueBlockEntity statue;
    private BlockPos statuePos;
    private Item hatItem = ItemInit.HAT.get();

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
        this.goalSelector.addGoal(3, new DepositItemGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new ApproachStatueGoal(this, 1.0D, 10, 5));
        this.goalSelector.addGoal(5, new MineBlockGoal(this, 1.0D, Tags.Blocks.ORES, 4, 10, 6, 1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder getGnomeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6f).add(Attributes.MOVEMENT_SPEED, 0.25D);
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
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putIntArray(statueTag, new int[]{statuePos.getX(), statuePos.getY(), statuePos.getZ()});
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        int[] tag = nbt.getIntArray(statueTag);

        if (tag != null && tag.length == 3) {

            BlockPos pos = new BlockPos(tag[0], tag[1], tag[2]);

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
    protected void dropCustomDeathLoot(DamageSource damageSource, int p_31465_, boolean p_31466_) {
        super.dropCustomDeathLoot(damageSource, p_31465_, p_31466_);

        ItemEntity itementity = this.spawnAtLocation(hatItem);
        if (itementity != null) {
            itementity.setExtendedLifetime();
        }

        if (this.statue != null)
            statue.removeGnome(this);
    }

    public BlockPos getStatuePos() {
        return statuePos;
    }

    // Attempts to put the gnome's held itemstack into a container at the given blockposition
    // Returns true if successful, false if there was no container there if it was full.
    public boolean depositHeldItem(BlockPos blockPos) {
        ServerLevel level = this.getLevel() instanceof ServerLevel ? (ServerLevel) this.getLevel() : null;
        if (level != null) {

            BlockState state = level.getBlockState(blockPos);
            if (state.hasBlockEntity()) {// get block entity and then check if its a container, then deposit item

                BlockEntity blockEntity = level.getBlockEntity(blockPos);
                if (blockEntity instanceof Container container) {
                    ItemStack stack = getMainHandItem();
                    return VanillaInventoryCodeHooks.getItemHandler(level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), Direction.UP)
                            .map(destinationResult -> {
                                IItemHandler itemHandler = destinationResult.getKey();
                                ItemStack depositedStack = stack.copy();
                                ItemStack remainder = ItemHandlerHelper.insertItemStacked(itemHandler, depositedStack, false);

//                                if (remainder.isEmpty())
//                                {
//                                    remainder = stack.copy();
//                                    remainder.shrink(stack.getCount());
//                                }
//                                else
//                                {
//                                    remainder = stack.copy();
//                                }

                                this.setItemInHand(InteractionHand.MAIN_HAND, remainder);
                                return false;
                            }).orElse(true);
                }
            }
        }
        return false;
    }
}
