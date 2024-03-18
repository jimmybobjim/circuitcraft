package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlockEntities;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.XYZRotatableBlockEntity;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.WireHarnessHoldable;
import org.jimmybobjim.circuitcraft.util.ShapeHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static org.jimmybobjim.circuitcraft.util.Util.v3;

public class WireHarnessBlock extends XYZRotatableBlockEntity {
    public static final ModelProperty<List<WireHarnessHoldable.WireData>> WIRE_DATA = new ModelProperty<>();
    public static final ModelProperty<BlockState> FACADE_BLOCK_ID = new ModelProperty<>();
    public static final ModelProperty<Boolean> IS_FACADE_BLOCK_SOLID = new ModelProperty<>();

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canSupportRigidBlock(pLevel, pPos.relative(pState.getValue(ATTACHED_FACE)));
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);

        if (pLevel.getBlockEntity(pPos) instanceof WireHarnessBlockEntity wireHarnessBE) {
            wireHarnessBE.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        }

        if (!pLevel.isClientSide() && shouldBeRemoved(pPos, pLevel, pState)) {
            dropResources(pState, pLevel, pPos);
            pLevel.removeBlock(pPos, pIsMoving);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if (pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof WireHarnessBlockEntity wireHarnessBE) {
            wireHarnessBE.markDirty();
        }
    }

    private boolean shouldBeRemoved(BlockPos pPos, Level pLevel, BlockState pState) {
        return !canSupportRigidBlock(pLevel, pPos.relative(pState.getValue(ATTACHED_FACE)));
    }

    public WireHarnessBlock(Properties properties) {
        super(properties);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        ShapeHelper shape = new ShapeHelper();
        shape.cube(v3(0,0,3.25), v3(16,0.5,12.75));    // brackets

        if (pLevel.getBlockEntity(pPos) instanceof WireHarnessBlockEntity wireHarnessBE) {
            for (int i=0; i<16; i++) {
                if (wireHarnessBE.getWIRE_DATA().get(i).width().getWidth() != 0) {
                    double width = wireHarnessBE.getWIRE_DATA().get(i).width().getWidth();

                    shape.cube(v3((width/4)+i, 0.5, 0), v3((width*0.75)+i, (width/2)+0.5, 16));
                }
            }
        }
        shape.rotateAll(pState.getValue(X_ROT), pState.getValue(Y_ROT), pState.getValue(Z_ROT));

        return shape.getShapes();
    }

    /**block entity stuff below
     */

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {

            if (pLevel.getBlockEntity(pPos) instanceof WireHarnessBlockEntity wireHarnessBlockEntity) {
                wireHarnessBlockEntity.drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    @ParametersAreNonnullByDefault
    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.getBlockEntity(pPos) instanceof WireHarnessBlockEntity wireHarnessBlockEntity){
            return wireHarnessBlockEntity.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WireHarnessBlockEntity(pPos, pState);
    }

    @Nullable
    @ParametersAreNonnullByDefault
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) return null;

        return createTickerHelper(pBlockEntityType, CCBlockEntities.WIRE_HARNESS_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}
