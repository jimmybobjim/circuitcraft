package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlock;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class FacadeBlock extends CableBlock implements EntityBlock {
    public FacadeBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FacadeBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
//        return Block.box(0, 0, 0, 16, 16, 16);

        if (pLevel.getBlockEntity(pPos) instanceof FacadeBlockEntity facade) {
            BlockState mimicBlock = facade.getMimicBlock();
            if (mimicBlock != null) {
                return mimicBlock.getShape(pLevel, pPos, pContext);
            }
        }
        return super.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof FacadeBlockEntity facadeBE) {
            @Nullable BlockState mimic = facadeBE.getMimicBlock();
            pPlayer.displayClientMessage(Component.literal(mimic==null?"null":mimic.toString()), false);
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public void playerDestroy(Level pLevel, Player pPlayer, BlockPos pPos, BlockState pState, @Nullable BlockEntity pBlockEntity, ItemStack pTool) {
        ItemStack item = new ItemStack(CCBlocks.FACADE_BLOCK.get());
        BlockState mimicBlock;
        if (pBlockEntity instanceof FacadeBlockEntity facadeBE) {
            mimicBlock = facadeBE.getMimicBlock();
        } else {
            mimicBlock = Blocks.COBBLESTONE.defaultBlockState();
        }
        FacadeBlockItem.setMimicBlock(item, mimicBlock);
        popResource(pLevel, pPos, item);
    }

    // When the player destroys the facade we need to restore the cable block
    @Override
    public boolean onDestroyedByPlayer(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, boolean pWillHarvest, FluidState pFluid) {
        BlockState defaultState = CCBlocks.CABLE_BLOCK.get().defaultBlockState();
        BlockState newState = CableBlock.calculateState(pLevel, pPos, defaultState);
        return ((LevelAccessor) pLevel).setBlock(pPos, newState, ((LevelAccessor) pLevel).isClientSide()
                ? Block.UPDATE_ALL_IMMEDIATE
                : Block.UPDATE_ALL);
    }
}
