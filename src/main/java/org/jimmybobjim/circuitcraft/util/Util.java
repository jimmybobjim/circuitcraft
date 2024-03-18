package org.jimmybobjim.circuitcraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelDataManager;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Util {
    public static void setRange(int integer, int min, int max) throws IllegalArgumentException {
        if (integer < min) {
            throw new IllegalArgumentException(integer + "is below minimum (" + min + ")");
        }
        if (integer > max) {
            throw new IllegalArgumentException(integer + "is above maximum (" + max + ")");
        }
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

    @SuppressWarnings("UnstableApiUsage")
    public static void dataManagerToModelData(@Nullable ModelDataManager dataManager, BlockPos pPos, Consumer<ModelData> consumer) {
        ModelData modelData;

        if (dataManager != null) {
            modelData = dataManager.getAt(pPos);

            if (modelData != null) {
                consumer.accept(modelData);
            }
        }
    }

    public static boolean implementsInterface(Class<?> classToCheck, Class<?> interfaceToCheck) {
        return Arrays.asList(classToCheck.getInterfaces()).contains(interfaceToCheck);
    }

    public static <T> void getModelData(ModelData modelData, ModelProperty<T> modelProperty, Consumer<T> consumer) {
        T property = modelData.get(modelProperty);
        if (property != null) {
            consumer.accept(property);
        }
    }

    public static <T> List<List<T>> init2dArray(int columns, int rows) {
        List<List<T>> list = new ArrayList<>(columns);

        for(int i = 0; i < columns; i++) {
            list.add(new ArrayList<>(rows));
        }

        return list;
    }

    public static void sendMessage(String msg) {
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(msg));
    }

    public static Vec3 v3(double x, double y, double z) {
        return new Vec3(x, y, z);
    }

    public static Vec3 v3 (List<Integer> vec) {
        return v3(vec.get(0), vec.get(1), vec.get(2));
    }

    public static Vec2 v2(float x, float y) {
        return new Vec2(x, y);
    }

    public static Vec2 v2(double x, double y) {
        return v2((float) x, (float) y);
    }
}
