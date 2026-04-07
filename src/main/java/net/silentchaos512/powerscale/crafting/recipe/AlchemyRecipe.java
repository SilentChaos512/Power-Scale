package net.silentchaos512.powerscale.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.silentchaos512.powerscale.setup.PsCrafting;
import net.silentchaos512.powerscale.setup.PsRecipeBookCategories;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public final class AlchemyRecipe implements Recipe<AlchemyRecipeInput> {
    private final Ingredient flask;
    private final Ingredient ingredient;
    private final ItemStack result;
    @Nullable private PlacementInfo placementInfo = null;

    public AlchemyRecipe(
            Ingredient flask,
            Ingredient ingredient,
            ItemStack result
    ) {
        this.flask = flask;
        this.ingredient = ingredient;
        this.result = result;
    }

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
    public RecipeSerializer<? extends Recipe<AlchemyRecipeInput>> getSerializer() {
        return PsCrafting.ALCHEMY_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<AlchemyRecipeInput>> getType() {
        return PsCrafting.ALCHEMY_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if (this.placementInfo == null) {
            this.placementInfo = PlacementInfo.create(List.of(this.flask, this.ingredient));
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return PsRecipeBookCategories.ALCHEMY_SET.get();
    }

    public Ingredient flask() {
        return flask;
    }

    public Ingredient ingredient() {
        return ingredient;
    }

    public ItemStack result() {
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AlchemyRecipe) obj;
        return Objects.equals(this.flask, that.flask) &&
                Objects.equals(this.ingredient, that.ingredient) &&
                Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flask, ingredient, result);
    }

    @Override
    public String toString() {
        return "AlchemyRecipe[" +
                "flask=" + flask + ", " +
                "ingredient=" + ingredient + ", " +
                "result=" + result + ']';
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
