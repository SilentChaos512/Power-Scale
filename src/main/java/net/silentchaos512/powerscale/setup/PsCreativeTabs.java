package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;

public class PsCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PowerScale.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_TABS.register(
            "main",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(PsItems.HEART_CRYSTAL.get()))
                    .title(Component.translatable("itemGroup.power_scale"))
                    .displayItems((pParameters, pOutput) ->
                            PsItems.ITEMS.getEntries()
                                    .forEach(item -> pOutput.accept(item.get()))
                    )
                    .build()
    );
}
