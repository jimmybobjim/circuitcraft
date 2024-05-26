package org.jimmybobjim.circuitcraft.api.chemistry;

import org.jimmybobjim.circuitcraft.util.Util;

@SuppressWarnings("unused")
public enum Isotopes implements CompoundHoldable {
    DEUTERIUM(Elements.HYDROGEN, 2),
    TRITIUM(Elements.HYDROGEN, 3),
    HELIUM_3(Elements.HELIUM, 3),
    URANIUM_235(Elements.URANIUM, 235),
    URANIUM_238(Elements.URANIUM, 238);


    public final Elements element;
    public final String symbol;
    public final int atomicNumber;
    public final int atomicMass;

    Isotopes(Elements element, int atomicMass) {
        this.element = element;
        this.symbol = Util.intToSuperScript(atomicMass) + element.symbol;
        this.atomicNumber = element.atomicNumber;
        this.atomicMass = atomicMass;
    }

    @Override
    public String getValue() {
        return symbol;
    }

    @Override
    public boolean requiresParentheses() {
        return false;
    }
}
