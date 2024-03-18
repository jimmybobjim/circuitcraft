package org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum WireWidth implements StringRepresentable {
    NULL(0),
    x1(1),
    x2(2),
    x4(4),
    x8(8),
    x16(16);

    private final int width;

    WireWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}
