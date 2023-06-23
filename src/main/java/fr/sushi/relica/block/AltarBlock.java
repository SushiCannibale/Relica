package fr.sushi.relica.block;

import fr.sushi.relica.entity.tileentity.BlockEntityAltar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AltarBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty HAS_BOOK = BlockStateProperties.HAS_BOOK;
    public AltarBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HAS_BOOK, false));
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return LecternBlock.SHAPE_COLLISION;
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> LecternBlock.SHAPE_NORTH;
            case SOUTH -> LecternBlock.SHAPE_SOUTH;
            case EAST -> LecternBlock.SHAPE_EAST;
            case WEST -> LecternBlock.SHAPE_WEST;
            default -> LecternBlock.SHAPE_COMMON;
        };
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, HAS_BOOK);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityAltar(pPos, pState);
    }

    private void popBook(BlockState pState, Level pLevel, BlockPos pPos) {
        if (pLevel.getBlockEntity(pPos) instanceof BlockEntityAltar entityaltar) {
            Direction direction = pState.getValue(FACING);
            ItemStack itemstack = entityaltar.getBook().copy();
            float f = 0.25F * (float)direction.getStepX();
            float f1 = 0.25F * (float)direction.getStepZ();
            ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5D + (double)f, (double)(pPos.getY() + 1), (double)pPos.getZ() + 0.5D + (double)f1, itemstack);
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
            entityaltar.clearContent();
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pState.getValue(HAS_BOOK)) {
            if (!pLevel.isClientSide) {
                this.openScreen(pLevel, pPos, pPlayer);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        } else {
            ItemStack itemstack = pPlayer.getItemInHand(pHand);
            if (itemstack.getItem() instanceof WrittenBookItem) { // Merci mojang pour le hardcode >:[
                if (pLevel.getBlockEntity(pPos) instanceof BlockEntityAltar altarentity) {
                    altarentity.setBook(pPlayer.canUseGameMasterBlocks() ? itemstack : itemstack.split(1));
                    updateBlock(pPlayer, pLevel, pPos, altarentity.getBlockState(), true);
                    pLevel.playSound(null, pPos, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0f, 1.0f);
                }
            }
            return !itemstack.isEmpty() && !itemstack.is(ItemTags.LECTERN_BOOKS) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
    }

    public static void updateBlock(@javax.annotation.Nullable Entity entity, Level level, BlockPos pos, BlockState state, boolean bookValue) {
        BlockState blockstate = state.setValue(HAS_BOOK, bookValue);
        level.setBlock(pos, blockstate, Block.UPDATE_ALL);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockstate));
    }

    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pState.getValue(HAS_BOOK)) {
                this.popBook(pState, pLevel, pPos);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @javax.annotation.Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return !pState.getValue(HAS_BOOK) ? null : super.getMenuProvider(pState, pLevel, pPos);
    }

    private void openScreen(Level pLevel, BlockPos pPos, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof BlockEntityAltar) {
            pPlayer.openMenu((BlockEntityAltar)blockentity);
//            pPlayer.awardStat(Stats.INTERACT_WITH_LECTERN);
        }
    }
}
