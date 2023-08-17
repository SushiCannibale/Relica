package fr.sushi.relica.entity.tileentity;

import fr.sushi.relica.client.menu.AltarMenu;
import fr.sushi.relica.recipe.InfusionRecipe;
import fr.sushi.relica.registry.ModEntities;
import fr.sushi.relica.registry.ModItems;
import fr.sushi.relica.registry.ModMenus;
import fr.sushi.relica.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Map;

public class AltarBlockEntity extends BaseContainerBlockEntity {

    public static final int INV_SIZE = 10;
    public static final int SCROLL_SLOT = 4;
    public static final int FUEL_SLOT = 9;
    /* 0, 2, 6, 8 for candles (even) */
    /* 1, 3, 5, 7 for resources (odd) */
    private final NonNullList<ItemStack> items = NonNullList.withSize(INV_SIZE, ItemStack.EMPTY);

    private int processTime;
    /* The amount of time in ticks since the enchantment has started */
    private int fuel;

    private static final int PROCESSING_TIME = 10_000; /* 10s */
    public static final int MAX_FUEL = 40;

    private RecipeManager.CachedCheck<RecipeWrapper, InfusionRecipe> recipeManager;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return switch(pIndex) {
                case 0 -> AltarBlockEntity.this.fuel;
                case 1 -> AltarBlockEntity.this.processTime;
                default -> -1;
            };
        }

        @Override
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0 -> AltarBlockEntity.this.fuel = pValue;
                case 1 -> AltarBlockEntity.this.processTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public enum ProcessState {
        NONE,
        STARTING,
        PROCEEDING,
        ENDING;

        public static ProcessState get(int processingTime) {
            int i = processingTime / PROCESSING_TIME;
            if (i == 0)
                return NONE;
            if (i < 0.33f)
                return STARTING;
            else if (i < 0.66f)
                return PROCEEDING;
            else
                return ENDING;
        }
    }

    public AltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.ALTAR.get(), pPos, pBlockState);
        this.recipeManager = RecipeManager.createCheck(ModRecipes.INFUSION_RECIPE_TYPE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.altar");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new AltarMenu(ModMenus.ALTAR_MENU.get(), pContainerId, pInventory, this, this.data);
    }

    @Override
    public int getContainerSize() {
        return INV_SIZE;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.items.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.items, pSlot, pAmount);
        this.setChanged();
        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(this.items, pSlot);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.items.set(pSlot, pStack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.m_272074_(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        if (pIndex == SCROLL_SLOT)
            return pStack.is(ModItems.SCROLL.get());

        else if (pIndex == FUEL_SLOT)
            return pStack.is(Items.ENCHANTED_BOOK) && EnchantmentHelper.getEnchantments(pStack).size() > 0;

        else if (pIndex % 2 == 0)
            return pStack.is(ItemTags.CANDLES);
        return true;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        ContainerHelper.loadAllItems(pTag, this.items);
        this.fuel = pTag.getInt("Fuel");
        this.processTime = pTag.getInt("ProcessTime");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.items);
        pTag.putInt("Fuel", this.fuel);
        pTag.putInt("ProcessTime", this.processTime);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AltarBlockEntity pBlockEntity) {
        ItemStack fuelStack = pBlockEntity.getItem(FUEL_SLOT);

        /* Adding fuel */
        if (!fuelStack.isEmpty()) {
            Tuple<Integer, ItemStack> tuple = getFuelAmountAndResult(fuelStack);
            int fuelGiven = tuple.getA();
            ItemStack consumed = tuple.getB();

            if (pBlockEntity.fuel + fuelGiven <= MAX_FUEL) {
                pBlockEntity.addFuel(fuelGiven);
                // Not good :(
                fuelStack.shrink(1);
                pBlockEntity.setItem(FUEL_SLOT, consumed);
                pLevel.playLocalSound(pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, 0.0f, false);
            }
        }

        ItemStack stack = pBlockEntity.getItem(0);

        if (pBlockEntity.fuel == 0)
            return;
    }

    public static Tuple<Integer, ItemStack> getFuelAmountAndResult(ItemStack pStack) {
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(pStack);
        ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
        int total = 0;

        for (Enchantment enchant : enchants.keySet()) {
            int level = enchants.get(enchant);
            if (enchant.isCurse()) {
                result.enchant(enchant, level);
            } else {
                total += level;
            }
        }

        return new Tuple<>(total, EnchantmentHelper.getEnchantments(result).size() > 0 ? result : new ItemStack(Items.BOOK));
    }

    public int getFuel() {
        return this.fuel;
    }

    public void addFuel(int value) {
        this.fuel += value;
    }

    public int getProcessTime() {
        return this.processTime;
    }

    public void setProcessTime(int value) {
        this.processTime = value;
    }

    public ProcessState getProcessState() {
        return ProcessState.get(this.processTime);
    }
}
