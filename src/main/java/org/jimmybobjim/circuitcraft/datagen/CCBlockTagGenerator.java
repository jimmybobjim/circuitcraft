package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;

import java.util.concurrent.CompletableFuture;

public class CCBlockTagGenerator extends BlockTagsProvider {
    public CCBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CircuitCraft.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        //tags here
    }

    @Override
    public @NotNull String getName() {
        return super.getName();
    }
}
