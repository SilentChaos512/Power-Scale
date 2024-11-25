package net.silentchaos512.powerscale.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsItems;
import net.silentchaos512.powerscale.setup.PsTags;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends LibRecipeProvider {
    public ModRecipesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, PowerScale.MOD_ID);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PsItems.FLASK, 2)
                .pattern("#/#")
                .pattern(" # ")
                .define('#', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('/', PsTags.Items.FLASK_GEMS)
                .unlockedBy("has_item", has(Tags.Items.GLASS_BLOCKS_COLORLESS))
                .save(output);
    }
}
