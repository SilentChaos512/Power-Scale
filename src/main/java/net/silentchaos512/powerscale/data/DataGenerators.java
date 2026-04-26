package net.silentchaos512.powerscale.data;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.silentchaos512.lib.data.recipe.LibRecipeProvider;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.data.client.ModModelProvider;
import net.silentchaos512.powerscale.data.crafting.ModRecipesProvider;
import net.silentchaos512.powerscale.data.loot.ModLootTables;
import net.silentchaos512.powerscale.data.tags.ModBlockTagsProvider;
import net.silentchaos512.powerscale.data.tags.ModEntityTypeTagsProvider;
import net.silentchaos512.powerscale.data.tags.ModItemTagsProvider;
import net.silentchaos512.powerscale.loot.modifier.BonusDropsLootModifier;

@EventBusSubscriber
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        var blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new ModEntityTypeTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, LibRecipeProvider.createRunner(packOutput, lookupProvider, "Power Scale Recipes", ModRecipesProvider::new));

        generator.addProvider(true, new ScalingAttributesProvider(packOutput));

        generator.addProvider(true, new ModModelProvider(packOutput));

        generator.addProvider(true, new ModLootTables(packOutput, lookupProvider));

        generator.addProvider(true, new GlobalLootModifierProvider(packOutput, lookupProvider, PowerScale.MOD_ID) {
            @Override
            protected void start() {
                add("bonus_drops", new BonusDropsLootModifier(new LootItemCondition[0]));
            }
        });
    }
}
