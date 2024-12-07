package net.silentchaos512.powerscale.data.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsBlocks;
import net.silentchaos512.powerscale.setup.PsItems;
import net.silentchaos512.powerscale.setup.PsTags;

import java.util.concurrent.CompletableFuture;

public class ModRecipesProvider extends LibRecipeProvider {
    public ModRecipesProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        super(packOutput, registries, PowerScale.MOD_ID);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        buildCrafting(output);
        buildAlchemy(output);
    }

    private static void buildCrafting(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, PsItems.ALCHEMY_POWDER)
                .requires(Tags.Items.GUNPOWDERS)
                .requires(Items.CLAY_BALL)
                .unlockedBy("has_item", has(Tags.Items.GUNPOWDERS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PsItems.FLASK, 2)
                .pattern("#/#")
                .pattern(" # ")
                .define('#', Tags.Items.GLASS_BLOCKS_COLORLESS)
                .define('/', PsTags.Items.FLASK_GEMS)
                .unlockedBy("has_item", has(Tags.Items.GLASS_BLOCKS_COLORLESS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PsBlocks.ALCHEMY_SET)
                .pattern("GC ")
                .pattern("GF ")
                .pattern("###")
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('C', Tags.Items.INGOTS_COPPER)
                .define('F', PsItems.FLASK)
                .define('#', Items.SMOOTH_STONE)
                .unlockedBy("has_item", has(PsItems.FLASK.get()))
                .save(output);
    }

    private void buildAlchemy(RecipeOutput output) {
        AlchemyRecipeBuilder.of(PsItems.MELLOW_BREW, PsItems.WATER_FLASK, Items.CHARCOAL).save(output);
        AlchemyRecipeBuilder.of(PsItems.TORPID_BREW, PsItems.WATER_FLASK, Items.SOUL_SAND).save(output);
        AlchemyRecipeBuilder.of(PsItems.PRETENTIOUS_BREW, PsItems.WATER_FLASK, Items.CHORUS_FRUIT).save(output);

        AlchemyRecipeBuilder.of(PsItems.HEALTH_BOOSTER_TONIC, PsItems.MELLOW_BREW, PsItems.HEART_CRYSTAL).save(output);
        AlchemyRecipeBuilder.of(PsItems.POWER_BOOSTER_TONIC, PsItems.TORPID_BREW, PsItems.POWER_CRYSTAL).save(output);
        AlchemyRecipeBuilder.of(PsItems.ARROW_POWER_BOOSTER_TONIC, PsItems.TORPID_BREW, PsItems.ARCHER_CRYSTAL).save(output);
        AlchemyRecipeBuilder.of(PsItems.SPEED_BOOSTER_TONIC, PsItems.PRETENTIOUS_BREW, PsItems.WING_CRYSTAL).save(output);

        AlchemyRecipeBuilder.of(PsItems.ARDUOUS_BREW, PsItems.MELLOW_BREW, PsItems.CURSED_HEART).save(output);
        AlchemyRecipeBuilder.of(PsItems.LANGUID_BREW, PsItems.MELLOW_BREW, PsItems.ENCHANTED_HEART).save(output);
    }
}
