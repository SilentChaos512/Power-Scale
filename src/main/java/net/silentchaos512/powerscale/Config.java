package net.silentchaos512.powerscale;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.parser.ParseException;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.silentchaos512.powerscale.config.ConfiguredExpression;
import net.silentchaos512.powerscale.config.ScalingAttributeConfigSet;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = PowerScale.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    public static final Common COMMON;
    public static final Client CLIENT;
    public static final Server SERVER;

    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec SERVER_SPEC;

    static {
        var commonPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = commonPair.getLeft();
        COMMON_SPEC = commonPair.getRight();

        var clientPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();

        var serverPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER = serverPair.getLeft();
        SERVER_SPEC = serverPair.getRight();
    }

    public static final class Common {
        public final ModConfigSpec.BooleanValue detailedLogging;

        public final ModConfigSpec.BooleanValue simpleAttributeBoosters;
        public final ModConfigSpec.BooleanValue simpleDifficultyMutators;

        public final ModConfigSpec.IntValue playerTimeUntilIdle;
        public final ModConfigSpec.BooleanValue sendIdleNotification;
        public final ModConfigSpec.DoubleValue difficultyPlayerInitial;
        public final ModConfigSpec.DoubleValue difficultyPlayerMax;
        public final ModConfigSpec.DoubleValue difficultyPlayerMin;
        public final ModConfigSpec.DoubleValue difficultyBossMax;
        public final ModConfigSpec.DoubleValue difficultyBossMin;
        public final ModConfigSpec.DoubleValue difficultyHostileMax;
        public final ModConfigSpec.DoubleValue difficultyHostileMin;
        public final ModConfigSpec.DoubleValue difficultyPeacefulMax;
        public final ModConfigSpec.DoubleValue difficultyPeacefulMin;
        public final ModConfigSpec.DoubleValue difficultyLocalMax;
        public final ModConfigSpec.DoubleValue difficultyLocalMin;

        public final ModConfigSpec.BooleanValue notifyOfAttributeChangesOnDeath;
        public final ModConfigSpec.BooleanValue notifyOfAttributeChangesOnSleep;
        public final ModConfigSpec.BooleanValue notifyOfAttributeChangesOnMobKill;
        public final ModConfigSpec.BooleanValue notifyOfAttributeChangesOnBossKill;

        public final ConfiguredExpression difficultyMutatorPerSecond;
        public final ConfiguredExpression localDifficultyOverride;
        public ModConfigSpec.BooleanValue localDifficultyUseOverride;
        public final ConfiguredExpression localDifficultyFromPlayers;
        public final ConfiguredExpression localDifficultyFromLocation;
        public final ConfiguredExpression localDifficultyLunarMultipliers;
        public final ConfiguredExpression localDifficultyGroupBonus;
        public final ConfiguredExpression playerLevelCurve;

        public final ScalingAttributeConfigSet defaultScalingAttributeArrowDamage;
        public final ScalingAttributeConfigSet defaultScalingAttributeAttackDamage;
        public final ScalingAttributeConfigSet defaultScalingAttributeMaxHealth;
        public final ScalingAttributeConfigSet defaultScalingAttributeMovementSpeed;

        private final Map<String, ConfiguredExpression> configuredExpressions = new HashMap<>();

        private Common(ModConfigSpec.Builder builder) {
            detailedLogging = builder
                    .comment(
                            "Prints additional (and often excessive amounts of) information to the log.",
                            "Do not use this unless you are diagnosing development issues."
                    )
                    .define("detailed_logging", false);

            simpleAttributeBoosters = builder
                    .comment("Allows the ingredients for attribute booster tonics (crystals) to be used directly")
                    .define("item.simple_attribute_boosters", false);
            simpleDifficultyMutators = builder
                    .comment("Allows the ingredients for difficulty mutator brews (cursed/enchanted hearts) to be used directly")
                    .define("item.simple_difficulty_mutators", false);

            final double minMaxDifficulty = 999_999;
            difficultyPlayerInitial = builder
                    .comment("The starting difficulty for new players")
                    .defineInRange("difficulty.player_difficulty.initial", 0, -minMaxDifficulty, minMaxDifficulty);
            difficultyPlayerMax = builder
                    .comment("The highest value that a player's difficulty can reach")
                    .defineInRange("difficulty.player_difficulty.max", 250, -minMaxDifficulty, minMaxDifficulty);
            difficultyPlayerMin = builder
                    .comment("The lowest value that a player's difficulty can reach")
                    .defineInRange("difficulty.player_difficulty.min", 0, -minMaxDifficulty, minMaxDifficulty);

            difficultyBossMax = builder
                    .comment("The highest value that a boss mob's difficulty can reach")
                    .defineInRange("difficulty.boss_difficulty.max", 250, -minMaxDifficulty, minMaxDifficulty);
            difficultyBossMin = builder
                    .comment("The lowest value that a boss mob's difficulty can reach")
                    .defineInRange("difficulty.boss_difficulty.min", 0, -minMaxDifficulty, minMaxDifficulty);

            difficultyHostileMax = builder
                    .comment("The highest value that a hostile mob's difficulty can reach")
                    .defineInRange("difficulty.hostile_difficulty.max", 250, -minMaxDifficulty, minMaxDifficulty);
            difficultyHostileMin = builder
                    .comment("The lowest value that a hostile mob's difficulty can reach")
                    .defineInRange("difficulty.hostile_difficulty.min", 0, -minMaxDifficulty, minMaxDifficulty);

            difficultyPeacefulMax = builder
                    .comment("The highest value that a peaceful mob's difficulty can reach")
                    .defineInRange("difficulty.peaceful_difficulty.max", 250, -minMaxDifficulty, minMaxDifficulty);
            difficultyPeacefulMin = builder
                    .comment("The lowest value that a peaceful mob's difficulty can reach")
                    .defineInRange("difficulty.peaceful_difficulty.min", 0, -minMaxDifficulty, minMaxDifficulty);

            difficultyLocalMax = builder
                    .comment("The highest difficulty that can be computed and applied to a mob")
                    .defineInRange("difficulty.local_difficulty.max", 250, -minMaxDifficulty, minMaxDifficulty);
            difficultyLocalMin = builder
                    .comment("The lowest difficulty that can be computed and applied to a mob")
                    .defineInRange("difficulty.local_difficulty.min", 0, -minMaxDifficulty, minMaxDifficulty);

            notifyOfAttributeChangesOnDeath = builder
                    .comment("Send a message to players if their attributes change after dying")
                    .define("notifications.attribute_changes.on_death", true);
            notifyOfAttributeChangesOnSleep = builder
                    .comment("Send a message to players if their attributes change after sleeping")
                    .define("notifications.attribute_changes.on_sleep", true);
            notifyOfAttributeChangesOnMobKill = builder
                    .comment("Send a message to players if their attributes change after killing a non-boss mob")
                    .define("notifications.attribute_changes.on_mob_kill", true);
            notifyOfAttributeChangesOnBossKill = builder
                    .comment("Send a message to players if their attributes change after killing a boss")
                    .define("notifications.attribute_changes.on_boss_kill", true);

            playerTimeUntilIdle = builder
                    .comment("The time (in seconds) until a player is considered idle, which affects the per second difficulty mutator.",
                            "Set to 0 to disable idling.")
                    .defineInRange("difficulty.player.time_until_idle", 120, 0, Integer.MAX_VALUE);
            sendIdleNotification = builder
                    .comment("Notify a player when they become idle")
                    .define("difficulty.player.send_idle_notification", true);
            difficultyMutatorPerSecond = expressionConfig(
                    builder,
                    "difficulty.player_difficulty.mutator.per_second",
                    "difficulty + 0.0011575 * IDLE_MULTIPLIER(0.5)",
                    "(EvalEx) The expression that modifies a player's difficulty every second"
            );
            localDifficultyFromPlayers = expressionConfig(
                    builder,
                    "difficulty.local_difficulty.parts.from_players",
                    "WEIGHTED_AVERAGE_PLAYER_DIFFICULTY(256)"
            );
            localDifficultyFromLocation = expressionConfig(
                    builder,
                    "difficulty.local_difficulty.parts.from_distance",
                    "0.0025 * DISTANCE_FROM_SPAWN() + 0.25 * DEPTH_BELOW(64)"
            );
            localDifficultyLunarMultipliers = expressionConfig(
                    builder,
                    "difficulty.local_difficulty.parts.lunar_multipliers",
                    "LUNAR_CYCLES(1.1, 1.075, 1.05, 1.0, 0.95, 1.0, 1.05, 1.075)"
            );
            localDifficultyGroupBonus = expressionConfig(
                    builder,
                    "difficulty.local_difficulty.parts.group_bonus",
                    "1 + 0.05 * (LOCAL_PLAYER_COUNT(128) - 1)"
            );
            localDifficultyUseOverride = builder
                    .comment("If enabled, local difficulty will be calculated using the override expression. Otherwise, difficulty is calculated using the expressions under \"parts\", with each part being added/multiplied together in a specific order.",
                            "Use the override expression only if you need very fine control over how difficulty is calculated.")
                    .define("difficulty.local_difficulty.use_override", false);
            localDifficultyOverride = expressionConfig(
                    builder,
                    "difficulty.local_difficulty.override_expression",
                    "(" + localDifficultyFromPlayers.getConfigDefault()
                            + " + " + localDifficultyFromLocation.getConfigDefault()
                            + ") * " + localDifficultyLunarMultipliers.getConfigDefault()
                            + " * (" + localDifficultyGroupBonus.getConfigDefault()
                            + ")",
                    "(EvalEx) The expression that calculates local (mob) difficulty.",
                    "Only used when \"use_override\" is set to true. Otherwise, the expressions under \"parts\" are used instead."
            );

            playerLevelCurve = expressionConfig(
                    builder,
                    "player.levelCurve",
                    "500(level * level) - 500*level",
                    "(EvalEx) The expression that computes the SP required to reach a given level"
            );

            defaultScalingAttributeArrowDamage = new ScalingAttributeConfigSet(
                    builder,
                    "scaling_attributes.arrow_damage",
                    "0.1 * (level - 1)",
                    "0.0",
                    "0.1 * (level - 1)",
                    "value",
                    "value",
                    "value",
                    "value"
            );
            defaultScalingAttributeAttackDamage = new ScalingAttributeConfigSet(
                    builder,
                    "scaling_attributes.attack_damage",
                    "0.1 * (level - 1)",
                    "0.0",
                    "0.1 * (level - 1)",
                    "value",
                    "value",
                    "value",
                    "value"
            );
            defaultScalingAttributeMaxHealth = new ScalingAttributeConfigSet(
                    builder,
                    "scaling_attributes.max_health",
                    "REDUCED_SCALING(0.375 * (level - 1), 0.5, 20)",
                    "REDUCED_SCALING(0.125 * (level - 1), 0.5, 20)",
                    "REDUCED_SCALING(0.375 * (level - 1), 0.5, 20)",
                    "value",
                    "value",
                    "value",
                    "value"
            );
            defaultScalingAttributeMovementSpeed = new ScalingAttributeConfigSet(
                    builder,
                    "scaling_attributes.movement_speed",
                    "base_value * 0.05 * FLOOR((level - 51) / 50)",
                    "0.0",
                    "0.0",
                    "value",
                    "value",
                    "value",
                    "value"
            );
        }

        private ConfiguredExpression expressionConfig(ModConfigSpec.Builder builder, String path, String expressionString, String... comments) {
            return new ConfiguredExpression(
                    builder
                            .comment(comments)
                            .define(path, expressionString)
            );
        }
    }

    public static final class Client {
        private Client(ModConfigSpec.Builder builder) {
            ;
        }
    }

    public static final class Server {
        public final ModConfigSpec.BooleanValue quickToggleDifficulty;
        public final ModConfigSpec.BooleanValue quickToggleScalingAttributes;

        private Server(ModConfigSpec.Builder builder) {
            quickToggleDifficulty = builder
                    .comment("Enables the difficulty system, which makes mobs progressively stronger over time")
                    .define("quick_toggles.difficulty", true);
            quickToggleScalingAttributes = builder
                    .comment("Enables the scaling attribute system, which allows players and mobs to gain attribute bonuses, such as extra health and attack damage")
                    .define("quick_toggles.scaling_attributes", true);
        }
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        PowerScale.LOGGER.info("Config loading :)");
    }

    @SubscribeEvent
    static void onReload(final ModConfigEvent.Reloading event) {
        PowerScale.LOGGER.info("Config reloading :)");

        // TEST
        PowerScale.LOGGER.info("Level Curve Test");
        for (int i = 1; i <= 10; ++i) {
            try {
                PowerScale.LOGGER.info("{}: {}", i, COMMON.playerLevelCurve.with("level", i).evaluate().getNumberValue().intValue());
            } catch (EvaluationException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
