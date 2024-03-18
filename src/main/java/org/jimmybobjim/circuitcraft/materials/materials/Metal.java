package org.jimmybobjim.circuitcraft.materials.materials;

import java.awt.*;

public record Metal(Color color, String chemicalFormula, int maxVolts, int maxAmps, int resistance) {
    public static final Metal
            COPPER = new Metal(Color.ORANGE, "Cu", 1, 1, 1);
}
