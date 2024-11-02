package net.silentchaos512.powerscale.core.scalingattribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;

public record ScalingAttribute(
        Holder<Attribute> attribute,
        MobScalingSet mobScaling,
        MutatorSet playerMutators
) {
    public static final Codec<ScalingAttribute> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(sa -> sa.attribute),
                    MobScalingSet.CODEC.fieldOf("mob_scaling").forGetter(sa -> sa.mobScaling),
                    MutatorSet.CODEC.fieldOf("player_mutators").forGetter(sa -> sa.playerMutators)
            ).apply(instance, ScalingAttribute::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ScalingAttribute> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ATTRIBUTE), sa -> sa.attribute,
            MobScalingSet.STREAM_CODEC, sa -> sa.mobScaling,
            MutatorSet.STREAM_CODEC, sa -> sa.playerMutators,
            ScalingAttribute::new
    );

    public double getMobBoost(Mob mob, int level) {
        return this.mobScaling.getExpressionForMob(mob)
                .with("base_value", mob.getAttributeBaseValue(this.attribute))
                .with("level", level)
                .evaluateDouble(0.0, null);
    }
}
