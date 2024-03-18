package org.jimmybobjim.circuitcraft.datagen;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import org.jimmybobjim.circuitcraft.materials.blocks.CCBlocks;

import java.util.function.Consumer;

public class CCRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public CCRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCBlocks.WIRE_HARNESS_BLOCK.get())
                .pattern("A A")
                .pattern("A A")
                .pattern("A A")
                .define('A', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget", inventoryTrigger(ItemPredicate.Builder.item()
                        .of(Items.IRON_NUGGET).build()))
                .save(pWriter);
    }
}
