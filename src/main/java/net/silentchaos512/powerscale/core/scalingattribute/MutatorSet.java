package net.silentchaos512.powerscale.core.scalingattribute;

import com.ezylang.evalex.Expression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.silentchaos512.powerscale.config.ScalingAttributeConfigSet;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;
import net.silentchaos512.powerscale.evalex.ExpressionWrapper;

public record MutatorSet(
        ExpressionExtension<?> onMobKill,
        ExpressionExtension<?> onBossKill,
        ExpressionExtension<?> onDeath,
        ExpressionExtension<?> onSleep
) {
    private static final String ON_PLAYER_KILL_MOB = "on_player_kill_mob";
    private static final String ON_PLAYER_KILL_BOSS = "on_player_kill_boss";
    private static final String ON_PLAYER_DEATH = "on_player_death";
    private static final String ON_PLAYER_SLEEP = "on_player_sleep";

    public static final Codec<MutatorSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExpressionExtension.CODEC.fieldOf(ON_PLAYER_KILL_MOB).forGetter(ms -> ms.onMobKill),
                    ExpressionExtension.CODEC.fieldOf(ON_PLAYER_KILL_BOSS).forGetter(ms -> ms.onBossKill),
                    ExpressionExtension.CODEC.fieldOf(ON_PLAYER_DEATH).forGetter(ms -> ms.onDeath),
                    ExpressionExtension.CODEC.fieldOf(ON_PLAYER_SLEEP).forGetter(ms -> ms.onSleep)
            ).apply(instance, MutatorSet::new)
    );

    public static final StreamCodec<FriendlyByteBuf, MutatorSet> STREAM_CODEC = StreamCodec.composite(
            ExpressionExtension.STREAM_CODEC, ms -> ms.onMobKill,
            ExpressionExtension.STREAM_CODEC, ms -> ms.onBossKill,
            ExpressionExtension.STREAM_CODEC, ms -> ms.onDeath,
            ExpressionExtension.STREAM_CODEC, ms -> ms.onSleep,
            MutatorSet::new
    );

    public MutatorSet(ScalingAttributeConfigSet configSet) {
        this(
                configSet.onMobKill(),
                configSet.onBossKill(),
                configSet.onDeath(),
                configSet.onSleep()
        );
    }

    public static MutatorSet noChanges(ResourceLocation id) {
        return new MutatorSet(
                new ExpressionWrapper(new Expression("value")),
                new ExpressionWrapper(new Expression("value")),
                new ExpressionWrapper(new Expression("value")),
                new ExpressionWrapper(new Expression("value"))
        );
    }
}
