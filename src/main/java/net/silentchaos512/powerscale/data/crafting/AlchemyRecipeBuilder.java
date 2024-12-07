package net.silentchaos512.powerscale.data.crafting;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import org.jetbrains.annotations.Nullable;

public record AlchemyRecipeBuilder(
        Ingredient baseBrewOrWaterFlask,
        Ingredient brewingIngredient,
        ItemStack result
) implements RecipeBuilder {
    public static AlchemyRecipeBuilder of(ItemLike result, ItemLike baseBrewOrWaterFlask, ItemLike brewingIngredient) {
        return of(new ItemStack(result), baseBrewOrWaterFlask, brewingIngredient);
    }

    public static AlchemyRecipeBuilder of(ItemStack result, ItemLike baseBrewOrWaterFlask, ItemLike brewingIngredient) {
        return new AlchemyRecipeBuilder(
                Ingredient.of(baseBrewOrWaterFlask),
                Ingredient.of(brewingIngredient),
                result
        );
    }

    @Override
    public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput pRecipeOutput, ResourceLocation pId) {
        var recipe = new AlchemyRecipe(this.baseBrewOrWaterFlask, this.brewingIngredient, this.result);
        pRecipeOutput.accept(pId, recipe, null);
    }
}
