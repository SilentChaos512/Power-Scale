package net.silentchaos512.powerscale.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.network.payload.MobDataPayload;
import net.silentchaos512.powerscale.network.payload.RequestMobDataPayload;
import net.silentchaos512.powerscale.network.payload.SyncScalingAttributesPayload;

@EventBusSubscriber(modid = PowerScale.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PsNetwork {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1.1");
        // Server to Client
        registrar.playToClient(
                SyncScalingAttributesPayload.TYPE,
                SyncScalingAttributesPayload.STREAM_CODEC,
                (data, ctx) -> PsClientPayloadHandler.getInstance().handleSyncScalingAttributes(data, ctx)
        );
        registrar.playToClient(
                MobDataPayload.TYPE,
                MobDataPayload.STREAM_CODEC,
                (data, ctx) -> PsClientPayloadHandler.getInstance().handleMobData(data, ctx)
        );
        // Client to Server
        registrar.playToServer(
                RequestMobDataPayload.TYPE,
                RequestMobDataPayload.STREAM_CODEC,
                (data, ctx) -> PsServerPayloadHandler.getInstance().handleRequestMobData(data, ctx)
        );
    }
}
