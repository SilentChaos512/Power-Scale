package net.silentchaos512.powerscale.client.helper;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.silentchaos512.powerscale.setup.PsCrafting;

import java.util.*;

@EventBusSubscriber
public class ClientRecipeHelper {
    private static final Multimap<RecipeType<?>, RecipeHolder<?>> MAP_BY_TYPE = ArrayListMultimap.create();
    private static final Map<ResourceKey<Recipe<?>>, RecipeHolder<?>> MAP_BY_KEY = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <R extends Recipe<?>> List<R> getRecipes(RecipeType<R> recipeType) {
        var builder = ImmutableList.<R>builder();
        for (var recipeHolder : MAP_BY_TYPE.get(recipeType)) {
            builder.add((R) recipeHolder.value());
        }
        return builder.build();
    }

    public static Optional<RecipeHolder<?>> getRecipe(ResourceKey<Recipe<?>> key) {
        return Optional.ofNullable(MAP_BY_KEY.getOrDefault(key, null));
    }

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(
                PsCrafting.ALCHEMY_TYPE.get()
        );
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        clearRecipeMaps();

        for (RecipeHolder<?> recipeHolder : event.getRecipeMap().values()) {
            addRecipe(recipeHolder);
        }
    }

    private static void clearRecipeMaps() {
        MAP_BY_TYPE.clear();
        MAP_BY_KEY.clear();
    }

    private static void addRecipe(RecipeHolder<?> recipe) {
        MAP_BY_TYPE.put(recipe.value().getType(), recipe);
        MAP_BY_KEY.put(recipe.id(), recipe);
    }
}
