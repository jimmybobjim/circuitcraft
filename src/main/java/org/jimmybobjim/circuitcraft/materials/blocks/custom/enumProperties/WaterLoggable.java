package org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum WaterLoggable implements StringRepresentable  {
    NOT_WATERLOGGABLE,
    NOT_WATERLOGGED,
    WATERLOGGED;

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }
}
