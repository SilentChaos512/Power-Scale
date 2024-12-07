package net.silentchaos512.powerscale.data;

import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.data.client.ModItemModelProvider;
import net.silentchaos512.powerscale.data.crafting.ModRecipesProvider;
import net.silentchaos512.powerscale.data.loot.ModLootTables;
import net.silentchaos512.powerscale.loot.BonusDropsLootModifier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var packOutput = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        var blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(true, blockTagsProvider);
        generator.addProvider(true, new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), existingFileHelper));
        generator.addProvider(true, new ModRecipesProvider(packOutput, lookupProvider));

        generator.addProvider(true, new ScalingAttributesProvider(packOutput));

        generator.addProvider(true, new ModItemModelProvider(packOutput, existingFileHelper));

        generator.addProvider(true, new ModLootTables(packOutput, lookupProvider));

        generator.addProvider(true, new GlobalLootModifierProvider(packOutput, lookupProvider, PowerScale.MOD_ID) {
            @Override
            protected void start() {
                add("bonus_drops", new BonusDropsLootModifier(new LootItemCondition[0]));
            }
        });
    }
}
