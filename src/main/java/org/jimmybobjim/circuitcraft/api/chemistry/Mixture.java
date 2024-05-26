package org.jimmybobjim.circuitcraft.api.chemistry;

import java.util.Arrays;

public class Mixture implements CompoundHoldable {
    private final CompoundComponent[] components;

    private Mixture(CompoundComponent... components) {
        this.components = components;
    }

    public static Mixture of(CompoundComponent... components) {
        return new Mixture(components);
    }

    public CompoundComponent[] getComponents() {
        return Arrays.copyOf(components, components.length);
    }

    @Override
    public String getValue() {
        StringBuilder builder = new StringBuilder();

        for (CompoundComponent component : components) {
            if (component.quantity() > 1) builder.append(component.quantity());

            builder.append('(').append(component.data().getValue()).append(')');
        }

        return builder.toString();
    }

    @Override
    public boolean requiresParentheses() {
        return true;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
