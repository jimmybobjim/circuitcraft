package org.jimmybobjim.circuitcraft.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;

import java.util.Set;

public class CCBlockLootTables extends BlockLootSubProvider {
    public CCBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(CCBlocks.WIRE_HARNESS_BLOCK.get());
        this.dropSelf(CCBlocks.FACADE_BLOCK.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return CCBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
