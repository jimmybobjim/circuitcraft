package org.jimmybobjim.circuitcraft.compat;

import net.minecraftforge.fml.ModList;

public class Compat {
    public static boolean isPresent(String modID) {
        return ModList.get().isLoaded(modID);
    }
}
