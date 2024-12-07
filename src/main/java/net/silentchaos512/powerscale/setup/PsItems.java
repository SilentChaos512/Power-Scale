package net.silentchaos512.powerscale.setup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.component.AttributeMutator;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.item.AttributeMutatorItem;
import net.silentchaos512.powerscale.item.DifficultyMutatorItem;
import net.silentchaos512.powerscale.item.FlaskItem;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class PsItems {
    static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PowerScale.MOD_ID);

    static {
        ITEMS.register("alchemy_set", simpleBlockItem(PsBlocks.ALCHEMY_SET));
    }

    public static final DeferredItem<Item> ALCHEMY_POWDER = ITEMS.register(
            "alchemy_powder",
            () -> new Item(new Item.Properties())
    );

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
    public static final DeferredItem<AttributeMutatorItem> ARCHER_CRYSTAL = ITEMS.register(
            "archer_crystal",
            () -> new AttributeMutatorItem(
                    true,
                    () -> simpleBoosterWrapper(Config.COMMON.simpleAttributeBoosters, PsItems::arrowPowerBoostModifier),
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

    public static final DeferredItem<DifficultyMutatorItem> CURSED_HEART = ITEMS.register(
            "cursed_heart",
            () -> new DifficultyMutatorItem(
                    true,
                    () -> simpleMutatorWrapper(Config.COMMON.simpleDifficultyMutators, PsItems::difficultyIncreaseModifier),
                    new Item.Properties()
            )
    );
    public static final DeferredItem<DifficultyMutatorItem> ENCHANTED_HEART = ITEMS.register(
            "enchanted_heart",
            () -> new DifficultyMutatorItem(
                    true,
                    () -> simpleMutatorWrapper(Config.COMMON.simpleDifficultyMutators, PsItems::difficultyDecreaseModifier),
                    new Item.Properties()
            )
    );

    public static final DeferredItem<FlaskItem> FLASK = ITEMS.register(
            "flask",
            () -> new FlaskItem(new Item.Properties())
    );
    public static final DeferredItem<Item> WATER_FLASK = registerBasicBrew("water_flask");
    public static final DeferredItem<Item> MELLOW_BREW = registerBasicBrew("mellow_brew");
    public static final DeferredItem<Item> TORPID_BREW = registerBasicBrew("torpid_brew");
    public static final DeferredItem<Item> PRETENTIOUS_BREW = registerBasicBrew("pretentious_brew");

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
    public static final DeferredItem<AttributeMutatorItem> ARROW_POWER_BOOSTER_TONIC = ITEMS.register(
            "arrow_power_booster_tonic",
            () -> new AttributeMutatorItem(
                    false,
                    PsItems::arrowPowerBoostModifier,
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

    public static final DeferredItem<DifficultyMutatorItem> ARDUOUS_BREW = ITEMS.register(
            "arduous_brew",
            () -> new DifficultyMutatorItem(
                    false,
                    PsItems::difficultyIncreaseModifier,
                    new Item.Properties()
                            .stacksTo(1)
            )
    );

    public static final DeferredItem<DifficultyMutatorItem> LANGUID_BREW = ITEMS.register(
            "languid_brew",
            () -> new DifficultyMutatorItem(
                    false,
                    PsItems::difficultyDecreaseModifier,
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

    private static AttributeMutator arrowPowerBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.ARROW_DAMAGE),
                0.25
        );
    }

    private static AttributeMutator speedBoostModifier() {
        return new AttributeMutator(
                ScalingAttributeManager.getHolder(Const.MOVEMENT_SPEED),
                0.01
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
        return ITEMS.register(
                name,
                () -> new Item(new Item.Properties().stacksTo(1))
        );
    }

    private static <T extends Block> Supplier<BlockItem> simpleBlockItem(DeferredBlock<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties());
    }
}
