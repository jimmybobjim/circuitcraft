package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CCBlockBehaviours {


    public static final BlockBehaviour.Properties
            WIRE_HARNESS = BlockBehaviour.Properties.of()
                    .strength(1f, 2f)
                    .noOcclusion().sound(SoundType.STONE).mapColor(MapColor.TERRACOTTA_GRAY).isValidSpawn(Properties::never)
                    .isRedstoneConductor(Properties::never).isSuffocating(Properties::never).pushReaction(PushReaction.DESTROY);

    /**functions copied from {@link net.minecraft.world.level.block.Blocks} as needed
     */
    private static class Properties {
        private static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
            return (boolean)false;
        }

        private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
            return false;
        }
    }
}
