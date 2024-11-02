package net.silentchaos512.powerscale.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;

public record ScalingAttributeConfigSet(
        ExpressionExtension<?> hostileMobScaling,
        ExpressionExtension<?> peacefulMobScaling,
        ExpressionExtension<?> bossMobScaling,
        ExpressionExtension<?> onMobKill,
        ExpressionExtension<?> onBossKill,
        ExpressionExtension<?> onDeath,
        ExpressionExtension<?> onSleep
) {
    public ScalingAttributeConfigSet(
            ModConfigSpec.Builder builder,
            String configPathRoot,
            String hostileMobScalingExpression,
            String peacefulMobScalingExpression,
            String bossMobScalingExpression,
            String onMobKillExpression,
            String onBossKillExpression,
            String onDeathExpression,
            String onSleepExpression
    ) {
        this(
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Scaling expression for hostile mobs")
                                .define(configPathRoot + ".mob_scaling.hostile", hostileMobScalingExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Scaling expression for peaceful mobs")
                                .define(configPathRoot + ".mob_scaling.peaceful", peacefulMobScalingExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Scaling expression for boss mobs")
                                .define(configPathRoot + ".mob_scaling.boss", bossMobScalingExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Player bonus change when a mob is killed")
                                .define(configPathRoot + ".mutator.player_kills_mob", onMobKillExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Player bonus change when a boss is killed")
                                .define(configPathRoot + ".mutator.player_kills_boss", onBossKillExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Player bonus change when the player dies")
                                .define(configPathRoot + ".mutator.player_dies", onDeathExpression)
                ),
                new ConfiguredExpression(
                        builder
                                .comment("(EvalEx) Player bonus change when the player sleeps")
                                .define(configPathRoot + ".mutator.player_sleeps", onSleepExpression)
                )
        );
    }
}
