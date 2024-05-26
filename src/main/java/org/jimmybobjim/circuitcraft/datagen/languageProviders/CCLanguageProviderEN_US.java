package org.jimmybobjim.circuitcraft.datagen.languageProviders;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.facade.FacadeBlockItem;

public class CCLanguageProviderEN_US extends LanguageProvider {
    public CCLanguageProviderEN_US(PackOutput output, String locale) {
        super(output, CircuitCraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(CCBlocks.FACADE_BLOCK.get(), "Facade");
        add(CCBlocks.CABLE_BLOCK.get(), "Cable");
        add(FacadeBlockItem.FACADE_IS_MIMICING, "Facade is mimicking %s");
    }
}
