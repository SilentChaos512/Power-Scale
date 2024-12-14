package net.silentchaos512.powerscale.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Mob;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

public record MobDataPayload(
        int entityId,
        float difficulty,
        int level,
        int blightTier
) implements CustomPacketPayload {
    public static final Type<MobDataPayload> TYPE = new Type<>(PowerScale.getId("mob_data"));

    public static final StreamCodec<FriendlyByteBuf, MobDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, d -> d.entityId,
            ByteBufCodecs.FLOAT, d -> d.difficulty,
            ByteBufCodecs.VAR_INT, d -> d.level,
            ByteBufCodecs.VAR_INT, d -> d.blightTier,
            MobDataPayload::new
    );

    public MobDataPayload(Mob mob) {
        this(
                mob.getId(),
                mob.getData(PsAttachmentTypes.DIFFICULTY).floatValue(),
                mob.getData(PsAttachmentTypes.LEVEL),
                mob.getData(PsAttachmentTypes.BLIGHT_TIER)
        );
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
