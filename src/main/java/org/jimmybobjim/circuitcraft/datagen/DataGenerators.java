package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.datagen.languageProviders.CCLanguageProviderEN_US;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CircuitCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new CCRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), CCLootTableProvider.create(packOutput));
        generator.addProvider(event.includeServer(), new CCBlockTagGenerator(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeClient(), new CCBlockStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new CCItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new CCLanguageProviderEN_US(packOutput, "en_us"));
    }
}
