package net.silentchaos512.powerscale.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.silentchaos512.powerscale.network.payload.MobDataPayload;
import net.silentchaos512.powerscale.network.payload.RequestMobDataPayload;

import java.util.concurrent.CompletableFuture;

public class PsServerPayloadHandler {
    private static final PsServerPayloadHandler INSTANCE = new PsServerPayloadHandler();

    public static PsServerPayloadHandler getInstance() {
        return INSTANCE;
    }

    private static CompletableFuture<Void> handleData(final IPayloadContext ctx, Runnable handler) {
        return ctx.enqueueWork(handler)
                .exceptionally(e -> {
                    ctx.disconnect(Component.translatable("network.sad.failure", e.getMessage()));
                    return null;
                });
    }

    public void handleRequestMobData(RequestMobDataPayload data, IPayloadContext ctx) {
        handleData(ctx, () -> {
            var entity = ctx.player().level().getEntity(data.entityId());
            if (entity instanceof Mob mob && ctx.player() instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new MobDataPayload(mob));
            }
        });
    }
}
