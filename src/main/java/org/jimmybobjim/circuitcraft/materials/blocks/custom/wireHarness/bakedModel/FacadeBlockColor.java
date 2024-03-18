package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.bakedModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.facadeBlock.FacadeBlockEntity;

public class FacadeBlockColor implements BlockColor {

    @Override
    public int getColor(@NotNull BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos, int pTintIndex) {
        if (pLevel != null) {
            if (pLevel.getBlockEntity(pPos) instanceof FacadeBlockEntity facadeBE) {
                BlockState mimic = facadeBE.getMimicBlock();

                if (mimic != null) {
                    return Minecraft.getInstance().getBlockColors().getColor(mimic, pLevel, pPos, pTintIndex);
                }
            }
        }

        return -1;
    }
}
