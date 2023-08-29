package fr.sushi.relica.block.entity;

import fr.sushi.relica.inventory.MachineInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/* Machines are blocks that contains 2 data slots (fuel & processTime) and matches recipes */
public abstract class MachineBlockEntity extends SynchedBlockEntity implements Container {

    protected final MachineInventory inventory;
    public final ContainerData data;
    protected int fuel;
    protected int processTime;

    public MachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int containerSize) {
        super(pType, pPos, pBlockState);
        this.inventory = new MachineInventory(containerSize) {
            @Override
            public void updateInventory() {
                MachineBlockEntity.this.setChanged();
            }
        };
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                if (pIndex == 0)
                    return MachineBlockEntity.this.fuel;
                else if (pIndex == 1)
                    return MachineBlockEntity.this.processTime;
                else
                    return -1;
            }

            @Override
            public void set(int pIndex, int pValue) {
                if (pIndex == 0)
                    MachineBlockEntity.this.fuel = pValue;
                else if (pIndex == 1)
                    MachineBlockEntity.this.processTime = pValue;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    /* Data */
    public int getFuel() {
        return this.data.get(0);
    }

    public int getProcessTime() {
        return this.data.get(1);
    }

    /* Container */

    public NonNullList<ItemStack> getItems() {
        return this.inventory.getItems();
    }

    @Override
    public int getContainerSize() {
        return this.inventory.getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.inventory.getStackInSlot(pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.inventory.setStackInSlot(pSlot, pStack);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return this.inventory.extractItem(pSlot, pAmount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack itemstack = this.inventory.getStackInSlot(pSlot);
        this.inventory.setStackInSlot(pSlot, ItemStack.EMPTY);
        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.m_272074_(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    /* Data saving */

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", this.inventory.serializeNBT());
        pTag.putInt("ProcessTime", this.processTime);
        pTag.putInt("Fuel", this.fuel);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        this.processTime = pTag.getInt("ProcessTime");
        this.fuel = pTag.getInt("Fuel");
    }

    abstract void serverTick(Level pLevel, BlockPos pPos, BlockState pState);

    public static void performServerTick(Level pLevel, BlockPos pPos, BlockState pState, MachineBlockEntity pBlockEntity) {
        pBlockEntity.serverTick(pLevel, pPos, pState);

        /*
            Add checks for :
            - adding fuel
            - recipe
         */
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.data.set(0, this.fuel);
        this.data.set(1, this.processTime);
    }
}
