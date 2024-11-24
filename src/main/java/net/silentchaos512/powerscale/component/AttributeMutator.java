package net.silentchaos512.powerscale.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;

public record AttributeMutator(
        DataHolder<ScalingAttribute> attribute,
        double amount
) {
    public static final Codec<AttributeMutator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ScalingAttributeManager.HOLDER_CODEC.fieldOf("scaling_attribute").forGetter(d -> d.attribute),
                    Codec.DOUBLE.fieldOf("amount").forGetter(d -> d.amount)
            ).apply(instance, AttributeMutator::new)
    );
    public static final StreamCodec<FriendlyByteBuf, AttributeMutator> STREAM_CODEC = StreamCodec.composite(
            ScalingAttributeManager.HOLDER_STREAM_CODEC, d -> d.attribute,
            ByteBufCodecs.DOUBLE, d -> d.amount,
            AttributeMutator::new
    );
}
