package org.jimmybobjim.circuitcraft.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.joml.Vector3f;

import java.awt.*;

public class QuadBuilder {
    private Vector3f from = new Vector3f(0, 0, 0);
    private Vector3f to = new Vector3f(16, 16, 16);
    private AABB cube = new AABB(0, 0, 0, 1, 1, 1);
    private final Direction face;
    private final TextureAtlasSprite sprite;
    private int tint = -1;
    private boolean cull = true;
    private boolean shade = true;
    private boolean AO = true;
    private boolean debugUV = false;
    private boolean debugSide = false;
    private UVPair uv0 = new UVPair(0, 0);
    private UVPair uv1 = new UVPair(16, 16);

    private QuadBuilder(Direction face, TextureAtlasSprite sprite) {
        this.face = face;
        this.sprite = sprite;
    }

    public static QuadBuilder builder(Direction face, TextureAtlasSprite sprite) {
        return new QuadBuilder(face, sprite);
    }

    public QuadBuilder cube(AABB cube) {
        this.cube = cube;
        from = new Vector3f((float) cube.minX * 16f, (float) cube.minY * 16f, (float) cube.minZ * 16f);
        to   = new Vector3f((float) cube.maxX * 16f, (float) cube.maxY * 16f, (float) cube.maxZ * 16f);
        return this;
    }

    public QuadBuilder uv0(float u0, float v0) {
        uv0 = new UVPair(u0, v0);
        return this;
    }

    public QuadBuilder uv1(float u1, float v1) {
        uv1 = new UVPair(u1, v1);
        return this;
    }

    public QuadBuilder cubeUV() {
        return switch (face) {
            case UP -> uv0(from.x(), from.z()).uv1(to.x(), to.z());
            case DOWN -> uv0(from.x(), to.z()).uv1(to.x(), from.z());
            case NORTH -> uv0(to.x(), to.y()).uv1(from.x(), from.y());
            case SOUTH -> uv0(from.x(), to.y()).uv1(to.x(), from.y());
            case WEST -> uv0(from.z(), to.y()).uv1(to.z(), from.y());
            case EAST -> uv0(to.z(), to.y()).uv1(from.z(), from.y());
        };
    }

    public QuadBuilder tint(int index) {
        tint = index;
        return this;
    }

    public QuadBuilder shade(boolean shade) {
        this.shade = shade;
        return this;
    }

    public QuadBuilder AO(boolean AO) {
        this.AO = AO;
        return this;
    }

    public QuadBuilder debugUV() {
        this.debugUV = true;
        return this;
    }

    public BakedQuad bake() {
        // furthest edges of cube
        float
                x1 = (float) cube.minX,
                x2 = (float) cube.maxX,
                y1 = (float) cube.minY,
                y2 = (float) cube.maxY,
                z1 = (float) cube.minZ,
                z2 = (float) cube.maxZ;

        // corners of cube (eus = east(+x) up(+y) south(+z), wdn = west(-x) down(-y) north(-z))
        Vector3f
                eus = new Vector3f(x2, y2, z2),
                eun = new Vector3f(x2, y2, z1),
                eds = new Vector3f(x2, y1, z2),
                edn = new Vector3f(x2, y1, z1),
                wus = new Vector3f(x1, y2, z2),
                wun = new Vector3f(x1, y2, z1),
                wds = new Vector3f(x1, y1, z2),
                wdn = new Vector3f(x1, y1, z1);

        // corners of square relative to facing (c1 is bottom left and the points go counter-clockwise)
        Vector3f
                c1 = new Vector3f(),
                c2 = new Vector3f(),
                c3 = new Vector3f(),
                c4 = new Vector3f();

        Color color = new Color(-1);

        switch (face) {
            case NORTH -> {
                color = Color.RED;
                c1 = edn;
                c2 = wdn;
                c3 = wun;
                c4 = eun;
            }
            case SOUTH -> {
                color = Color.BLACK;
                c1 = wds;
                c2 = eds;
                c3 = eus;
                c4 = wus;
            }
            case EAST -> {
                color = Color.GREEN;
                c1 = eds;
                c2 = edn;
                c3 = eun;
                c4 = eus;
            }
            case WEST -> {
                color = Color.BLUE;
                c1 = wdn;
                c2 = wds;
                c3 = wus;
                c4 = wun;
            }
            case UP -> {
                color = Color.YELLOW;
                c1 = wus;
                c2 = eus;
                c3 = eun;
                c4 = wun;
            }
            case DOWN -> {
                color = Color.CYAN;
                c1 = eds;
                c2 = wds;
                c3 = wdn;
                c4 = edn;
            }
        }

        if (debugUV) {
            CircuitCraft.LOGGER.debug(face + "{"
                    + "uv0" + uv0
                    + "uv1" + uv1
                    + "}"
            );
        }

        if (!debugSide) color = new Color(-1);

        Vector3f normal = new Vector3f(c3).sub(c2).cross(new Vector3f(c1).sub(c2)).normalize();

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);

        builder.setSprite(sprite);
        builder.setDirection(Direction.getNearest(normal.x, normal.y, normal.z));
        builder.setTintIndex(tint);
        builder.setShade(shade);
        builder.setHasAmbientOcclusion(AO);

        int
                u0 = (int) uv0.u(),
                v0 = (int) uv0.v(),
                u1 = (int) uv1.u(),
                v1 = (int) uv1.v();

        putVertex(builder, normal, c1.x, c1.y, c1.z, u0, v1, color);
        putVertex(builder, normal, c2.x, c2.y, c2.z, u1, v1, color);
        putVertex(builder, normal, c3.x, c3.y, c3.z, u1, v0, color);
        putVertex(builder, normal, c4.x, c4.y, c4.z, u0, v0, color);

        return quad[0];
    }

    private void putVertex(VertexConsumer builder, Vector3f normal, double x, double y, double z, int u0, int v0, Color color) {
        builder.vertex(x, y, z)
                .uv(sprite.getU(u0), sprite.getV(v0))
                .color(color.getRGB())
                .normal(normal.x(), normal.y(), normal.z())
                .endVertex();
    }
}
