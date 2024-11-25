package net.silentchaos512.powerscale.setup;

import net.neoforged.bus.api.IEventBus;

public class Registration {
    public static void register(IEventBus modEventBus) {
        PsAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        PsCreativeTabs.CREATIVE_TABS.register(modEventBus);
        PsDataComponents.DATA_COMPONENTS.register(modEventBus);
        PsItems.ITEMS.register(modEventBus);
        PsSounds.SOUND_EVENTS.register(modEventBus);
    }
}
