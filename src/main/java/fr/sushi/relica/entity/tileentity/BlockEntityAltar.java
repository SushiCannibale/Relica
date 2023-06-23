package fr.sushi.relica.entity.tileentity;

import fr.sushi.relica.block.AltarBlock;
import fr.sushi.relica.registry.ModEntities;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class BlockEntityAltar extends BlockEntity implements Clearable, MenuProvider {
    public BlockEntityAltar(BlockPos pPos, BlockState pBlockState) {
        super(ModEntities.ALTAR.get(), pPos, pBlockState);
    }

    private final Container bookContainer = new Container() {
        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return BlockEntityAltar.this.book.isEmpty();
        }

        @Override
        public ItemStack getItem(int pSlot) {
            return pSlot == 0 ? BlockEntityAltar.this.book : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int pSlot, int pAmount) {
            if (pSlot == 0)
            {
                ItemStack stack = BlockEntityAltar.this.book.split(pAmount);
                if (BlockEntityAltar.this.book.isEmpty())
                    BlockEntityAltar.this.onBookItemRemove();

                return stack;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int pSlot) {
            if (pSlot == 0)
            {
                ItemStack stack = BlockEntityAltar.this.book;
                BlockEntityAltar.this.book = ItemStack.EMPTY;
                BlockEntityAltar.this.onBookItemRemove();

                return stack;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int pSlot, ItemStack pStack) { }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public void setChanged() {
            BlockEntityAltar.this.setChanged();
        }

        @Override
        public boolean stillValid(Player pPlayer) {
            return Container.m_272074_(BlockEntityAltar.this, pPlayer) && BlockEntityAltar.this.hasBook();
        }

        @Override
        public boolean canPlaceItem(int pIndex, ItemStack pStack) {
            return false;
        }

        @Override
        public void clearContent() { }
    };

    private final ContainerData containerData = new ContainerData() {
        public int get(int p_59600_) {
            return p_59600_ == 0 ? BlockEntityAltar.this.page : 0;
        }

        public void set(int p_59602_, int p_59603_) {
            if (p_59602_ == 0) {
                BlockEntityAltar.this.setPage(p_59603_);
            }

        }

        public int getCount() {
            return 1;
        }
    };

    private ItemStack book = ItemStack.EMPTY;
    int page;
    private int pageCount;

    private void onBookItemRemove() {
        this.page = 0;
        this.pageCount = 0;
        AltarBlock.updateBlock(null, this.getLevel(), this.getBlockPos(), this.getBlockState(), false);
    }

    public ItemStack getBook() {
        return this.book;
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    public void setPage(int n) {
        int i = Mth.clamp(n, 0, this.pageCount - 1);
        if (i != this.page) {
            this.page = i;
            this.setChanged();
        }
    }

    public boolean hasBook() {
        return this.book.is(Items.WRITABLE_BOOK) || this.book.is(Items.WRITTEN_BOOK);
    }

    @Override
    public void clearContent() {
        this.setBook(ItemStack.EMPTY);
    }

    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new LecternMenu(pContainerId, this.bookContainer, this.containerData);
    }

    public Component getDisplayName() {
        return Component.translatable("container.lectern");
    }

    private CommandSourceStack createCommandSourceStack(@Nullable Player pPlayer) {
        String s;
        Component component;
        if (pPlayer == null) {
            s = "Altar";
            component = Component.literal("Altar");
        } else {
            s = pPlayer.getName().getString();
            component = pPlayer.getDisplayName();
        }

        Vec3 vec3 = Vec3.atCenterOf(this.worldPosition);
        return new CommandSourceStack(CommandSource.NULL, vec3, Vec2.ZERO, (ServerLevel)this.level, 2, s, component, this.level.getServer(), pPlayer);
    }

    private ItemStack resolveBook(ItemStack pStack, @Nullable Player pPlayer) {
        if (this.level instanceof ServerLevel && pStack.is(Items.WRITTEN_BOOK)) {
            WrittenBookItem.resolveBookComponents(pStack, this.createCommandSourceStack(pPlayer), pPlayer);
        }

        return pStack;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Book", 10)) {
            this.book = this.resolveBook(ItemStack.of(pTag.getCompound("Book")), (Player)null);
        } else {
            this.book = ItemStack.EMPTY;
        }

        this.pageCount = WrittenBookItem.getPageCount(this.book);
        this.page = Mth.clamp(pTag.getInt("Page"), 0, this.pageCount - 1);
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (!this.getBook().isEmpty()) {
            pTag.put("Book", this.getBook().save(new CompoundTag()));
            pTag.putInt("Page", this.page);
        }

    }
}
