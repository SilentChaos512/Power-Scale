package net.silentchaos512.powerscale.core.scalingattribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record PlayerBonusSettings(
        double startingValue,
        double minValue,
        double maxValue
) {
    public static final Codec<PlayerBonusSettings> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("starting_value").forGetter(s -> s.startingValue),
                    Codec.DOUBLE.fieldOf("min_value").forGetter(s -> s.minValue),
                    Codec.DOUBLE.fieldOf("max_value").forGetter(s -> s.maxValue)
            ).apply(instance, PlayerBonusSettings::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerBonusSettings> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, s -> s.startingValue,
            ByteBufCodecs.DOUBLE, s -> s.minValue,
            ByteBufCodecs.DOUBLE, s -> s.maxValue,
            PlayerBonusSettings::new
    );
}
