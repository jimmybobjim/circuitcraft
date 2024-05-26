package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.client;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import org.jimmybobjim.circuitcraft.util.Util;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;

public record CableSpriteData(ResourceLocation side, ResourceLocation end, Color tint, @Nullable ResourceLocation end2, @Nullable Color tint2) {
    public static CableSpriteData coveredCableSprite(Color insulationColor, Color wireColor) {
        return new CableSpriteData(Util.rl("block/materials/metal/cable/side"), Util.rl("block/materials/metal/cable/side"), insulationColor, Util.rl("block/materials/metal/cable/side"), wireColor);
    }

    public static CableSpriteData uncoveredCableSprite(Color wireColor) {
        return new CableSpriteData(Util.rl(""), Util.rl(""), wireColor, null, null);
    }

    public String serialize() {
        return "{" +
                "side=" + side +
                ", end=" + end +
                ", tint=" + tint.getRGB() +
                ", end2=" + (end2==null?"null":end2) +
                ", tint2=" + (tint2 ==null?"null": tint2.getRGB()) +
                '}';
    }

    public static CableSpriteData deserialize(JsonElement jsonElement) {
        String jsonString = jsonElement.getAsString();

        if (jsonString.equals("null")) return null;

        String[] values = jsonString.substring(1, jsonString.length()-1).split("(side=)|(, end=)|(, tint=)|(, end2=)|(, tint2=)");

        if (values.length != 6) throw new IllegalArgumentException("not a valid String to deserialize" + jsonString + Arrays.toString(values));

        ResourceLocation side = new ResourceLocation(values[1]);
        ResourceLocation end = new ResourceLocation(values[2]);
        Color tint = new Color(Integer.parseInt(values[3]));

        @Nullable ResourceLocation side2 = values[4].equals("null")?null:new ResourceLocation(values[4]);
        @Nullable Color tint2 = values[5].equals("null")?null:new Color(Integer.parseInt(values[5]));

        return new CableSpriteData(side, end, tint, side2, tint2);
    }
}
