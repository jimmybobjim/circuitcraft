package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;

import java.util.concurrent.CompletableFuture;

public class CCItemTagGenerator extends ItemTagsProvider {
    public CCItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                              CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, CircuitCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {

    }

    @Override
    public @NotNull String getName() {
        return super.getName();
    }
}
