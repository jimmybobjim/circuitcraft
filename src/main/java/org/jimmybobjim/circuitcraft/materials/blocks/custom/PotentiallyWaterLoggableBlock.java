package org.jimmybobjim.circuitcraft.materials.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WaterLoggable;

import javax.annotation.ParametersAreNonnullByDefault;

import static org.jimmybobjim.circuitcraft.materials.blocks.CCBlockStates.WATER_LOGGABLE;

public interface PotentiallyWaterLoggableBlock extends SimpleWaterloggedBlock {
    @Override
    @ParametersAreNonnullByDefault
    default boolean canPlaceLiquid(BlockGetter pLevel, BlockPos pPos, BlockState pState, Fluid pFluid) {
        return pState.getValue(WATER_LOGGABLE) == WaterLoggable.NOT_WATERLOGGED && pFluid == Fluids.WATER;
    }

    @Override
    @ParametersAreNonnullByDefault
    default boolean placeLiquid(LevelAccessor pLevel, BlockPos pPos, BlockState pState, FluidState pFluidState) {
        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.NOT_WATERLOGGED && pFluidState.getType() == Fluids.WATER) {
            if (!pLevel.isClientSide()) {
                pLevel.setBlock(pPos, pState.setValue(WATER_LOGGABLE, WaterLoggable.WATERLOGGED), 3);
                pLevel.scheduleTick(pPos, pFluidState.getType(), pFluidState.getType().getTickDelay(pLevel));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    default @NotNull ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.WATERLOGGED) {
            pLevel.setBlock(pPos, pState.setValue(WATER_LOGGABLE, WaterLoggable.NOT_WATERLOGGED), 3);
            if (!pState.canSurvive(pLevel, pPos)) {
                pLevel.destroyBlock(pPos, true);
            }

            return new ItemStack(Items.WATER_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }
}
