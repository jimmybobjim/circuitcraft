package org.jimmybobjim.circuitcraft.capabilities;

import net.minecraft.core.Direction;

public interface ICCEnergyStorage {
    boolean canExtract(Direction side);

    boolean canReceive(Direction side);

    int acceptEnergy();
}
