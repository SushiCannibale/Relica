package fr.sushi.relica.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

public class TestItemHandler implements IItemHandlerModifiable {

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {

    }

    @Override
    public int getSlots() {
        return 0;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return null;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }
}
