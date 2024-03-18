package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public interface WireHarnessConnectable {
    Direction[] getConnections(BlockState pState);
}
