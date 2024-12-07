package net.silentchaos512.powerscale.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.silentchaos512.powerscale.setup.PsCrafting;

public record AlchemyRecipe(
        Ingredient flask,
        Ingredient ingredient,
        ItemStack result
) implements Recipe<AlchemyRecipeInput> {
    @Override
    public boolean matches(AlchemyRecipeInput input, Level level) {
        return this.flask.test(input.flask()) && this.ingredient.test(input.ingredient());
    }

    @Override
    public ItemStack assemble(AlchemyRecipeInput input, HolderLookup.Provider pRegistries) {
        var result = input.flask().transmuteCopy(this.result.getItem());
        result.applyComponents(this.result.getComponentsPatch());
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PsCrafting.ALCHEMY_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return PsCrafting.ALCHEMY_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<AlchemyRecipe> {
        private static final MapCodec<AlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC.fieldOf("flask").forGetter(r -> r.flask),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.ingredient),
                        ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result)
                ).apply(instance, AlchemyRecipe::new)
        );
        private static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.flask,
                Ingredient.CONTENTS_STREAM_CODEC, r -> r.ingredient,
                ItemStack.STREAM_CODEC, r -> r.result,
                AlchemyRecipe::new
        );

        @Override
        public MapCodec<AlchemyRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
