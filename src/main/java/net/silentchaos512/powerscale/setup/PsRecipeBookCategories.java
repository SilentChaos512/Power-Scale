package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;

public class PsRecipeBookCategories {
    static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(BuiltInRegistries.RECIPE_BOOK_CATEGORY, PowerScale.MOD_ID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> ALCHEMY_SET = register("alchemy_set");

    private static DeferredHolder<RecipeBookCategory, RecipeBookCategory> register(String path) {
        return RECIPE_BOOK_CATEGORIES.register(path, RecipeBookCategory::new);
    }
}
