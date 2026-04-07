package net.silentchaos512.powerscale.core.scalingattribute;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;

public record CustomMobScalingEntry(
        TagKey<EntityType<?>> matchingEntities, // restricting to a single tag for now, for code simplicity
        ExpressionExtension<?> expression
) {
    public static final Codec<CustomMobScalingEntry> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    TagKey.codec(Registries.ENTITY_TYPE).fieldOf("entity_tag").forGetter(e -> e.matchingEntities),
                    ExpressionExtension.CODEC.fieldOf("expression").forGetter(e -> e.expression)
            ).apply(instance, CustomMobScalingEntry::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CustomMobScalingEntry> STREAM_CODEC = StreamCodec.composite(
            tagStreamCodec(Registries.ENTITY_TYPE), e -> e.matchingEntities,
            ExpressionExtension.STREAM_CODEC, e -> e.expression,
            CustomMobScalingEntry::new
    );

    public boolean matches(Entity entity) {
        return entity.getType().is(this.matchingEntities);
    }

    // TODO: This is copied from CodecUtils in Silent Gear; it should really be moved to Silent Lib
    private static <T> StreamCodec<FriendlyByteBuf, TagKey<T>> tagStreamCodec(ResourceKey<? extends Registry<T>> registryKey) {
        return StreamCodec.of(
                (buf, val) -> buf.writeIdentifier(val.location()),
                buf -> TagKey.create(registryKey, buf.readIdentifier())
        );
    }
}
