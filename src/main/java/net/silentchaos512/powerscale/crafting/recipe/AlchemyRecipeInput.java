package net.silentchaos512.powerscale.crafting.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record AlchemyRecipeInput(
        ItemStack flask,
        ItemStack ingredient
) implements RecipeInput {
    @Override
    public ItemStack getItem(int pIndex) {
        return switch (pIndex) {
            case 0 -> flask;
            case 1 -> ingredient;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + pIndex);
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
