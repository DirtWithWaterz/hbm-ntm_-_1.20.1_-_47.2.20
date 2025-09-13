package com.hbm.nucleartech.block.custom;

import com.hbm.nucleartech.capability.HbmCapabilities;
import com.hbm.nucleartech.interfaces.IEntityCapabilityBase.Type;
import com.hbm.nucleartech.network.HbmPacketHandler;
import com.hbm.nucleartech.network.packet.ClientboundSpawnDeconParticlePacket;
import com.hbm.nucleartech.util.ContaminationUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.network.PacketDistributor;

public class DeconRadBlock extends HorizontalDirectionalBlock {

    public DeconRadBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {

        if(!pLevel.isClientSide && pLevel instanceof ServerLevel serverLevel) {
            double x = pPos.getX()+0.25+(pLevel.random.nextFloat()*0.5); // x pos
            double y = pPos.getY()+1; // y pos
            double z = pPos.getZ()+0.25+(pLevel.random.nextFloat()*0.5); // z pos
            double dx = 0D; // x speed
            double dy = pLevel.random.nextDouble()*0.1; // y speed
            double dz = 0D; // z speed

            ClientboundSpawnDeconParticlePacket packet = new ClientboundSpawnDeconParticlePacket(
                    x, y, z, dx, dy, dz
            );

            HbmPacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> serverLevel.getChunkAt(pPos)), packet);
        }


        if(pEntity instanceof LivingEntity entity) {

//            HbmCapabilities.getData(entity).addValue(Type.RADENV, -0.5f);
            HbmCapabilities.getData(entity).addValue(Type.RADIATION, -0.5F);
//            HbmCapabilities.getData(entity).addValue(Type.NEUTRON, -0.5f);

            if(entity instanceof Player player) {

                ContaminationUtil.neutronActivateInventory(player, -0.5f, 1f);
                HbmCapabilities.getData(entity).syncLivingVariables(entity);
            }
//            System.err.println("Total " + entity.getName().getString() + String.valueOf(entity.getId()) + " Rads: " + HbmCapabilities.getData(entity).getValue(Type.RADIATION));
        }
        else if(pEntity instanceof ItemEntity item) {

            ContaminationUtil.neutronActivateItem(item.getItem(), -0.5f, 1f);
        }

        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
