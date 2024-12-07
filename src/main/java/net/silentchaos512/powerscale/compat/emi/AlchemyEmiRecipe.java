package net.silentchaos512.powerscale.compat.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;
import net.silentchaos512.powerscale.setup.PsTags;

import java.util.List;

public class AlchemyEmiRecipe extends BasicEmiRecipe {
    public static final ResourceLocation BACKGROUND_TEXTURE = PowerScale.getId("textures/jei/alchemy_set_background.png");
    public static final EmiIngredient ALCHEMY_FUELS = EmiIngredient.of(PsTags.Items.ALCHEMY_FUELS);

    public AlchemyEmiRecipe(RecipeHolder<AlchemyRecipe> recipe) {
        super(PowerScaleEmiPlugin.ALCHEMY_CATEGORY, recipe.id(), 114, 61);
        this.inputs.add(EmiIngredient.of(recipe.value().flask()));
        this.inputs.add(EmiIngredient.of(recipe.value().ingredient()));
        this.outputs.add(EmiStack.of(recipe.value().result()));
    }

    @Override
    public void addWidgets(WidgetHolder widgetHolder) {
        widgetHolder.addTexture(BACKGROUND_TEXTURE, 1, 1, 64, 60, 0, 0, 64, 60, 64, 60);
        widgetHolder.addSlot(ALCHEMY_FUELS, 1, 37);
        widgetHolder.addSlot(inputs.get(0), 24, 43).drawBack(false); // Flask
        widgetHolder.addSlot(inputs.get(1), 24, 2).drawBack(false); // Ingredient
        widgetHolder.addSlot(outputs.get(0), 81, 3).recipeContext(this);
    }

    @Override
    public List<EmiIngredient> getCatalysts() {
        return List.of(EmiIngredient.of(PsTags.Items.ALCHEMY_FUELS));
    }
}
