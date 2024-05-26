package org.jimmybobjim.circuitcraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.joml.Quaternionf;

public class Util {
    // do not instantiate
    private Util() {}

    public static void setRange(int integer, int min, int max) throws IllegalArgumentException {
        if (integer < min) {
            throw new IllegalArgumentException(integer + "is below minimum (" + min + ")");
        }
        if (integer > max) {
            throw new IllegalArgumentException(integer + "is above maximum (" + max + ")");
        }
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(CircuitCraft.MODID, path);
    }

    public static String intToSubscript(int val) {
        return String.valueOf(val)
                .replace('0', '₀')
                .replace('1', '₁')
                .replace('2', '₂')
                .replace('3', '₃')
                .replace('4', '₄')
                .replace('5', '₅')
                .replace('6', '₆')
                .replace('7', '₇')
                .replace('8', '₈')
                .replace('9', '₉');
    }

    public static BakedModel getBlockModel(BlockState blockState) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
    }

    public static Vec3 rotate(Vec3 pos, float x, float y, float z) {
        x = (float) Math.toRadians(x);
        y = (float) Math.toRadians(y);
        z = (float) Math.toRadians(z);

        return pos.add(-8, -8, -8).xRot(x).yRot(y).zRot(z).add(8, 8, 8);
    }

    public static Vec3 inverseRotate(Vec3 pos, float x, float y, float z) {
        x = (float) -Math.toRadians(x);
        y = (float) -Math.toRadians(y);
        z = (float) -Math.toRadians(z);

        return pos.add(-8, -8, -8).zRot(z).yRot(y).xRot(x).add(8, 8, 8);
    }

    public static Direction inverseRotate(Direction direction, float x, float y, float z) {
        Quaternionf quaternionf = direction.getRotation().rotateXYZ(-x, -y, -z);

        return Direction.getNearest(quaternionf.x, quaternionf.y, quaternionf.z);
    }

    //returns true if a point lies within a rectangle
    public static boolean isPointInsideRectangle(Vec2 point, Vec2 pos1, Vec2 pos2) {
        return point.x > pos1.x && point.x < pos2.x && point.y > pos1.y && point.y < pos2.y;
    }

    //returns true if a point lies within a cube
    public static boolean isPointInsideCube(Vec3 point, Vec3 pos1, Vec3 pos2) {
        return point.x > pos1.x && point.x < pos2.x && point.y > pos1.y && point.y < pos2.y && point.z > pos1.z && point.z < pos2.z;
    }

    public static Vec3 globalToBlockCoords(Vec3 pos) {
        return v3(Math.abs(pos.x%1*16), Math.abs(pos.y%1*16), Math.abs(pos.z%1*16));
    }

    public static void sendMessage(String msg) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(msg));
    }

    public static Vec3 v3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    public static Vec2 v2(float x, float y) {
        return new Vec2(x, y);
    }

    public static Vec2 v2(double x, double y) {
        return v2((float) x, (float) y);
    }
}
