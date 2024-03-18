package org.jimmybobjim.circuitcraft.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jimmybobjim.circuitcraft.CircuitCraft;

public class CCTags {
    public static class Items {
        public static final TagKey<Item>
                WIRE = tag("wire");



        protected static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CircuitCraft.MODID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Blocks {
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(CircuitCraft.MODID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }
}
