package net.silentchaos512.powerscale.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.network.payload.SyncScalingAttributesPayload;
import net.silentchaos512.powerscale.setup.PsRegistries;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class ServerEvents {
    private ServerEvents() {}

    @SubscribeEvent
    public static void onDataPackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() != null) {
            PacketDistributor.sendToPlayer(
                    event.getPlayer(),
                    new SyncScalingAttributesPayload()
            );
        } else {
            PacketDistributor.sendToAllPlayers(
                    new SyncScalingAttributesPayload()
            );
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPlayerJoinServer(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PsRegistries.SCALING_ATTRIBUTE.getErrorMessages(serverPlayer).forEach(serverPlayer::sendSystemMessage);

        PlayerEvents.sendClientUpdate(serverPlayer);
    }
}
