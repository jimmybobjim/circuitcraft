package org.jimmybobjim.circuitcraft.materials.blocks;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WaterLoggable;

public class CCBlockStates {
    public static final EnumProperty<WaterLoggable> WATER_LOGGABLE = EnumProperty.create("waterloggable", WaterLoggable.class);
}
