package net.silentchaos512.powerscale.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiRecipeSorting;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsBlocks;
import net.silentchaos512.powerscale.setup.PsCrafting;

@EmiEntrypoint
public class PowerScaleEmiPlugin implements EmiPlugin {
    public static final EmiStack ALCHEMY_WORKSTATION = EmiStack.of(PsBlocks.ALCHEMY_SET);
    public static final EmiRecipeCategory ALCHEMY_CATEGORY = new EmiRecipeCategory(
            PowerScale.getId("alchemy"),
            ALCHEMY_WORKSTATION,
            new EmiTexture(PowerScale.getId("textures/jei/alchemy_set_background.png"), 0, 0, 64, 60),
            EmiRecipeSorting.none()
    );

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(ALCHEMY_CATEGORY);
        registry.addWorkstation(ALCHEMY_CATEGORY, ALCHEMY_WORKSTATION);

        var recipeManager = registry.getRecipeManager();
        for (var holder : recipeManager.getAllRecipesFor(PsCrafting.ALCHEMY_TYPE.get())) {
            registry.addRecipe(new AlchemyEmiRecipe(holder));
        }
    }
}
