package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.bakedModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.WireHarnessHoldable;
import org.jimmybobjim.circuitcraft.materials.items.CCItems;
import org.jimmybobjim.circuitcraft.util.BakedModelHelper;
import org.jimmybobjim.circuitcraft.util.Util;

import java.util.HashMap;
import java.util.List;

import static org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.wireHarnessBlock.WireHarnessBlock.*;
import static org.jimmybobjim.circuitcraft.util.BakedModelHelper.*;
import static org.jimmybobjim.circuitcraft.util.Util.v3;

public class WireHarnessBakedModel implements IDynamicBakedModel {
    private record WireHarnessData(
            int xRot,
            int yRot,
            int zRot,
            boolean isFacadeBlockSolid,
            @Nullable BlockState facadeBlock,
            List<WireHarnessHoldable.WireData> wireDatas
    ) {
        @Override
        public String toString() {
            StringBuilder wireDataString = new StringBuilder();
            wireDataString.append("[");
            wireDatas.forEach(wireData -> wireDataString.append(wireData).append(","));
            wireDataString.deleteCharAt(wireDataString.length()-1);
            wireDataString.append("]");

            return "WireHarnessData{" +
                    "xRot=" + xRot +
                    ", yRot=" + yRot +
                    ", zRot=" + zRot +
                    ", isFacadeBlockSolid=" + isFacadeBlockSolid +
                    ", facadeBlock=" + facadeBlock +
                    ", wireDatas=" + wireDataString +
                    '}';
        }
    }

    private final IGeometryBakingContext context;
    private final boolean FACADE;

    private TextureAtlasSprite
            facade,
            bottom,
            side_long,
            side_short,
            top;

    private void initTextures() {
        if (bottom == null) {
            facade = getTexture("block/wire_harness_block/facade");
            bottom = getTexture("block/wire_harness_block/base/bottom");
            side_long = getTexture("block/wire_harness_block/base/side_long");
            side_short = getTexture("block/wire_harness_block/base/side_short");
            top = getTexture("block/wire_harness_block/base/top");
        }
    }

    private WireHarnessData getData(@Nullable BlockState pState, @NotNull ModelData modelData) {
        if (pState == null) {
            return new WireHarnessData(0,0,0, true, null, List.of(
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(0),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(1),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(2),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(3),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(4),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(5),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(6),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(7),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(8),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(9),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(10),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(11),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(12),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(13),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(14),
                    ((WireHarnessHoldable) CCItems.WIRE_COPPER_RED_X1.get()).getWireData(15)
            ));
        } else {
            return new WireHarnessData(pState.getValue(X_ROT), pState.getValue(Y_ROT), pState.getValue(Z_ROT),
                    Boolean.TRUE.equals(modelData.get(IS_FACADE_BLOCK_SOLID)), modelData.get(FACADE_BLOCK_ID), modelData.get(WIRE_DATA));
        }
    }

    public WireHarnessBakedModel(IGeometryBakingContext context, boolean facade) {
        this.context = context;
        this.FACADE = facade;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState pState, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        initTextures();

        if (FACADE) {
            Util.sendMessage("facade");

            if (pState==null) {
                Util.sendMessage("state");

                if (side==null && (renderType==null || renderType.equals(RenderType.solid()))) {
                    Util.sendMessage("renderType");
                    return List.of(
                            basicQuad(v3(0, 16, 16), v3(16, 16, 16), v3(16, 16, 0), v3(0, 16, 0), facade),
                            basicQuad(v3(0, 0, 0), v3(16, 0, 0), v3(16, 0, 16), v3(0, 0, 16), facade),
                            basicQuad(v3(16, 0, 0), v3(16, 16, 0), v3(16, 16, 16), v3(16, 0, 16), facade),
                            basicQuad(v3(0, 0, 16), v3(0, 16, 16), v3(0, 16, 0), v3(0, 0, 0), facade),
                            basicQuad(v3(0, 16, 0), v3(16, 16, 0), v3(16, 0, 0), v3(0, 0, 0), facade),
                            basicQuad(v3(0, 0, 16), v3(16, 0, 16), v3(16, 16, 16), v3(0, 16, 16), facade)
                    );
                }
            }
        }

        WireHarnessData wireHarnessData = getData(pState, modelData);

        List<BakedQuad> facadeQuads = null;
        if (wireHarnessData.facadeBlock != null) {
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(wireHarnessData.facadeBlock);
            ChunkRenderTypeSet renderTypes = model.getRenderTypes(wireHarnessData.facadeBlock, rand, modelData);

            if (renderType == null || renderTypes.contains(renderType)) {
                if (wireHarnessData.isFacadeBlockSolid) {
                    try {
                        return model.getQuads(pState, side, rand, ModelData.EMPTY, renderType);
                    } catch (Exception e) {
                        CircuitCraft.LOGGER.error(e.toString());
                    }
                } else {
                    try {
                        facadeQuads = model.getQuads(pState, side, rand, ModelData.EMPTY, renderType);
                    } catch (Exception e) {
                        CircuitCraft.LOGGER.error(e.toString());
                    }
                }
            }
        }

        BakedModelHelper quads = new BakedModelHelper();

        HashMap<Direction, TextureAtlasSprite> baseSprites = cubeSprite(top, bottom, side_long, side_long, side_short, side_short);

        //north bracket
        quads.cube(v3(0,0,3.25), v3(16,0.5,4.75), baseSprites);

        //south bracket
        quads.cube(v3(0,0,11.25), v3(16,0.5,12.75), baseSprites);

        for (int i = 0; i < 16; i++) {
            WireHarnessHoldable.WireData wireData = wireHarnessData.wireDatas.get(i);

            double width = wireData.width().getWidth();

            if (width != 0) {
                quads.add(wireData.wireTexture());
                quads.cube(v3((width / 8) + i, 0.5, 3.625), v3((width * 0.875) + i, (width * 0.625) + 0.5, 4.375), top);
                quads.cube(v3((width / 8) + i, 0.5, 11.625), v3((width * 0.875) + i, (width * 0.625) + 0.5, 12.375), top);
            }
        }

        quads.rotateAll(wireHarnessData.xRot * 90, wireHarnessData.yRot * 90, wireHarnessData.zRot * 90);

        List<BakedQuad> finalQuads = quads.getBakedQuads();

        if (facadeQuads != null) {
            finalQuads.addAll(facadeQuads);
        }

        return finalQuads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return bottom == null
                ? Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "missingno"))
                : bottom;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemTransforms getTransforms() {
        return context.getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    @NotNull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.all();
    }
}
