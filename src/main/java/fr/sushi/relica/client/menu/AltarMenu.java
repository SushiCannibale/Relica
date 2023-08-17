package fr.sushi.relica.client.menu;

import fr.sushi.relica.entity.tileentity.AltarBlockEntity;
import fr.sushi.relica.registry.ModBlocks;
import fr.sushi.relica.registry.ModItems;
import fr.sushi.relica.registry.ModMenus;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AltarMenu extends AbstractContainerMenu {

    private final class ScrollSlot extends Slot {

        public ScrollSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            // TODO : Add checks for a complete scroll
            return pStack.is(ModItems.SCROLL.get());
        }
    }
    private final class CandleSlot extends Slot {
        public CandleSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            return pStack.is(ItemTags.CANDLES);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
    private final class FuelSlot extends Slot {

        public FuelSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack pStack) {
            /* Maybe add the possibility to provide enchanted gears in addition to books */
            return pStack.is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantments(pStack).size() > 0;
        }
    }

    private static final int START_INV_ID = AltarBlockEntity.INV_SIZE;
    private static final int END_HOTBAR_ID = START_INV_ID + 36;
    private final Container container;
    private ContainerData data;

    public AltarMenu(int pContainerId, Inventory pPlayerInventory) {
        this(ModMenus.ALTAR_MENU.get(), pContainerId, pPlayerInventory, new SimpleContainer(AltarBlockEntity.INV_SIZE), new SimpleContainerData(2));
    }

    public int getFuel() {
        return this.data.get(0);
    }

    public AltarMenu(@Nullable MenuType<?> pMenuType, int pContainerId, Inventory pPlayerInventory, Container pContainer, ContainerData data) {
        super(pMenuType, pContainerId);
        this.container = pContainer;
        this.data = data;

        ItemStack stack = this.container.getItem(0);

        this.addSlot(new CandleSlot(this.container, 0, 45, 18));
        this.addSlot(new Slot(this.container,1, 68, 14));
        this.addSlot(new CandleSlot(this.container, 2, 91, 18));
        this.addSlot(new Slot(this.container,3, 41, 41));
        this.addSlot(new ScrollSlot(this.container, 4, 68, 41));
        this.addSlot(new Slot(this.container,5, 95, 41));
        this.addSlot(new CandleSlot(this.container, 6, 45, 64));
        this.addSlot(new Slot(this.container, 7, 68, 68));
        this.addSlot(new CandleSlot(this.container, 8, 91, 64));
        this.addSlot(new FuelSlot(this.container, 9, 129, 68));

        this.addDataSlots(data);

        /* Inventory slots () */
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pPlayerInventory, j + i * 9 + 9, 13 + j * 18, 104 + i * 18));
            }
        }

        /* Hotbar slots */
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(pPlayerInventory, k, 13 + k * 18, 162));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack preStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            preStack = preStack.copy();

            /* From inventory to...  */
            if (pIndex > 9) {
                /* ...scroll slot */
                if (stack.is(ModItems.SCROLL.get())) {
                    if (!this.moveItemStackTo(stack, AltarBlockEntity.SCROLL_SLOT, AltarBlockEntity.SCROLL_SLOT+1, false)) {
                        return ItemStack.EMPTY;
                    }
                    /* ...candles slots */
                } else if (stack.is(ItemTags.CANDLES)) {
                    boolean f = true;
                    for (int i = 0; i <= 8; i += 2) {
                        f &= this.moveItemStackTo(stack, i, i+1, false);
                    }

                    if (!f)
                        return ItemStack.EMPTY;
                    /* ...fuel slot */
                } else if (stack.is(Items.ENCHANTED_BOOK) || stack.getAllEnchantments().size() > 0) {
                    if (!this.moveItemStackTo(stack, AltarBlockEntity.FUEL_SLOT, AltarBlockEntity.FUEL_SLOT+1, false)) {
                        return ItemStack.EMPTY;
                    }
                    /* ...resources slots */
                } else {
                    if (!this.moveItemStackTo(stack, 0, AltarBlockEntity.INV_SIZE, false)) {
                        return ItemStack.EMPTY;
                    }
                }

                /* From item slots to inventory */
            } else if (!this.moveItemStackTo(stack, START_INV_ID, END_HOTBAR_ID, true)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.m_269060_(ItemStack.EMPTY);
            }
            slot.setChanged();

            if (stack.getCount() == preStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, stack);
            this.broadcastChanges();

        }

        return preStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return this.container.stillValid(pPlayer);
    }
}
