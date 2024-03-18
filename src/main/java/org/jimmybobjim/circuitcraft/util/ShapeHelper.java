package org.jimmybobjim.circuitcraft.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public class ShapeHelper {
    private int num;
    private final List<Vec3> POS1 = new ArrayList<>();
    private final List<Vec3> POS2 = new ArrayList<>();

    public void cube(Vec3 pos1, Vec3 pos2) {
        POS1.add(pos1);
        POS2.add(pos2);
        num++;
    }

    public void rotateAll(int x, int y, int z) {
        for (int i = 0; i < num; i++) {
            POS1.set(i, Util.rotate(POS1.get(i), x*90, y*90, z*90));
            POS2.set(i, Util.rotate(POS2.get(i), x*90, y*90, z*90));
        }
    }

    public VoxelShape getShapes() {
        VoxelShape shape = null;
        VoxelShape box;
        double temp, x1, y1, z1, x2, y2, z2;

        for (int i = 0; i < num; i++) {
            x1 = POS1.get(i).x;
            y1 = POS1.get(i).y;
            z1 = POS1.get(i).z;

            x2 = POS2.get(i).x;
            y2 = POS2.get(i).y;
            z2 = POS2.get(i).z;

            if (x1 > x2) {
                temp = x1;
                x1 = x2;
                x2 = temp;
            }
            if (y1 > y2) {
                temp = y1;
                y1 = y2;
                y2 = temp;
            }
            if (z1 > z2) {
                temp = z1;
                z1 = z2;
                z2 = temp;
            }

            box = Block.box(x1, y1, z1, x2, y2, z2);

            if (shape == null) {
                shape = box;
            } else {
                shape = Shapes.join(shape, box, BooleanOp.OR);
            }
        }

        return shape;
    }
}
