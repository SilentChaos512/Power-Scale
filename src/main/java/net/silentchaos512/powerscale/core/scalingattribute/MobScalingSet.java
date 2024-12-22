package net.silentchaos512.powerscale.core.scalingattribute;

import com.ezylang.evalex.Expression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Mob;
import net.silentchaos512.powerscale.config.ScalingAttributeConfigSet;
import net.silentchaos512.powerscale.core.EntityGroups;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;
import net.silentchaos512.powerscale.evalex.ExpressionWrapper;

import java.util.List;

public record MobScalingSet(
        ExpressionExtension<?> hostile,
        ExpressionExtension<?> peaceful,
        ExpressionExtension<?> boss,
        List<CustomMobScalingEntry> customMobScalingEntries
) {
    public static final Codec<MobScalingSet> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ExpressionExtension.CODEC.fieldOf("hostile_mobs").forGetter(s -> s.hostile),
                    ExpressionExtension.CODEC.fieldOf("peaceful_mobs").forGetter(s -> s.peaceful),
                    ExpressionExtension.CODEC.fieldOf("boss_mobs").forGetter(s -> s.boss),
                    CustomMobScalingEntry.CODEC.listOf().optionalFieldOf("custom", List.of()).forGetter(s -> s.customMobScalingEntries)
            ).apply(instance, MobScalingSet::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, MobScalingSet> STREAM_CODEC = StreamCodec.composite(
            ExpressionExtension.STREAM_CODEC, s -> s.hostile,
            ExpressionExtension.STREAM_CODEC, s -> s.peaceful,
            ExpressionExtension.STREAM_CODEC, s -> s.boss,
            CustomMobScalingEntry.STREAM_CODEC.apply(ByteBufCodecs.list()), s -> s.customMobScalingEntries,
            MobScalingSet::new
    );

    public MobScalingSet(ExpressionExtension<?> shared) {
        this(shared, shared, shared, List.of());
    }

    public MobScalingSet(Expression hostile, Expression peaceful, Expression boss) {
        this(
                new ExpressionWrapper(hostile),
                new ExpressionWrapper(peaceful),
                new ExpressionWrapper(boss),
                List.of()
        );
    }

    public MobScalingSet(ScalingAttributeConfigSet configSet) {
        this(configSet.hostileMobScaling(), configSet.peacefulMobScaling(), configSet.bossMobScaling(), List.of());
    }

    public MobScalingSet(ScalingAttributeConfigSet configSet, List<CustomMobScalingEntry> customMobScalingEntryList) {
        this(configSet.hostileMobScaling(), configSet.peacefulMobScaling(), configSet.bossMobScaling(), customMobScalingEntryList);
    }

    public ExpressionExtension<?> getExpressionForMob(Mob mob) {
        for (var entry : this.customMobScalingEntries) {
            if (entry.matches(mob)) {
                return entry.expression();
            }
        }
        return switch (EntityGroups.from(mob)) {
            case PEACEFUL -> this.peaceful;
            case BOSS -> this.boss;
            default -> this.hostile;
        };
    }
}
