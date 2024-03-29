package org.jimmybobjim.circuitcraft.datagen;

import com.google.gson.JsonObject;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.bakedModel.WireHarnessModelLoader;

public class CCBlockStateProvider extends BlockStateProvider {
    public CCBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CircuitCraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        registerWireHarness("wire_harness", false, CCBlocks.WIRE_HARNESS_BLOCK.get());
        registerWireHarness("facade", true, CCBlocks.FACADE_BLOCK.get());
//        registerWireHarness();
//        registerFacade();
        //blockWithItem(CCBlocks.EXAMPLE_BLOCK);

        //horizontalBlock(CCBlocks.WIRE_HARNESS_BLOCK.get(), new ModelFile.UncheckedModelFile(modLoc("block/wire_harness_block")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

//    private void registerWireHarness() {
//        BlockModelBuilder model = models().getBuilder("wire_harness")
//                .parent(models().getExistingFile(mcLoc("cube")))
//                .customLoader((builder, helper) -> new WireHarnessLoaderBuilder(WireHarnessModelLoader.GENERATOR_LOADER, builder, helper, false))
//                .end();
//
//        simpleBlock(CCBlocks.WIRE_HARNESS_BLOCK.get(), model);
//    }
//
//    private void registerFacade() {
//        BlockModelBuilder model = models().getBuilder("facade")
//                .parent(models().getExistingFile(mcLoc("cube")))
//                .customLoader((builder, helper) -> new WireHarnessLoaderBuilder(WireHarnessModelLoader.GENERATOR_LOADER, builder, helper, true))
//                .end();
//
//        simpleBlock(CCBlocks.FACADE_BLOCK.get(), model);
//    }

    private void registerWireHarness(String path, boolean facade, Block block) {
        BlockModelBuilder model = models().getBuilder(path)
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((builder, helper) -> new WireHarnessLoaderBuilder(WireHarnessModelLoader.GENERATOR_LOADER, builder, helper, facade))
                .end();

        simpleBlock(block, model);
    }

    public static class WireHarnessLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {
        private final boolean FACADE;
        protected WireHarnessLoaderBuilder(ResourceLocation loaderId, BlockModelBuilder parent, ExistingFileHelper existingFileHelper, boolean facade) {
            super(loaderId, parent, existingFileHelper);
            this.FACADE = facade;
        }

        @Override
        public JsonObject toJson(JsonObject json) {
            JsonObject obj = super.toJson(json);
            obj.addProperty("facade", FACADE);
            return obj;
        }
    }
}
