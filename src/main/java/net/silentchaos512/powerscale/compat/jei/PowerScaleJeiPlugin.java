package net.silentchaos512.powerscale.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.types.IRecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetScreen;
import net.silentchaos512.powerscale.client.helper.ClientRecipeHelper;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import net.silentchaos512.powerscale.setup.PsBlocks;
import net.silentchaos512.powerscale.setup.PsCrafting;

@JeiPlugin
public class PowerScaleJeiPlugin implements IModPlugin {
    private static final Identifier PLUGIN_ID = PowerScale.getId("plugin");

    public static final IRecipeType<AlchemyRecipe> ALCHEMY_RECIPE_TYPE = IRecipeType.create(PowerScale.MOD_ID, "alchemy", AlchemyRecipe.class);

    @Override
    public Identifier getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new AlchemyRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(ALCHEMY_RECIPE_TYPE, ClientRecipeHelper.getRecipes(PsCrafting.ALCHEMY_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(ALCHEMY_RECIPE_TYPE, PsBlocks.ALCHEMY_SET);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlchemySetScreen.class, 97, 16, 14, 30, ALCHEMY_RECIPE_TYPE);
    }
}
