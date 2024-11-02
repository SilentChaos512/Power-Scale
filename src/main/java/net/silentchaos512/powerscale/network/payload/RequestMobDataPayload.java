package net.silentchaos512.powerscale.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.silentchaos512.powerscale.PowerScale;

public record RequestMobDataPayload(
        int entityId
) implements CustomPacketPayload {
    public static final Type<RequestMobDataPayload> TYPE = new Type<>(PowerScale.getId("request_mob_data"));

    public static final StreamCodec<FriendlyByteBuf, RequestMobDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, d -> d.entityId,
            RequestMobDataPayload::new
    );

    public RequestMobDataPayload(Entity entity) {
        this(entity.getId());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
