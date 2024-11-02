package net.silentchaos512.powerscale.data;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.silentchaos512.powerscale.data.client.ModItemModelProvider;

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

        generator.addProvider(true, new ScalingAttributesProvider(packOutput));

        generator.addProvider(true, new ModItemModelProvider(packOutput, existingFileHelper));
    }
}
