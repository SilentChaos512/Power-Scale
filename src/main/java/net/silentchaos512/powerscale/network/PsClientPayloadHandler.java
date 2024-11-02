package net.silentchaos512.powerscale.network;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.silentchaos512.powerscale.core.MobDifficulty;
import net.silentchaos512.powerscale.network.payload.MobDataPayload;
import net.silentchaos512.powerscale.network.payload.SyncScalingAttributesPayload;
import net.silentchaos512.powerscale.setup.PsRegistries;

import java.util.concurrent.CompletableFuture;

public class PsClientPayloadHandler {
    private static final PsClientPayloadHandler INSTANCE = new PsClientPayloadHandler();

    public static PsClientPayloadHandler getInstance() {
        return INSTANCE;
    }

    private static CompletableFuture<Void> handleData(final IPayloadContext ctx, Runnable handler) {
        return ctx.enqueueWork(handler)
                .exceptionally(e -> {
                    ctx.disconnect(Component.translatable("network.sad.failure", e.getMessage()));
                    return null;
                });
    }

    public void handleMobData(MobDataPayload data, IPayloadContext ctx) {
        handleData(ctx, () -> {
            var entity = ctx.player().level().getEntity(data.entityId());
            if (entity != null) {
                MobDifficulty.onClientSync(entity, data);
            }
        });
    }

    public void handleSyncScalingAttributes(SyncScalingAttributesPayload data, IPayloadContext ctx) {
        handleData(ctx, () -> PsRegistries.SCALING_ATTRIBUTE.handleSyncPacket(data, ctx));
    }
}
