package org.jimmybobjim.circuitcraft.datagen.languageProviders;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;

public class CCLanguageProviderEN_US extends LanguageProvider {
    public CCLanguageProviderEN_US(PackOutput output, String locale) {
        super(output, CircuitCraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(CCBlocks.FACADE_BLOCK.get(), "Facade");
        add(CCBlocks.WIRE_HARNESS_BLOCK.get(), "Wire Harness");
        add(CCItems.WIRE_COPPER_RED_X1.get(), "1x Red Copper Wire");
    }
}
