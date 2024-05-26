package org.jimmybobjim.circuitcraft.api.chemistry;

import org.jimmybobjim.circuitcraft.util.Util;

import java.util.Arrays;

public class Compound implements CompoundHoldable {
    private final CompoundComponent[] components;

    private Compound(CompoundComponent... components) {
        for (CompoundComponent component : components) {
            if (component.data() instanceof Mixture) throw new IllegalArgumentException("cannot add mixture to compound");
        }

        this.components = components;
    }

    public static Compound of(CompoundComponent... components) {
        return new Compound(components);
    }

    public CompoundComponent[] getComponents() {
        return Arrays.copyOf(components, components.length);
    }

    @Override
    public String getValue() {
        StringBuilder builder = new StringBuilder();

        for (CompoundComponent component : components) {
            if (component.parenthesesInCompounds()) builder.append('(');

            builder.append(component.data().getValue());

            if (component.quantity() > 1) builder.append(Util.intToSubscript(component.quantity()));

            if (component.parenthesesInCompounds()) builder.append(')');
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean requiresParentheses() {
        return true;
    }
}
