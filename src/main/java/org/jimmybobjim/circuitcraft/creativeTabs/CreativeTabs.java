package org.jimmybobjim.circuitcraft.creativeTabs;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;

public class CreativeTabs {
    public static void register(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            event.accept(CCItems.EXAMPLE_BLOCK_ITEM);
        } else if (event.getTabKey() == CreativeModeTabs.COLORED_BLOCKS) {
//            event.accept(CCItems.EXAMPLE_ITEM);
        }
    }
}
