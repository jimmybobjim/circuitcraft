package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlockStates;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WaterLoggable;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlock;

import javax.annotation.ParametersAreNonnullByDefault;

public class FacadeBlock extends WireHarnessBlock implements SimpleWaterloggedBlock {

    public FacadeBlock(Properties properties) {
        super(properties);
    }

    // water logging stuff below

//    private static boolean waterLoggable(BlockGetter pLevel, BlockPos pPos) {
//        if (pLevel.getBlockEntity(pPos) instanceof FacadeBlockEntity facadeBE) {
//            return facadeBE.waterLoggable();
//        }
//
//        return false;
//    }
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
//        pBuilder.add(X_ROT, Y_ROT, Z_ROT, ATTACHED_FACE, WATER_LOGGABLE);
//    }
//
//    @Override
//    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
//        BlockState parentState = CCBlocks.WIRE_HARNESS_BLOCK.get().getStateForPlacement(pContext);
//
//        if (parentState == null) return null;
//
//        if (waterLoggable(pContext.getLevel(), pContext.getClickedPos())) {
//            return parentState.setValue(BlockStateProperties.WATERLOGGED,
//                    pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
//        } else {
//            return parentState.setValue(BlockStateProperties.WATERLOGGED, false);
//        }
//    }
//
//    @Override
//    @ParametersAreNonnullByDefault
//    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
//        BlockState parentState = super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
//
//        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.NOT_WATERLOGGABLE) {
//            return parentState.setValue(WATER_LOGGABLE, WaterLoggable.NOT_WATERLOGGABLE);
//        } else {
//            return parentState;
//        }
//    }
//
//    @Override
//    public @NotNull FluidState getFluidState(@NotNull BlockState pState) {
//        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.WATERLOGGED) {
//            return Fluids.WATER.getSource(false);
//        } else {
//            return Fluids.EMPTY.defaultFluidState();
//        }
//    }
//
//    @Override
//    @ParametersAreNonnullByDefault
//    public boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
//        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.NOT_WATERLOGGABLE) {
//            return false;
//        } else {
//            return super.canPlaceLiquid(pLevel, pPos, pState, pFluid);
//        }
//    }

    //

    @Override
    @ParametersAreNonnullByDefault
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FacadeBlockEntity(pPos, pState);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pLevel.getBlockEntity(pPos) instanceof FacadeBlockEntity facadeBE) {
            BlockState mimicBlock = facadeBE.getMimicBlock();

            if (mimicBlock != null) {
                VoxelShape mimicShape = mimicBlock.getCollisionShape(pLevel, pPos, pContext);

                if (mimicShape == Block.box(0,0,0,16,16,16) && mimicBlock.isSolidRender(pLevel, pPos)) {
                    return mimicShape;
                } else {
                    return Shapes.join(super.getShape(pState, pLevel, pPos, pContext), mimicShape, BooleanOp.OR);
                }
            }
        }

        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        ItemStack facade = new ItemStack(CCBlocks.FACADE_BLOCK.get());

        BlockState mimicBlock;

        if (pBlockEntity instanceof FacadeBlockEntity facadeBE) {
            mimicBlock = facadeBE.getMimicBlock();
        } else {
            mimicBlock = Blocks.COBBLESTONE.defaultBlockState();
        }

        FacadeBlockItem.setMimicBlock(facade, mimicBlock);

        popResource(pLevel, pPos, facade);
    }

    private WaterLoggable waterLoggableOnDestroy(BlockState state) {
        return state.getValue(CCBlockStates.WATER_LOGGABLE) == WaterLoggable.WATERLOGGED
                ? WaterLoggable.WATERLOGGED
                : WaterLoggable.NOT_WATERLOGGED;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        BlockState newWireHarnessState = CCBlocks.WIRE_HARNESS_BLOCK.get().defaultBlockState()
                .setValue(X_ROT, state.getValue(X_ROT))
                .setValue(Y_ROT, state.getValue(Y_ROT))
                .setValue(Z_ROT, state.getValue(Z_ROT))
                .setValue(ATTACHED_FACE, state.getValue(ATTACHED_FACE))
                .setValue(CCBlockStates.WATER_LOGGABLE, waterLoggableOnDestroy(state));

        return level.setBlock(pos, newWireHarnessState, level.isClientSide() ? Block.UPDATE_ALL_IMMEDIATE : Block.UPDATE_ALL);
    }
}
