package net.pecant.cultofthegnome.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.pecant.cultofthegnome.init.BlockEntityInit;
import net.pecant.cultofthegnome.screen.AltarMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AltarBlockEntity extends RandomizableContainerBlockEntity implements ICapabilityProvider {

    private NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);;

    public AltarBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.ALTAR.get(), pos, state);
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cultofthegnome.altar");
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.cultofthegnome.altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new AltarMenu(id, inventory, this);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int addItem(ItemStack item) {
        for(int i = 0; i < this.items.size(); ++i) {
            if (this.items.get(i).isEmpty()) {
                this.setItem(i, item);
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.items);
        }

    }

    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.items);
        }

    }

    public static <E extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, AltarBlockEntity entity) {
    }

    @Override
    public int getContainerSize() {
        return 9;
    }
}
