package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jimmybobjim.circuitcraft.datagen.loot.CCBlockLootTables;

import java.util.List;
import java.util.Set;

public class CCLootTableProvider {
    public static LootTableProvider create(PackOutput packOutput) {
        return new LootTableProvider(packOutput, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(CCBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
