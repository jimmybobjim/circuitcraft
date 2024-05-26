package org.jimmybobjim.circuitcraft.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlock;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.client.CableModelLoader;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.client.CableSpriteData;

import javax.annotation.Nullable;
import java.awt.*;

public class CCBlockStateProvider extends BlockStateProvider {
    public CCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CircuitCraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerCable(CCBlocks.CABLE_BLOCK.get(), CableSpriteData.coveredCableSprite(new Color(-1), new Color(-1)));
        registerFacade();
    }

    private void registerCable(Block block, CableSpriteData spriteData) {
        BlockModelBuilder model = models().getBuilder("cable")
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper,
                        block instanceof CableBlock cableBlock ? cableBlock.getWidth() : -1f, spriteData))
                .end();
        simpleBlock(block, model);
    }

    private void registerFacade() {
        BlockModelBuilder model = models().getBuilder("facade")
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper))
                .end();
        simpleBlock(CCBlocks.FACADE_BLOCK.get(), model);
    }

    public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

        private final boolean facade;
        private final float width;
        @Nullable
        private final CableSpriteData sprites;

        //for cables
        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper, float width, CableSpriteData sprites) {
            super(loader, parent, existingFileHelper);
            this.facade = false;
            this.width = width;
            this.sprites = sprites;
        }

        //for the facade
        public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper) {
            super(loader, parent, existingFileHelper);
            this.facade = true;
            this.width = -1f;
            this.sprites = null;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
            obj.addProperty("facade", facade);
            obj.addProperty("width", width);
            obj.addProperty("sprites", sprites==null?"null":sprites.serialize());
            return obj;
        }
    }
}
