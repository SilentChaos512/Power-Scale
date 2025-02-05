package net.silentchaos512.powerscale.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.silentchaos512.powerscale.PowerScale;

public record ClientDataUpdatePayload(
        float playerDifficulty,
        float localDifficulty
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientDataUpdatePayload> TYPE = new CustomPacketPayload.Type<>(PowerScale.getId("client_data"));

    public static final StreamCodec<FriendlyByteBuf, ClientDataUpdatePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, p -> p.playerDifficulty,
            ByteBufCodecs.FLOAT, p -> p.localDifficulty,
            ClientDataUpdatePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
