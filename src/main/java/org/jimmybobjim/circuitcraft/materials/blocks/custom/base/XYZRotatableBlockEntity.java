package org.jimmybobjim.circuitcraft.materials.blocks.custom.base;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

public abstract class XYZRotatableBlockEntity extends BaseEntityBlock {
    public static final DirectionProperty ATTACHED_FACE = DirectionProperty.create("attached_face");
    public static final BooleanProperty ROTATED = BooleanProperty.create("rotated");

    protected XYZRotatableBlockEntity(Properties pProperties) {
        super(pProperties);
    }

    public static Vec3i getRotationVect(BlockState pState) {
        return getRotationVect(pState.getValue(ATTACHED_FACE), pState.getValue(ROTATED));
    }

    public static Vec3i getRotationVect(Direction attachedFace, boolean rotated) {
        int xRot = 0, yRot = 0, zRot = 0;

        switch (attachedFace) {
            case UP -> {
                if (rotated) {
                    xRot = 2;
                    yRot = 1;
                    zRot = 0;
                } else {
                    xRot = 2;
                    yRot = 0;
                    zRot = 0;
                }
            }
            case DOWN -> {
                if (rotated) {
                    xRot = 0;
                    yRot = 1;
                    zRot = 0;
                } else {
                    xRot = 0;
                    yRot = 0;
                    zRot = 0;
                }
            }
            case NORTH -> {
                if (rotated) {
                    xRot = 3;
                    yRot = 0;
                    zRot = 1;
                } else {
                    xRot = 3;
                    yRot = 0;
                    zRot = 0;
                }
            }
            case SOUTH -> {
                if (rotated) {
                    xRot = 1;
                    yRot = 0;
                    zRot = 1;
                } else {
                    xRot = 1;
                    yRot = 0;
                    zRot = 0;
                }
            }
            case WEST -> {
                if (rotated) {
                    xRot = 0;
                    yRot = 0;
                    zRot = 1;
                } else { //
                    xRot = 0;
                    yRot = 3;
                    zRot = 1;
                }
            }
            case EAST -> {
                if (rotated) {
                    xRot = 0;
                    yRot = 0;
                    zRot = 3;
                } else { //
                    xRot = 0;
                    yRot = 3;
                    zRot = 3;
                }
            }
        }

        return new Vec3i(xRot, yRot, zRot);
    }

    @Nullable
    @Override
    @ParametersAreNonnullByDefault
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction attachedFace = pContext.getClickedFace().getOpposite();

        boolean rotated;
        if (Arrays.asList(Direction.NORTH, Direction.SOUTH).contains(pContext.getHorizontalDirection())) {
            rotated = Arrays.asList(Direction.EAST, Direction.WEST).contains(attachedFace);
        } else {
            rotated = !Arrays.asList(Direction.EAST, Direction.WEST).contains(attachedFace);
        }

        return this.defaultBlockState()
                .setValue(ATTACHED_FACE, attachedFace)
                .setValue(ROTATED, rotated);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ATTACHED_FACE, ROTATED);
    }
}
