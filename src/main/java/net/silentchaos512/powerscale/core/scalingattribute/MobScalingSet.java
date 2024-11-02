package net.silentchaos512.powerscale.core.scalingattribute;

import com.ezylang.evalex.Expression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.silentchaos512.powerscale.config.ScalingAttributeConfigSet;
import net.silentchaos512.powerscale.core.EntityGroups;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;
import net.silentchaos512.powerscale.evalex.ExpressionWrapper;

public record MobScalingSet(
        ExpressionExtension<?> hostile,
        ExpressionExtension<?> peaceful,
        ExpressionExtension<?> boss
) {
    public static final Codec<MobScalingSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExpressionExtension.CODEC.fieldOf("hostile_mobs").forGetter(s -> s.hostile),
                    ExpressionExtension.CODEC.fieldOf("peaceful_mobs").forGetter(s -> s.peaceful),
                    ExpressionExtension.CODEC.fieldOf("boss_mobs").forGetter(s -> s.boss)
            ).apply(instance, MobScalingSet::new)
    );

    public static final StreamCodec<FriendlyByteBuf, MobScalingSet> STREAM_CODEC = StreamCodec.composite(
            ExpressionExtension.STREAM_CODEC, s -> s.hostile,
            ExpressionExtension.STREAM_CODEC, s -> s.peaceful,
            ExpressionExtension.STREAM_CODEC, s -> s.boss,
            MobScalingSet::new
    );

    public MobScalingSet(ExpressionExtension<?> shared) {
        this(shared, shared, shared);
    }

    public MobScalingSet(ResourceLocation id, Expression hostile, Expression peaceful, Expression boss) {
        this(
                new ExpressionWrapper(hostile),
                new ExpressionWrapper(peaceful),
                new ExpressionWrapper(boss)
        );
    }

    public MobScalingSet(ResourceLocation id, Expression shared) {
        this(id, shared, shared, shared);
    }

    public MobScalingSet(ScalingAttributeConfigSet configSet) {
        this(configSet.hostileMobScaling(), configSet.peacefulMobScaling(), configSet.bossMobScaling());
    }

    public ExpressionExtension<?> getExpressionForMob(Mob mob) {
        return switch (EntityGroups.from(mob)) {
            case PEACEFUL -> this.peaceful;
            case BOSS -> this.boss;
            default -> this.hostile;
        };
    }
}
