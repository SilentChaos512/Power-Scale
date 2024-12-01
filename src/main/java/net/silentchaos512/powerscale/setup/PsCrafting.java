package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.crafting.recipe.AlchemyRecipe;

import java.util.function.Supplier;

public class PsCrafting {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, PowerScale.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, PowerScale.MOD_ID);

    public static final Supplier<RecipeType<AlchemyRecipe>> ALCHEMY_TYPE = RECIPE_TYPES.register(
            "alchemy",
            () -> RecipeType.simple(PowerScale.getId("alchemy"))
    );

    public static final Supplier<RecipeSerializer<AlchemyRecipe>> ALCHEMY_SERIALIZER = RECIPE_SERIALIZERS.register(
            "alchemy",
            AlchemyRecipe.Serializer::new
    );
}
