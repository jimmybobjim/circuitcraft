package org.jimmybobjim.circuitcraft.materials.fluids;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidHelper {
    private static final ResourceLocation
            WATER_STILL = new ResourceLocation("block/water_still"),
            WATER_FLOWING = new ResourceLocation("block/water_flow"),
            WATER_OVERLAY = new ResourceLocation("block/water_overlay");

    private final String name;
    private final Color tint;
    private final Color fog;

    private ForgeFlowingFluid.Properties forgeProperties;
    private RegistryObject<FluidType> fluidType;
    private RegistryObject<ForgeFlowingFluid.Source> source;
    private RegistryObject<ForgeFlowingFluid.Flowing> flowing;
    private Supplier<BucketItem> bucket;
    private Supplier<LiquidBlock> block;
    private int slopeFindDistance = 4;
    private int levelDecreasePerBlock = 1;
    private float explosionResistance = 1;
    private int tickRate = 5;
    private boolean hasBucket = true;
    private FluidType.Properties fluidProperties = FluidType.Properties.create();
    private BlockBehaviour.Properties blockProperties = BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable();
    private Item.Properties itemProperties = new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1);

    private FluidHelper(String name, Color tint, Color fog) {
        this.name = name;
        this.tint = tint;
        this.fog = fog;
    }

    public static FluidHelper create(String name, Color tint, Color fog) {
        return new FluidHelper(name, tint, fog);
    }

    public FluidHelper slopeFindDistance(int slopeFindDistance) {
        this.slopeFindDistance = slopeFindDistance;

        return this;
    }

    public FluidHelper levelDecreasePerBlock(int levelDecreasePerBlock) {
        this.levelDecreasePerBlock = levelDecreasePerBlock;

        return this;
    }

    public FluidHelper explosionResistance(float explosionResistance) {
        this.explosionResistance = explosionResistance;

        return this;
    }

    public FluidHelper tickRate(int tickRate) {
        this.tickRate = tickRate;

        return this;
    }

    public FluidHelper fluidProperties(FluidType.Properties fluidProperties) {
        this.fluidProperties = fluidProperties;

        return this;
    }

    public FluidHelper blockProperties(BlockBehaviour.Properties blockProperties) {
        this.blockProperties = blockProperties;

        return this;
    }

    public FluidHelper itemProperties(Item.Properties itemProperties) {
        this.itemProperties = itemProperties;

        return this;
    }

    public FluidHelper noBucket() {
        hasBucket = false;

        return this;
    }

    private FluidType registerFluidType() {
        return new FluidType(fluidProperties) {
            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {
                    @Override
                    public int getTintColor() {
                        return tint.getRGB();
                    }

                    @Override
                    public ResourceLocation getStillTexture() {
                        return WATER_STILL;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return WATER_FLOWING;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return WATER_OVERLAY;
                    }

                    @Override
                    public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                        return new Vector3f(fog.getRed()/255f, fog.getBlue()/255f, fog.getGreen()/255f);
                    }

                    // @Todo: figure out what I should do about these numbers
                    @Override
                    public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                        RenderSystem.setShaderFogStart(1f);
                        RenderSystem.setShaderFogEnd(6f);
                    }
                });
            }
        };
    }

    public void build(DeferredRegister<Fluid> fluidRegister, DeferredRegister<FluidType> fluidTypeRegister,
                      DeferredRegister<Item> itemRegister, DeferredRegister<Block> blockRegister)
    {
        fluidType = fluidTypeRegister.register(name + "_fluid", this::registerFluidType);

        source = fluidRegister.register(name + "_source",
                () -> new ForgeFlowingFluid.Source(forgeProperties));

        flowing = fluidRegister.register(name + "_flowing",
                () -> new ForgeFlowingFluid.Flowing(forgeProperties));

        block = blockRegister.register(name + "_block", () -> new LiquidBlock(source, blockProperties));

        forgeProperties = new ForgeFlowingFluid.Properties(fluidType, source, flowing)
                .slopeFindDistance(slopeFindDistance)
                .levelDecreasePerBlock(levelDecreasePerBlock)
                .explosionResistance(explosionResistance)
                .tickRate(tickRate)
                .block(block);

        if (hasBucket) {
            bucket = itemRegister.register(name + "_bucket", () -> new BucketItem(source, itemProperties));
            forgeProperties.bucket(bucket);
        }
    }
}
