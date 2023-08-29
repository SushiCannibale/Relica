package fr.sushi.relica.block.entity;

import fr.sushi.relica.inventory.AltarMenu;
import fr.sushi.relica.recipe.InfusionRecipe;
import fr.sushi.relica.registry.ModEntities;
import fr.sushi.relica.registry.ModItems;
import fr.sushi.relica.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AltarBlockEntity extends MachineBlockEntity implements MenuProvider {
    public static final int INV_SIZE = 10;
    public static final int SCROLL_SLOT = 4;
    public static final int FUEL_SLOT = 9;
    public static final int MAX_FUEL = 40;

    private final RecipeManager.CachedCheck<AltarBlockEntity, InfusionRecipe> recipeChecker;

    public AltarBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.ALTAR.get(), pPos, pBlockState, INV_SIZE);
        this.recipeChecker = RecipeManager.createCheck(InfusionRecipe.InfusionRecipeType.INSTANCE);
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
    void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
        ItemStack scroll = this.getItem(SCROLL_SLOT);
        boolean needsUpdate = false;

        if (!scroll.isEmpty() && this.getFuel() > 0) {
            InfusionRecipe recipe = this.recipeChecker.getRecipeFor(this, pLevel).orElse(null);
            /* Container is matching the recipe */
            if (recipe != null) {
                int process = this.processTime;
                int requiredMagic = recipe.getMagicNeeded();

                /* Recipe is finished */
                if (process == recipe.getProcessTime()) {
                    needsUpdate = true;
                    ItemStack result = recipe.assemble(this, pLevel.m_9598_());
                    this.fuel -= requiredMagic;
                    this.processTime = 0;

                    /* Debug only */
                    pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX(), pPos.getY() + 2D, pPos.getZ(), result));
//                    pBlockEntity.inventory.insertItem(SCROLL_SLOT, result, false);

                } else if (this.fuel >= requiredMagic) {
                    needsUpdate = true;
                    this.processTime++;
                }
            }
        }


        /* Adding fuel */
        ItemStack fuelStack = this.getItem(FUEL_SLOT);

        if (!fuelStack.isEmpty()) {
            Tuple<Integer, ItemStack> tuple = getFuelAmountAndResult(fuelStack);
            int fuelGiven = tuple.getA();
            ItemStack consumed = tuple.getB();

            if (this.fuel + fuelGiven <= MAX_FUEL) {
                needsUpdate = true;
                this.fuel += fuelGiven;
                // Not good :(
                fuelStack.shrink(1);
                this.setItem(FUEL_SLOT, consumed);
                pLevel.playLocalSound(pPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
            }
        }

        if (needsUpdate)
            this.setChanged();
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
