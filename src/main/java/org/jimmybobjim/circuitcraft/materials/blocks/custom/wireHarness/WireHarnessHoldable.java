package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.enumProperties.WireWidth;
import org.jimmybobjim.circuitcraft.util.BakedModelHelper;

import static org.jimmybobjim.circuitcraft.util.Util.v3;

public interface WireHarnessHoldable {
    record WireData(WireWidth width, BakedModelHelper wireTexture) {
        public static final WireData EMPTY = new WireData(WireWidth.NULL, new BakedModelHelper());

        @Override
        public WireWidth width() {
            return width != null ? width : WireWidth.NULL;
        }

        @Override
        public String toString() {
            return width.toString();
        }
    }

    WireWidth getWidth();

    default BakedModelHelper getWireTexture(int pos) {
        TextureAtlasSprite top = BakedModelHelper.getTexture("block/wire_harness_block/base/top");

        int width = getWidth().getWidth();

        BakedModelHelper helper = new BakedModelHelper();

        helper.cube(
               v3(((double) width/4)+pos, 0.5, 0),
               v3(((double) width*0.75)+pos, ((double) width/2)+0.5, 16),
               top
        );

        return helper;
    }

    default WireHarnessHoldable.WireData getWireData(int pos) {
        return new WireData(getWidth(), getWireTexture(pos));
    }
}
