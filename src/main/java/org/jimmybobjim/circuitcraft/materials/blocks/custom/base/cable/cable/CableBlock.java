package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.PotentiallyWaterLoggableBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WaterLoggable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

import static org.jimmybobjim.circuitcraft.materials.blocks.CCBlockStates.WATER_LOGGABLE;

@ParametersAreNonnullByDefault
public class CableBlock extends Block implements EntityBlock, PotentiallyWaterLoggableBlock {
    private final float width;

    public static final BooleanProperty
            NORTH = BooleanProperty.create("north"),
            SOUTH = BooleanProperty.create("south"),
            EAST = BooleanProperty.create("east"),
            WEST = BooleanProperty.create("west"),
            UP = BooleanProperty.create("up"),
            DOWN = BooleanProperty.create("down");

    public static final ModelProperty<BlockState> FACADE_ID = new ModelProperty<>();
    public static final ModelProperty<Float> WIDTH = new ModelProperty<>();

    public static boolean isConnected(BlockState state, Direction side) {
        return state.getValue(switch (side) {
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            case UP -> UP;
            case DOWN -> DOWN;
        });
    }

    public CableBlock(Properties pProperties, int width) {
        super(pProperties);

        if (width == -1) {
            // this is a facade, width is handled by the block entity
            this.width = -1f;
        } else {
            // converting from pixels wide to a number between 0f (infinitely thin) and 1f (an entire block)
            this.width = width / 16f;
        }

        makeShapes();
        registerDefaultState(defaultBlockState().setValue(WATER_LOGGABLE, WaterLoggable.NOT_WATERLOGGED));
    }

    protected CableBlock(Properties pProperties) {
        super(pProperties);
        this.width = -1f;

        registerDefaultState(defaultBlockState().setValue(WATER_LOGGABLE, WaterLoggable.NOT_WATERLOGGED));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        return InteractionResult.PASS;
    }

    private static final HashMap<Float, VoxelShape[]> shapeCache = new HashMap<>();

    private VoxelShape[] localShapeCache;

    private static boolean getBit(int val, int pos) {
        return ((val>>pos)&1)==1;
    }

    private void makeShapes() {
        // this is a facade, width is handled by the block entity
        if (width == -1f) return;

        // the cache for this width has already been registered
        if (shapeCache.get(width) != null) {
            localShapeCache = shapeCache.get(width);
            return;
        }

        shapeCache.put(width, new VoxelShape[64]);

        float min = 0.5f-(width/2);
        float max = 0.5f+(width/2);

        VoxelShape
                centerShape = Shapes.box(min, min, min, max, max, max),
                northShape = Shapes.box(min, min, 0, max, max, min),
                southShape = Shapes.box(min, min, max, max, max, 1),
                eastShape = Shapes.box(max, min, min, 1, max, max),
                westShape = Shapes.box(0, min, min, min, max, max),
                upShape = Shapes.box(min, max, min, max, 1, max),
                downShape = Shapes.box(min, 0, min, max, min, max);

        // each bit represents the connector type in that direction (MSB) n,s,e,w,u,d (LSB)
        for (int i = 0; i < 64; i++) {
            boolean
                    north = getBit(i, 5),
                    south = getBit(i, 4),
                    east  = getBit(i, 3),
                    west  = getBit(i, 2),
                    up    = getBit(i, 1),
                    down  = getBit(i, 0);

            VoxelShape newShape = centerShape;

            if (north) newShape = Shapes.join(newShape, northShape, BooleanOp.OR);
            if (south) newShape = Shapes.join(newShape, southShape, BooleanOp.OR);
            if (east)  newShape = Shapes.join(newShape, eastShape, BooleanOp.OR);
            if (west)  newShape = Shapes.join(newShape, westShape, BooleanOp.OR);
            if (up)    newShape = Shapes.join(newShape, upShape, BooleanOp.OR);
            if (down)  newShape = Shapes.join(newShape, downShape, BooleanOp.OR);


            VoxelShape[] cache = shapeCache.get(width);
            cache[i] = newShape;
            shapeCache.put(width, cache);
        }

        localShapeCache = shapeCache.get(width);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (localShapeCache == null) return Shapes.empty();

        int
                north = getConnectorType(pLevel, pPos, Direction.NORTH)?1:0,
                south = getConnectorType(pLevel, pPos, Direction.SOUTH)?1:0,
                west = getConnectorType(pLevel, pPos, Direction.WEST)?1:0,
                east = getConnectorType(pLevel, pPos, Direction.EAST)?1:0,
                up = getConnectorType(pLevel, pPos, Direction.UP)?1:0,
                down = getConnectorType(pLevel, pPos, Direction.DOWN)?1:0;

        int index = (north<<5)+(south<<4)+(east<<3)+(west<<2)+(up<<1)+down;

        return localShapeCache[index];
    }

    public float getWidth() {
        return width;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pState.getValue(WATER_LOGGABLE) == WaterLoggable.WATERLOGGED) {
            pLevel.getFluidTicks().schedule(new ScheduledTick<>(Fluids.WATER, pCurrentPos, Fluids.WATER.getTickDelay(pLevel), 0L));
        }
        return calculateState(pLevel, pCurrentPos, pState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CableBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, be) -> {
                if (be instanceof CableBlockEntity cable) {
                    cable.tickServer();
                }
            };
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);

        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof CableBlockEntity cableBE) {
            cableBE.markDirty();
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);

        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof CableBlockEntity cableBE) {
            cableBE.markDirty();
        }

        BlockState blockState = calculateState(pLevel, pPos, pState);
        if (pState != blockState) {
            pLevel.setBlockAndUpdate(pPos, blockState);
        }
    }

    // Return the connector type for the given position and facing direction (false=none, true=cable)
    private static boolean getConnectorType(BlockGetter level, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof CableBlock || isConnectable(level, connectorPos, facing);
    }

    // Return true if the block at the given position is connectable to a cable. This is the
    // case if the block supports forge energy
    public static boolean isConnectable(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (te == null) {
            return false;
        }
        return te.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);

        pBuilder.add(WATER_LOGGABLE, NORTH, SOUTH, EAST, WEST, UP, DOWN);
    }

    @Nonnull
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();

        return calculateState(level, pos, defaultBlockState()).setValue(WATER_LOGGABLE,
                level.getFluidState(pos).getType() == Fluids.WATER ? WaterLoggable.WATERLOGGED : WaterLoggable.NOT_WATERLOGGED);
    }

    @Nonnull
    public static BlockState calculateState(LevelAccessor level, BlockPos pos, BlockState state) {
        boolean
                north = getConnectorType(level, pos, Direction.NORTH),
                south = getConnectorType(level, pos, Direction.SOUTH),
                west = getConnectorType(level, pos, Direction.WEST),
                east = getConnectorType(level, pos, Direction.EAST),
                up = getConnectorType(level, pos, Direction.UP),
                down = getConnectorType(level, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull FluidState getFluidState(BlockState pState) {
        return switch (pState.getValue(WATER_LOGGABLE)) {
            case NOT_WATERLOGGABLE -> Fluids.EMPTY.defaultFluidState();
            case NOT_WATERLOGGED -> super.getFluidState(pState);
            case WATERLOGGED -> Fluids.WATER.getSource(false);
        };
    }
}
