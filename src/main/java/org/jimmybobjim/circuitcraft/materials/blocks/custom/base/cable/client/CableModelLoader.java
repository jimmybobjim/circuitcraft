package org.jimmybobjim.circuitcraft.materials.blocks.custom.base.cable.client;

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

public class CableModelLoader implements IGeometryLoader<CableModelLoader.CableModelGeometry> {
    public static final ResourceLocation GENERATOR_LOADER = new ResourceLocation(CircuitCraft.MODID, "cableloader");

    @Override
    public CableModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        boolean facade = jsonObject.has("facade") && jsonObject.get("facade").getAsBoolean();
        float width = jsonObject.has("width") ? jsonObject.get("width").getAsFloat() : -1;
        CableSpriteData data = jsonObject.has("sprites")&&!jsonObject.get("sprites").getAsString().equals("null") ? CableSpriteData.deserialize(jsonObject.get("sprites")) : null;

        return new CableModelGeometry(facade, width, data);
    }

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register(GENERATOR_LOADER.getPath(), new CableModelLoader());
    }

    public static class CableModelGeometry implements IUnbakedGeometry<CableModelGeometry> {
        private final boolean facade;
        private final float width;
        private final CableSpriteData sprites;

        public CableModelGeometry(boolean facade, float width, CableSpriteData sprites) {
            this.facade = facade;
            this.width = width;
            this.sprites = sprites;
        }

        @Override
        public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
            return new CableBakedModel(context, facade, width, sprites);
        }
    }
}
