package net.pecant.cultofthegnome.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfig;
import net.pecant.cultofthegnome.blocks.StatueBlock;
import net.pecant.cultofthegnome.entities.GnomeEntity;
import net.pecant.cultofthegnome.init.BlockEntityInit;
import net.pecant.cultofthegnome.init.BlockInit;
import net.pecant.cultofthegnome.init.EntityInit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;


public class StatueBlockEntity extends BlockEntity {

    private static int MAX_CAPACITY = StatueBlock.MAX_GNOMES;
    private static String gnomeNBTPrefix = "gnome_";
    private static int MAX_DISTANCE = 32;
    private ArrayList<UUID> gnomes;

    public StatueBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.STATUE.get(), pos, state);
        gnomes = new ArrayList<UUID>();
    }

    public boolean isFull() {
        return gnomes.size() < MAX_CAPACITY;
    }

    public boolean isEmpty() {
        return gnomes.size() == 0;
    }

    // This should NOT be called directly except by the GnomeEntity.setStatue() method.
    // This does not update the blockstate GNOMES property
    public void addGnome(GnomeEntity gnome) {
        gnomes.add(gnome.getUUID());
    }

    public void removeGnome(GnomeEntity gnome) {
        gnomes.remove(gnome.getUUID());
        updateBlockState();
    }

    public int updateBlockState() {
        int numGnomes = gnomes.size();
        if (level != null && this.level.getBlockState(this.getBlockPos()).is(BlockInit.STATUE.get()))
            this.level.setBlock(this.getBlockPos(), this.getBlockState().setValue(StatueBlock.GNOMES, numGnomes), 3);
        return numGnomes;
    }


    // Saving and loading NBT so that the gnomes stay bound to the statue

    @Override
    public void load(CompoundTag nbt) {

        super.load(nbt);

        // each gnome will be a different entry in the
        for (int i = 0; i < getBlockState().getValue(StatueBlock.GNOMES); i++) {
            UUID id = nbt.getUUID(gnomeNBTPrefix + i);

            // Make sure an id was given before getting the gnome
            if (id != null)
                gnomes.add(id);
        }

        // This makes sure the gnomes bound to this statue are still bound when the statue is loaded in.
//        claimGnomes();

        // Any gnomes that failed to load will no longer count toward the statue's maximum.
        updateBlockState();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        // Save the UUID of each gnome into the nbt tag as "gnome_#", # is 0 to MAX_CAPACITY - 1
        for (int i = 0; i < getBlockState().getValue(StatueBlock.GNOMES); i++) {
            nbt.putUUID(gnomeNBTPrefix + i, gnomes.get(i));
        }
    }


    // Kills all gnomes and updates the blockstate accordingly.
    // This took me three hours and I hate it.
    public void cullGnomes() {
        BlockPos pos = getBlockPos();
        for (Entity entity: level.getEntities((Player)null,
                new AABB(pos.getX() - MAX_DISTANCE, pos.getY() - MAX_DISTANCE, pos.getZ() - MAX_DISTANCE,
                        pos.getX() + MAX_DISTANCE, pos.getY() + MAX_DISTANCE, pos.getZ() + MAX_DISTANCE),
                (entity) ->
                  (gnomes.contains(entity.getUUID()))
                )) {
            GnomeEntity gnome = (GnomeEntity) entity;
            gnome.kill();
            gnomes.remove(gnome.getUUID());
        }
        updateBlockState();
    }

    public void claimGnomes() {
        BlockPos pos = getBlockPos();
        for (Entity entity: level.getEntities((Player)null,
                new AABB(pos.getX() - MAX_DISTANCE, pos.getY() - MAX_DISTANCE, pos.getZ() - MAX_DISTANCE,
                        pos.getX() + MAX_DISTANCE, pos.getY() + MAX_DISTANCE, pos.getZ() + MAX_DISTANCE),
                (entity) ->
                        (gnomes.contains(entity.getUUID()))
        )) {
            GnomeEntity gnome = (GnomeEntity) entity;
            gnome.setStatue(this, pos);
        }
    }

}
