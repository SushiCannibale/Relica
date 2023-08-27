package fr.sushi.relica.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MachineInventory extends ItemStackHandler {
    public MachineInventory(int size) {
        super(size);
    }

    public void updateInventory() {

    }

    @Override
    public void onContentsChanged(int slot) {
        this.updateInventory();
    }

    public NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    public void clear() {
        this.stacks = NonNullList.withSize(this.stacks.size(), ItemStack.EMPTY);
        this.updateInventory();
    }
}