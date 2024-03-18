package org.jimmybobjim.circuitcraft.materials.blocks.custom.wireHarness.bakedModel;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import org.jimmybobjim.circuitcraft.CircuitCraft;

import java.util.function.Function;

public class WireHarnessModelLoader implements IGeometryLoader<WireHarnessModelLoader.WireHarnessGeometry> {
    public static final ResourceLocation GENERATOR_LOADER = new ResourceLocation(CircuitCraft.MODID, "wireharnessloader");

    @Override
    public WireHarnessGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        boolean facade = jsonObject.has("facade") && jsonObject.get("facade").getAsBoolean();
        return new WireHarnessGeometry(facade);
    }

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register("wireharnessloader", new WireHarnessModelLoader());
    }

    public static class WireHarnessGeometry implements IUnbakedGeometry<WireHarnessGeometry> {
        private final boolean FACADE;

        public WireHarnessGeometry(boolean facade) {
            this.FACADE = facade;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new WireHarnessBakedModel(context, FACADE);
        }
    }
}
