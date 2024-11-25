package net.silentchaos512.powerscale.setup;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.component.AttributeMutator;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.item.AttributeMutatorItem;
import net.silentchaos512.powerscale.item.FlaskItem;

import java.util.function.Supplier;

public class PsItems {
    static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PowerScale.MOD_ID);

    public static final DeferredItem<AttributeMutatorItem> HEART_CRYSTAL = ITEMS.register(
            "heart_crystal",
            () -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::healthBoostModifier),
                    new Item.Properties()
            )
    );
    public static final DeferredItem<AttributeMutatorItem> POWER_CRYSTAL = ITEMS.register(
            "power_crystal",
            () -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::powerBoostModifier),
                    new Item.Properties()
            )
    );
    public static final DeferredItem<AttributeMutatorItem> WING_CRYSTAL = ITEMS.register(
            "wing_crystal",
            () -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::speedBoostModifier),
                    new Item.Properties()
            )
    );

    public static final DeferredItem<FlaskItem> FLASK = ITEMS.register(
            "flask",
            () -> new FlaskItem(new Item.Properties())
    );
    public static final DeferredItem<Item> WATER_FLASK = ITEMS.register(
            "water_flask",
            () -> new Item(new Item.Properties().stacksTo(1))
    );

    public static final DeferredItem<AttributeMutatorItem> HEALTH_BOOSTER_TONIC = ITEMS.register(
            "health_booster_tonic",
            () -> new AttributeMutatorItem(
                    false,
                    PsItems::healthBoostModifier,
                    new Item.Properties()
                            .stacksTo(1)
            )
    );
    public static final DeferredItem<AttributeMutatorItem> POWER_BOOSTER_TONIC = ITEMS.register(
            "power_booster_tonic",
            () -> new AttributeMutatorItem(
                    false,
                    PsItems::powerBoostModifier,
                    new Item.Properties()
                            .stacksTo(1)
            )
    );
    public static final DeferredItem<AttributeMutatorItem> SPEED_BOOSTER_TONIC = ITEMS.register(
            "speed_booster_tonic",
            () -> new AttributeMutatorItem(
                    false,
                    PsItems::speedBoostModifier,
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    private static AttributeMutator healthBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.MAX_HEALTH),
                2.0
        );
    }

    private static AttributeMutator powerBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.ATTACK_DAMAGE),
                0.5
        );
    }

    private static AttributeMutator speedBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.MOVEMENT_SPEED),
                0.01
        );
    }

    private static AttributeMutator simpleBoosterWrapper(ModConfigSpec.BooleanValue simpleBoosterItemsConfig, Supplier<AttributeMutator> defaultModifierSupplier) {
        if (simpleBoosterItemsConfig.get()) {
            return defaultModifierSupplier.get();
        }
        return null;
    }
}
