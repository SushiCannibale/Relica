package fr.sushi.relica.block.entity;

import fr.sushi.relica.inventory.AltarMenu;
import fr.sushi.relica.recipe.InfusionRecipe;
import fr.sushi.relica.registry.ModEntities;
import fr.sushi.relica.registry.ModItems;
import fr.sushi.relica.registry.ModMenus;
import fr.sushi.relica.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class AltarBlockEntity extends SynchedBlockEntity implements Container, MenuProvider {

    public static final int INV_SIZE = 10;
    public static final int SCROLL_SLOT = 4;
    public static final int FUEL_SLOT = 9;

    public final class AltarInventory extends ItemStackHandler {
        private AltarInventory(int size) {
            super(size);
        }

        @Override
        protected void onContentsChanged(int slot) {
            AltarBlockEntity.this.setChanged();
        }

        @Override
        protected void onLoad() {
            AltarBlockEntity.this.onLoad();
        }

        public NonNullList<ItemStack> getItems() {
            return this.stacks;
        }

        public void clear() {
            this.stacks = NonNullList.withSize(this.stacks.size(), ItemStack.EMPTY);
            AltarBlockEntity.this.setChanged();
        }
    }

    private final AltarInventory inventory = new AltarInventory(INV_SIZE);

    public int tickTime;

    private int processTime;
    /* The amount of time in ticks since the enchantment has started */
    private int fuel;
    public static final int MAX_FUEL = 40;

    private final RecipeManager.CachedCheck<AltarBlockEntity, InfusionRecipe> recipeManager;

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

    public AltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.ALTAR.get(), pPos, pBlockState);
        this.recipeManager = RecipeManager.createCheck(InfusionRecipe.InfusionRecipeType.INSTANCE);
    }

    /* MenuProvider */
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new AltarMenu(ModMenus.ALTAR_MENU.get(), pContainerId, pPlayerInventory, this, this.data);
    }

    /* Container */

    @Override
    public int getContainerSize() {
        return INV_SIZE;
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
    public ItemStack removeItem(int pSlot, int pAmount) {
        return this.inventory.extractItem(pSlot, pAmount, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return this.removeItem(pSlot, 1);
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        this.inventory.insertItem(pSlot, pStack, false);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.m_272074_(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
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
        this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        this.fuel = pTag.getInt("Fuel");
        this.processTime = pTag.getInt("ProcessTime");
        this.tickTime = pTag.getInt("TickTime");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", this.inventory.serializeNBT());
        pTag.putInt("Fuel", this.fuel);
        pTag.putInt("ProcessTime", this.processTime);
        pTag.putInt("TickTime", this.tickTime);
    }

    public AltarInventory getInventory() {
        return this.inventory;
    }
    public NonNullList<ItemStack> getItems() {
        return this.inventory.getItems();
    }

    public int getFuel() {
        return this.fuel;
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, AltarBlockEntity pBlockEntity) {
        ItemStack scroll = pBlockEntity.getItem(SCROLL_SLOT);

        if (!scroll.isEmpty() && pBlockEntity.getFuel() > 0) {
            InfusionRecipe recipe = pBlockEntity.recipeManager.getRecipeFor(pBlockEntity, pLevel).orElse(null);
            /* Container is matching the recipe */
            if (recipe != null) {
                System.out.println(pBlockEntity.processTime);
                int process = pBlockEntity.processTime;
                int requiredMagic = recipe.getMagicNeeded();

                /* Recipe is finished */
                if (process == recipe.getProcessTime()) {
                    ItemStack result = recipe.assemble(pBlockEntity, pLevel.m_9598_());
                    pBlockEntity.fuel -= requiredMagic;
                    pBlockEntity.processTime = 0;

                    /* Debug only */
                    pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX(), pPos.getY() + 2D, pPos.getZ(), result));
//                    pBlockEntity.inventory.insertItem(SCROLL_SLOT, result, false);

                } else {
                    if (pBlockEntity.fuel >= requiredMagic) {
                        pBlockEntity.processTime++;
                    }
                }
//                /* Recipe is about to start */
//                } else if(process == 0 && pBlockEntity.fuel >= requiredMagic) {
//
//                /* Recipe must continue */
//                } else {
//                    pBlockEntity.processTime++;
//                }

            }
        }


        /* Adding fuel */
        ItemStack fuelStack = pBlockEntity.getItem(FUEL_SLOT);

        if (!fuelStack.isEmpty()) {
            Tuple<Integer, ItemStack> tuple = getFuelAmountAndResult(fuelStack);
            int fuelGiven = tuple.getA();
            ItemStack consumed = tuple.getB();

            if (pBlockEntity.fuel + fuelGiven <= MAX_FUEL) {
                pBlockEntity.fuel += fuelGiven;
                // Not good :(
                fuelStack.shrink(1);
                pBlockEntity.setItem(FUEL_SLOT, consumed);
                pLevel.playLocalSound(pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }
        }
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
}
