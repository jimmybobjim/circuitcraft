package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class CCBlockBehaviours {

    public static final BlockBehaviour.Properties
            WIRE_HARNESS = BlockBehaviour.Properties.of()
                    .strength(1f, 2f)
                    .noOcclusion().sound(SoundType.STONE).mapColor(MapColor.TERRACOTTA_GRAY).isValidSpawn(Blocks::never)
                    .isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).pushReaction(PushReaction.DESTROY);
}
