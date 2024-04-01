package org.jimmybobjim.circuitcraft.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jimmybobjim.circuitcraft.CircuitCraft;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static org.jimmybobjim.circuitcraft.util.Util.v3;

public class BakedModelHelper {
    private int num;
    private final List<Vec3> v1s = new ArrayList<>();
    private final List<Vec3> v2s = new ArrayList<>();
    private final List<Vec3> v3s = new ArrayList<>();
    private final List<Vec3> v4s = new ArrayList<>();
    private final List<Color> colors = new ArrayList<>();
    private final List<TextureAtlasSprite> sprites = new ArrayList<>();

    public record QuadData(
            int num,
            List<Vec3> v1s,
            List<Vec3> v2s,
            List<Vec3> v3s,
            List<Vec3> v4s,
            List<Color> colors,
            List<TextureAtlasSprite> sprites
    ) {}

    public void quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Color color, TextureAtlasSprite sprite) {
        v1s.add(v1);
        v2s.add(v2);
        v3s.add(v3);
        v4s.add(v4);
        colors.add(color);
        sprites.add(sprite);
        num++;
    }

    public void quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, Color color, TextureAtlasSprite sprite, int rotation) {
        switch (rotation) {
            case 1 -> quad(v2, v3, v4, v1, color, sprite);
            case 2 -> quad(v3, v4, v1, v2, color, sprite);
            case 3 -> quad(v4, v1, v2, v3, color, sprite);
            default -> quad(v1, v2, v3, v4, color, sprite);            //inc. case 0
        }
    }

    public void quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite, int rotation) {
        quad(v1, v2, v3, v4, Color.WHITE, sprite, rotation);
    }

    public void quad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        quad(v1, v2, v3, v4, Color.WHITE, sprite);
    }

    public void cube(Vec3 pos1, Vec3 pos2, HashMap<Direction, Color> colors, HashMap<Direction, TextureAtlasSprite> sprites) {
        double
                x1 = pos1.x, y1 = pos1.y, z1 = pos1.z,
                x2 = pos2.x, y2 = pos2.y, z2 = pos2.z;

        Vec3
                c1 = v3(x1, y1, z1),
                c2 = v3(x1, y1, z2),
                c3 = v3(x2, y1, z1),
                c4 = v3(x2, y1, z2),
                c5 = v3(x1, y2, z1),
                c6 = v3(x1, y2, z2),
                c7 = v3(x2, y2, z1),
                c8 = v3(x2, y2, z2);

        quad(c6, c8, c7, c5, colors.get(Direction.UP), sprites.get(Direction.UP));          // I can't remember how I figured these out, but it works
        quad(c1, c3, c4, c2, colors.get(Direction.DOWN), sprites.get(Direction.DOWN));
        quad(c5, c7, c3, c1, colors.get(Direction.NORTH), sprites.get(Direction.NORTH));
        quad(c2, c4, c8 ,c6, colors.get(Direction.SOUTH), sprites.get(Direction.SOUTH));
        quad(c3, c7, c8, c4, colors.get(Direction.EAST), sprites.get(Direction.EAST));
        quad(c2, c6, c5, c1, colors.get(Direction.WEST), sprites.get(Direction.WEST));
    }

    public void cube(Vec3 pos1, Vec3 pos2, HashMap<Direction, Color> colors, TextureAtlasSprite sprite) {
        cube(pos1, pos2, colors, cubeSprite(sprite, sprite, sprite, sprite, sprite, sprite));
    }

    public void cube(Vec3 pos1, Vec3 pos2, Color color, HashMap<Direction, TextureAtlasSprite> sprites) {
        cube(pos1, pos2, cubeColor(color, color, color, color, color, color), sprites);
    }

    public void cube(Vec3 pos1, Vec3 pos2, Color color, TextureAtlasSprite sprite) {
        cube(pos1, pos2, cubeColor(color, color, color, color, color, color), cubeSprite(sprite, sprite, sprite, sprite, sprite, sprite));
    }

    public void cube(Vec3 pos1, Vec3 pos2, TextureAtlasSprite sprite) {
        cube(pos1, pos2, Color.WHITE, sprite);
    }

    public void cube(Vec3 pos1, Vec3 pos2, HashMap<Direction, TextureAtlasSprite> sprites) {
        cube(pos1, pos2, Color.WHITE, sprites);
    }

    public void rotateAll(int x, int y, int z) {
        for (int i = 0; i < num; i++) {
            v1s.set(i, Util.rotate(v1s.get(i), x, y, z));
            v2s.set(i, Util.rotate(v2s.get(i), x, y, z));
            v3s.set(i, Util.rotate(v3s.get(i), x, y, z));
            v4s.set(i, Util.rotate(v4s.get(i), x, y, z));
        }
    }

    public QuadData getQuadData() {

        return new QuadData(num, v1s, v2s, v3s, v4s, colors, sprites);
    }

    public void add(BakedModelHelper helper) {
        QuadData quadData = helper.getQuadData();

        num+= quadData.num();

        for (int i = 0; i < quadData.num(); i++) {
            v1s.add(quadData.v1s().get(i));
            v2s.add(quadData.v2s().get(i));
            v3s.add(quadData.v3s().get(i));
            v4s.add(quadData.v4s().get(i));

            colors.add(quadData.colors().get(i));

            sprites.add(quadData.sprites().get(i));
        }
    }

    public List<BakedQuad> getBakedQuads() {
        List<BakedQuad> quads = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            Vec3 v1 = v1s.get(i);
            Vec3 v2 = v2s.get(i);
            Vec3 v3 = v3s.get(i);
            Vec3 v4 = v4s.get(i);
            Color color = colors.get(i);
            TextureAtlasSprite sprite = sprites.get(i);

            Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

            final BakedQuad[] quad = new BakedQuad[1];
            QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
            builder.setSprite(sprite);
            builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));

            putVertex(builder, normal, v1, 0, 0, color, sprite);
            putVertex(builder, normal, v2, 0, 16, color, sprite);
            putVertex(builder, normal, v3, 16, 16, color, sprite);
            putVertex(builder, normal, v4, 16, 0, color, sprite);

            quads.add(quad[0]);
        }

        return quads;
    }

    private static void putVertex(VertexConsumer builder, Position normal, Vec3 vec, float u, float v, Color color, TextureAtlasSprite sprite) {
        if (sprite == null) {
            CircuitCraft.LOGGER.error("sprite is null");
        } else {
            float iu = sprite.getU(u), iv = sprite.getV(v);

            builder.vertex(vec.x / 16, vec.y / 16, vec.z / 16)
                    .uv(iu, iv)
                    .uv2(0, 0)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .normal((float) normal.x(), (float) normal.y(), (float) normal.z())
                    .endVertex();
        }
    }

    public static BakedQuad basicQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4, TextureAtlasSprite sprite) {
        Vec3 normal = v3.subtract(v2).cross(v1.subtract(v2)).normalize();

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        putVertex(builder, normal, v1, 0, 0, Color.WHITE, sprite);
        putVertex(builder, normal, v2, 0, 16, Color.WHITE, sprite);
        putVertex(builder, normal, v3, 16, 16, Color.WHITE, sprite);
        putVertex(builder, normal, v4, 16, 0, Color.WHITE, sprite);
        return quad[0];
    }

    public static HashMap<Direction, Color> cubeColor(Color up, Color down, Color north, Color south, Color east, Color west) {
        HashMap<Direction, Color> colors = new HashMap<>();

        colors.put(Direction.UP, up);
        colors.put(Direction.DOWN, down);
        colors.put(Direction.NORTH, north);
        colors.put(Direction.SOUTH, south);
        colors.put(Direction.EAST, east);
        colors.put(Direction.WEST, west);

        return colors;
    }

    public static HashMap<Direction, TextureAtlasSprite> cubeSprite(TextureAtlasSprite up, TextureAtlasSprite down, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite east, TextureAtlasSprite west) {
        HashMap<Direction, TextureAtlasSprite> sprites = new HashMap<>();

        sprites.put(Direction.UP, up);
        sprites.put(Direction.DOWN, down);
        sprites.put(Direction.NORTH, north);
        sprites.put(Direction.SOUTH, south);
        sprites.put(Direction.EAST, east);
        sprites.put(Direction.WEST, west);

        return sprites;
    }

    private static final HashMap<String, TextureAtlasSprite> spritesMap = new HashMap<>();
    public static TextureAtlasSprite getTexture(String path) {
        if (spritesMap.get(path) != null) {
            return spritesMap.get(path);
        } else {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(
                    new ResourceLocation(CircuitCraft.MODID, path)
            );
            spritesMap.put(path, sprite);

            if (sprite == null) CircuitCraft.LOGGER.error("sprite is null at: " + path);

            return sprite;
        }
    }

    public static void refreshTextures() {
        spritesMap.forEach((path, sprite) -> {
            spritesMap.put(path, Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(
                    new ResourceLocation(CircuitCraft.MODID, path)
            ));
        });
    }
}
