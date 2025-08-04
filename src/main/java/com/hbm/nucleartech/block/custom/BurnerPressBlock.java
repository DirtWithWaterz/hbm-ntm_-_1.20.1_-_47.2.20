package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.block.RegisterBlocks;
import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import com.hbm.nucleartech.block.entity.RegisterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BurnerPressBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public BurnerPressBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BurnerPressEntity(pPos, pState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0, 0, 0, 16, 48, 16);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {

        if(!pLevel.getBlockState(pPos.above()).isAir() || !pLevel.getBlockState(pPos.above(2)).isAir()){

            super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
            pLevel.destroyBlock(pPos, true);
        }
        else {

            super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
            pLevel.setBlock(pPos.above(), RegisterBlocks.BURNER_PRESS_PART.get().defaultBlockState(), 3);
            pLevel.setBlock(pPos.above(2), RegisterBlocks.BURNER_PRESS_PART.get().defaultBlockState(), 3);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

//        add logic for burner press GUI here~ (watch a youtube vid about it or smth :3)
        if(!pLevel.isClientSide()) {

            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof BurnerPressEntity burnerPress)
                NetworkHooks.openScreen(((ServerPlayer) pPlayer), burnerPress, pPos);
            else
                throw new IllegalStateException("Burner Press Container provider is missing!");

            return InteractionResult.CONSUME;
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {

        if(pState.getBlock() != pNewState.getBlock()) {

            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof BurnerPressEntity burnerPress)
                burnerPress.drops();
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {

        ((BurnerPressEntity)pLevel.getBlockEntity(pPos)).tick(pLevel, pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {

        if(pLevel.isClientSide())
            return null;

        return createTickerHelper(pBlockEntityType, RegisterBlockEntities.BURNER_PRESS_ENTITY.get(),
                (pLevel1, pPos, pState1, pBlockEntity) ->
                    pBlockEntity.tick(pLevel1, pPos, pState1));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof BurnerPressEntity pressBe) {
            pressBe.lastInteractedPlayer = player;
            pressBe.lastInteractedPlayerHeldItem = player.getMainHandItem();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {

        pLevel.destroyBlock(pPos.above(), false);
        pLevel.destroyBlock(pPos.above(2), false);

        boolean drop = true;

        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof BurnerPressEntity pressBe && pressBe.lastInteractedPlayer != null) {
            drop = !pressBe.lastInteractedPlayer.isCreative() && (pressBe.lastInteractedPlayerHeldItem.is(ItemTags.PICKAXES) && TierSortingRegistry.isCorrectTierForDrops(((TieredItem) pressBe.lastInteractedPlayerHeldItem.getItem()).getTier(), pState));
        }

        pLevel.destroyBlock(pPos, drop);
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {

        pLevel.destroyBlock(pPos.above(), false);
        pLevel.destroyBlock(pPos.above(2), false);

        super.playerDestroy(pLevel, pPlayer, pPos, pState, pBlockEntity, pTool);

        pLevel.destroyBlock(pPos, !pPlayer.isCreative() && (pTool.is(ItemTags.PICKAXES) && TierSortingRegistry.isCorrectTierForDrops(((TieredItem) pTool.getItem()).getTier(), pState)));
    }
}
