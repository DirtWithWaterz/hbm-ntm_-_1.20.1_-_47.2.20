package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.block.entity.BurnerPressEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;

public class BurnerPressPartBlock extends Block {

    public BurnerPressPartBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean isAir(BlockState state) {
        return false; // Return true if you want it to be considered as air
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16); // Full block shape, or use Shapes.empty() for no collision
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.block(); // Or Shapes.block() if you want it to block players
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {

        BlockPos struct = getPressStructure(level, pos);

        BlockEntity be = level.getBlockEntity(struct);
        if (be instanceof BurnerPressEntity pressBe) {
            pressBe.lastInteractedPlayer = player;
            pressBe.lastInteractedPlayerHeldItem = player.getMainHandItem();
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {

        BlockPos struct = getPressStructure(pLevel, pPos);

        if(pLevel.getBlockState(struct).getBlock() instanceof BurnerPressBlock) {

            pLevel.getBlockState(struct).getBlock().destroy(pLevel, struct, pLevel.getBlockState(struct));
        }
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {

        BlockPos struct = getPressStructure(pLevel, pPos);

        if(pLevel.getBlockState(struct).getBlock() instanceof BurnerPressBlock) {

            BlockState block = pLevel.getBlockState(struct);

            block.getBlock().playerDestroy(pLevel, pPlayer, struct, block, pLevel.getBlockEntity(struct), pTool);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        BlockPos struct = getPressStructure(pLevel, pPos);

        if(pLevel.getBlockState(struct).getBlock() instanceof BurnerPressBlock)
            return pLevel.getBlockState(struct).getBlock().use(pLevel.getBlockState(struct), pLevel, struct, pPlayer, pHand, pHit);

        return InteractionResult.FAIL;
    }

    private BlockPos getPressStructure(LevelAccessor pLevel, BlockPos pPos) {

        BlockPos base = null;

        if(pLevel.getBlockState(pPos.below()).getBlock() instanceof BurnerPressBlock || pLevel.getBlockState(pPos.below()).isAir()){

            base = pPos.below();
        }
        else if(pLevel.getBlockState(pPos.below(2)).getBlock() instanceof BurnerPressBlock || pLevel.getBlockState(pPos.below(2)).isAir()) {

            base = pPos.below(2);;
        }
        return base;
    }
}
