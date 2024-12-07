package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetMenu;
import net.silentchaos512.powerscale.block.AlchemySetScreen;

import java.util.function.Supplier;

public class PsMenuTypes {
    static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, PowerScale.MOD_ID);

    public static final Supplier<MenuType<AlchemySetMenu>> ALCHEMY_SET = MENU_TYPES.register(
            "alchemy_set",
            () -> IMenuTypeExtension.create(AlchemySetMenu::new)
    );

    @OnlyIn(Dist.CLIENT)
    @EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = PowerScale.MOD_ID)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ALCHEMY_SET.get(), AlchemySetScreen::new);
        }
    }
}
