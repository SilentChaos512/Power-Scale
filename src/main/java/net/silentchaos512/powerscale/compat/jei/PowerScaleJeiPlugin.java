package net.silentchaos512.powerscale.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetScreen;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import net.silentchaos512.powerscale.setup.PsBlocks;
import net.silentchaos512.powerscale.setup.PsCrafting;

import java.util.stream.Collectors;

@JeiPlugin
public class PowerScaleJeiPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = PowerScale.getId("plugin");

    public static final RecipeType<AlchemyRecipe> ALCHEMY_RECIPE_TYPE = RecipeType.create(PowerScale.MOD_ID, "alchemy", AlchemyRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlchemyRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().level != null;
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        registration.addRecipes(
                ALCHEMY_RECIPE_TYPE,
                recipeManager.getRecipes().stream()
                        .filter(r -> r.value().getType() == PsCrafting.ALCHEMY_TYPE.get())
                        .map(RecipeHolder::value)
                        .map(r -> (AlchemyRecipe) r)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PsBlocks.ALCHEMY_SET, ALCHEMY_RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlchemySetScreen.class, 97, 16, 14, 30, ALCHEMY_RECIPE_TYPE);
    }
}
