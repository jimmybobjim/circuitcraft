package org.jimmybobjim.circuitcraft.api.chemistry;

import net.minecraft.network.chat.Component;

public interface CompoundHoldable {
    String getValue();
    boolean requiresParentheses();

    default Component getComponent() {
        return Component.literal(getValue());
    }
}
