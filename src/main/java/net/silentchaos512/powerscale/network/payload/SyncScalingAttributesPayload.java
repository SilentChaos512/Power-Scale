package net.silentchaos512.powerscale.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.setup.PsRegistries;

import java.util.HashMap;
import java.util.Map;

public record SyncScalingAttributesPayload(
        Map<Identifier, ScalingAttribute> scalingAttributes
) implements CustomPacketPayload, DataResourcesPayload<ScalingAttribute> {
    public static final Type<SyncScalingAttributesPayload> TYPE = new Type<>(PowerScale.getId("sync_scaling_attributes"));

    private static final StreamCodec<RegistryFriendlyByteBuf, HashMap<Identifier, ScalingAttribute>> MAP_STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            Identifier.STREAM_CODEC,
            ScalingAttribute.STREAM_CODEC
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncScalingAttributesPayload> STREAM_CODEC = StreamCodec.of(
            (buf, data) -> MAP_STREAM_CODEC.encode(buf, new HashMap<>(data.scalingAttributes)),
            buf -> new SyncScalingAttributesPayload(MAP_STREAM_CODEC.decode(buf))
    );

    public SyncScalingAttributesPayload() {
        this(PsRegistries.SCALING_ATTRIBUTE.copyOfMap());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public Map<Identifier, ScalingAttribute> values() {
        return this.scalingAttributes;
    }
}
