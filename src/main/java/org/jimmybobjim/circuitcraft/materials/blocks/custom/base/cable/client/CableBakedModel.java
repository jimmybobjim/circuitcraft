package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jimmybobjim.circuitcraft.CircuitCraft;
import org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.cable.CableBlock;
import org.jimmybobjim.circuitcraft.util.QuadBuilder;

import java.util.*;

public class CableBakedModel implements IDynamicBakedModel {
    private final IGeometryBakingContext context;
    private final boolean facade;
    private final float width;
    private final float min;
    private final float max;
    private final @Nullable CableSpriteData sprites;

    private static final Map<Float, Map<Direction, AABB>> sideCubesCache = new HashMap<>();
    private Map<Direction, AABB> localSideCubesCache = new EnumMap<>(Direction.class);

    private boolean hasSecondaryEndTexture = false;
    private TextureAtlasSprite
            spriteSide,
            spriteEnd,
            spriteEnd2,
            spriteFacade,
            missingTexture;

    public CableBakedModel(IGeometryBakingContext context, boolean facade, float width, @Nullable CableSpriteData sprites) {
        this.context = context;
        this.facade = facade;
        this.width = width;
        this.sprites = sprites;
        this.min = 0.5f-(width/2);
        this.max = 0.5f+(width/2);

        initSideCubes();
    }

    private void initSideCubes() {
        if (!sideCubesCache.containsKey(width)) {
            for (Direction direction : Direction.VALUES) {
                Vec3i normal = direction.getNormal();
                localSideCubesCache.put(direction, new AABB(
                        sideCubesHelper(normal.getX(), min, max, 0),
                        sideCubesHelper(normal.getY(), min, max, 0),
                        sideCubesHelper(normal.getZ(), min, max, 0),
                        sideCubesHelper(normal.getX(), max, 1, min),
                        sideCubesHelper(normal.getY(), max, 1, min),
                        sideCubesHelper(normal.getZ(), max, 1, min)
                ));
            }
            sideCubesCache.put(width, localSideCubesCache);
        } else {
            localSideCubesCache = sideCubesCache.get(width);
        }
    }

    private static double sideCubesHelper(int normal, double a, double b, double c) {
        if (normal == 0) return a;
        else if (normal > 0) return b;
        else return c;
    }

    private void initTextures() {
        if (missingTexture == null) {
            missingTexture = getTexture("minecraft", "missingno");
            spriteFacade = getTexture(CircuitCraft.MODID, "block/materials/metal/cable/facade");
        }

        if (sprites == null) {
            spriteSide = spriteEnd = spriteEnd2 = missingTexture;
        } else if (spriteSide == null) {
            hasSecondaryEndTexture = sprites.end2()!=null;

            spriteSide = getTexture(sprites.side());
            spriteEnd = getTexture(sprites.end());
            spriteEnd2 = hasSecondaryEndTexture ? getTexture(sprites.end2()) : missingTexture;
        }
    }

    private boolean[] getDefaultConnections() {
        boolean[] connections = new boolean[6];

        for (Direction direction : Direction.VALUES) {
            connections[direction.ordinal()] = false;
        }

        return connections;
    }

    private boolean[] getConnections(BlockState state) {
        boolean[] connections = new boolean[6];

        for (Direction direction : Direction.VALUES) {
            connections[direction.ordinal()] = CableBlock.isConnected(state, direction);
        }

        return connections;
    }

    private static TextureAtlasSprite getTexture(ResourceLocation resourceLocation) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(resourceLocation);
    }

    private static TextureAtlasSprite getTexture(String modID, String path) {
        return getTexture(new ResourceLocation(modID, path));
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        initTextures();

        float width = this.width != -1f
                ? this.width
                : extraData.get(CableBlock.WIDTH) != null
                        ? extraData.get(CableBlock.WIDTH) : -1f;

        List<BakedQuad> quads = new ArrayList<>();

        boolean[] connections;

        if (state == null) {
            connections = getDefaultConnections();
        } else {
            connections = getConnections(state);
        }

        if (width != -1) {
            AABB coreCube = new AABB(min, min, min, max, max, max);

            if (side != null) {
                if (this.width == 1) {
                    quads.add(QuadBuilder.builder(side, spriteSide).cube(coreCube).cubeUV().bake());
                } else if (connections[side.ordinal()]) {
                    quads.add(QuadBuilder.builder(side, spriteEnd).cube(localSideCubesCache.get(side)).cubeUV().bake());
                    if (hasSecondaryEndTexture)
                        quads.add(QuadBuilder.builder(side, spriteEnd2).cube(localSideCubesCache.get(side)).cubeUV().bake());
                }
            }

            if (width < 1) {
                for (Direction face : Direction.VALUES) {
                    if (!connections[face.ordinal()]) quads.add(QuadBuilder.builder(face, spriteSide).cube(coreCube).cubeUV().bake());

                    for (Direction facing : Direction.VALUES) {
                        if (facing.getAxis() != face.getAxis() && connections[facing.ordinal()]) {
                            quads.add(QuadBuilder.builder(face, spriteSide).cube(localSideCubesCache.get(facing)).cubeUV().debugUV().bake());
                        }
                    }
                }
            }
        }

        if (facade && state == null) {
            for (Direction direction : Direction.VALUES) {
                quads.add(QuadBuilder.builder(direction, spriteFacade).bake());
            }

            return quads;
        }

        // Render the facade if we have one in addition to the cable above. Note that the facade comes from the model data property
        // (FACADEID)
        BlockState facadeId = extraData.get(CableBlock.FACADE_ID);
        if (facadeId != null) {
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(facadeId);
            ChunkRenderTypeSet renderTypes = model.getRenderTypes(facadeId, rand, extraData);
            if (renderType == null || renderTypes.contains(renderType)) { // always render in the null layer or the block-breaking textures don't show up
                try {
                    quads.addAll(model.getQuads(state, side, rand, ModelData.EMPTY, renderType));
                } catch (Exception ignored) {
                }
            }
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
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
        return spriteSide == null ? missingTexture : spriteSide;
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
}
