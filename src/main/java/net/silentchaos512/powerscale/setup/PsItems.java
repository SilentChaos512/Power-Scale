package net.silentchaos512.powerscale.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.component.AttributeMutator;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.item.AttributeMutatorItem;
import net.silentchaos512.powerscale.item.DifficultyMeterItem;
import net.silentchaos512.powerscale.item.DifficultyMutatorItem;
import net.silentchaos512.powerscale.item.FlaskItem;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class PsItems {
    static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PowerScale.MOD_ID);

    static {
        ITEMS.registerItem(
                "alchemy_set",
                p -> new BlockItem(PsBlocks.ALCHEMY_SET.get(), p),
                Item.Properties::useBlockDescriptionPrefix
        );
    }

    public static final DeferredItem<Item> ALCHEMY_POWDER = ITEMS.registerItem(
            "alchemy_powder",
            Item::new
    );

    public static final DeferredItem<DifficultyMeterItem> DIFFICULTY_METER = ITEMS.registerItem(
            "difficulty_meter",
            DifficultyMeterItem::new,
            p -> p.stacksTo(1)
    );

    public static final DeferredItem<AttributeMutatorItem> HEART_CRYSTAL = ITEMS.registerItem(
            "heart_crystal",
            p -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::healthBoostModifier),
                    p
            )
    );
    public static final DeferredItem<AttributeMutatorItem> POWER_CRYSTAL = ITEMS.registerItem(
            "power_crystal",
            p -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::powerBoostModifier),
                    p
            )
    );
    public static final DeferredItem<AttributeMutatorItem> ARCHER_CRYSTAL = ITEMS.registerItem(
            "archer_crystal",
            p -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::arrowPowerBoostModifier),
                    p
            )
    );
    public static final DeferredItem<AttributeMutatorItem> WING_CRYSTAL = ITEMS.registerItem(
            "wing_crystal",
            p -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::speedBoostModifier),
                    p
            )
    );

    public static final DeferredItem<DifficultyMutatorItem> CURSED_HEART = ITEMS.registerItem(
            "cursed_heart",
            p -> new DifficultyMutatorItem(
                    true,
                    () -> simpleMutatorWrapper(Config.COMMON.simpleDifficultyMutators, PsItems::difficultyIncreaseModifier),
                    p
            )
    );
    public static final DeferredItem<DifficultyMutatorItem> ENCHANTED_HEART = ITEMS.registerItem(
            "enchanted_heart",
            p -> new DifficultyMutatorItem(
                    true,
                    () -> simpleMutatorWrapper(Config.COMMON.simpleDifficultyMutators, PsItems::difficultyDecreaseModifier),
                    p
            )
    );

    public static final DeferredItem<FlaskItem> FLASK = ITEMS.registerItem(
            "flask",
            FlaskItem::new
    );
    public static final DeferredItem<Item> WATER_FLASK = registerBasicBrew("water_flask");
    public static final DeferredItem<Item> MELLOW_BREW = registerBasicBrew("mellow_brew");
    public static final DeferredItem<Item> TORPID_BREW = registerBasicBrew("torpid_brew");
    public static final DeferredItem<Item> PRETENTIOUS_BREW = registerBasicBrew("pretentious_brew");

    public static final DeferredItem<AttributeMutatorItem> HEALTH_BOOSTER_TONIC = ITEMS.registerItem(
            "health_booster_tonic",
            p -> new AttributeMutatorItem(
                    false,
                    PsItems::healthBoostModifier,
                    p.stacksTo(1)
            )
    );
    public static final DeferredItem<AttributeMutatorItem> POWER_BOOSTER_TONIC = ITEMS.registerItem(
            "power_booster_tonic",
            p -> new AttributeMutatorItem(
                    false,
                    PsItems::powerBoostModifier,
                    p.stacksTo(1)
            )
    );
    public static final DeferredItem<AttributeMutatorItem> ARROW_POWER_BOOSTER_TONIC = ITEMS.registerItem(
            "arrow_power_booster_tonic",
            p -> new AttributeMutatorItem(
                    false,
                    PsItems::arrowPowerBoostModifier,
                    p.stacksTo(1)
            )
    );
    public static final DeferredItem<AttributeMutatorItem> SPEED_BOOSTER_TONIC = ITEMS.registerItem(
            "speed_booster_tonic",
            p -> new AttributeMutatorItem(
                    false,
                    PsItems::speedBoostModifier,
                    p.stacksTo(1)
            )
    );

    public static final DeferredItem<DifficultyMutatorItem> ARDUOUS_BREW = ITEMS.registerItem(
            "arduous_brew",
            p -> new DifficultyMutatorItem(
                    false,
                    PsItems::difficultyIncreaseModifier,
                    p.stacksTo(1)
            )
    );

    public static final DeferredItem<DifficultyMutatorItem> LANGUID_BREW = ITEMS.registerItem(
            "languid_brew",
            p -> new DifficultyMutatorItem(
                    false,
                    PsItems::difficultyDecreaseModifier,
                    p.stacksTo(1)
            )
    );

    private static AttributeMutator healthBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.MAX_HEALTH),
                Config.COMMON.healthBoosterTonicIncreaseAmount.get()
        );
    }

    private static AttributeMutator powerBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.ATTACK_DAMAGE),
                Config.COMMON.powerBoosterTonicIncreaseAmount.get()
        );
    }

    private static AttributeMutator arrowPowerBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.ARROW_DAMAGE),
                Config.COMMON.arrowPowerBoosterTonicIncreaseAmount.get()
        );
    }

    private static AttributeMutator speedBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.MOVEMENT_SPEED),
                Config.COMMON.speedBoosterTonicIncreaseAmount.get()
        );
    }

    @Nullable
    private static AttributeMutator simpleBoosterWrapper(ModConfigSpec.BooleanValue simpleBoosterItemsConfig, Supplier<AttributeMutator> defaultModifierSupplier) {
        if (simpleBoosterItemsConfig.get()) {
            return defaultModifierSupplier.get();
        }
        return null;
    }

    private static Optional<Double> difficultyIncreaseModifier() {
        return Optional.of(10.0);
    }

    private static Optional<Double> difficultyDecreaseModifier() {
        return Optional.of(-10.0);
    }

    private static Optional<Double> simpleMutatorWrapper(ModConfigSpec.BooleanValue simpleDifficultyMutatorsConfig, Supplier<Optional<Double>> defaultModifierSupplier) {
        if (simpleDifficultyMutatorsConfig.get()) {
            return defaultModifierSupplier.get();
        }
        return Optional.empty();
    }

    private static DeferredItem<Item> registerBasicBrew(String name) {
        return ITEMS.registerItem(
                name,
                Item::new,
                p -> p.stacksTo(1)
        );
    }
}
