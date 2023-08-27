package fr.sushi.relica.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class BlockEntityUtil {

//    public static void sendClientBlockEntityUpdateTag(BlockEntity pBlockEntity, CompoundTag tag) {
//        sendClientUpdatePacket(pBlockEntity.getLevel(), pBlockEntity.getBlockPos(), ClientboundBlockEntityDataPacket.create(pBlockEntity, e -> tag));
//    }
//
//    public static void sendClientBlockEntityUpdate(BlockEntity pBlockEntity) {
//        Packet<ClientGamePacketListener> packet = pBlockEntity.getUpdatePacket();
//        sendClientUpdatePacket(pBlockEntity.getLevel(), pBlockEntity.getBlockPos(), packet);
//    }
//
//    public static void sendClientUpdatePacket(Level pLevel, BlockPos pBlockPos, Packet<?> packet) {
//        if (pLevel instanceof ServerLevel serverLevel) {
//            List<ServerPlayer> players = serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pBlockPos), false);
//            players.forEach(player -> player.connection.send(packet));
//        }
//    }
}
