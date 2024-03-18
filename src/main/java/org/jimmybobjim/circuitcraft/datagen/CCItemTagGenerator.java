package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;
import org.jimmybobjim.circuitcraft.tags.CCTags;

import java.util.concurrent.CompletableFuture;

public class CCItemTagGenerator extends ItemTagsProvider {
    public CCItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                              CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, CircuitCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(CCTags.Items.WIRE)
                .add(CCItems.WIRE_COPPER_RED_X1.get());
    }

    @Override
    public @NotNull String getName() {
        return super.getName();
    }
}
