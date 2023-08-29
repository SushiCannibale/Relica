package fr.sushi.relica.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class SynchedBlockEntity extends BlockEntity {

    public SynchedBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    /* Called when the client receive a chunk update */
    @Override
    public CompoundTag getUpdateTag() {
        // Used to specify which data is not necessarily needed on the client
        return this.saveWithFullMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        // Used to specify which data is not necessarily needed on the client
        super.handleUpdateTag(tag);
    }

    /* Called when the client receive a block update */
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        // TODO : Check for differences in client datas and if necessary, make an update
        this.load(pkt.getTag());
    }

    @Override
    public void setChanged() {
        if (this.getLevel() != null) {
            setChanged(this.getLevel(), this.getBlockPos(), this.getBlockState());
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }
}
