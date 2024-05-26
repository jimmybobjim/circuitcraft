package org.jimmybobjim.circuitcraft.api.chemistry;

public record CompoundComponent(CompoundHoldable data, int quantity, boolean parenthesesInCompounds) {
    public CompoundComponent(CompoundHoldable data, int quantity, boolean parenthesesInCompounds) {
        this.data = data;
        this.quantity = quantity;
        this.parenthesesInCompounds = data.requiresParentheses() && parenthesesInCompounds;
    }

    public static CompoundComponent of(CompoundHoldable data, int quantity, boolean parentheses) {
        return new CompoundComponent(data, quantity, parentheses);
    }

    public static CompoundComponent of(CompoundHoldable data, int quantity) {
        return new CompoundComponent(data, quantity,  quantity > 1);
    }

    public static CompoundComponent of(CompoundHoldable data) {
        return new CompoundComponent(data, 1, false);
    }
}
