package net.silentchaos512.powerscale.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class IdleTracker {
    public static boolean isIdle(Player player) {
        return player.getData(PsAttachmentTypes.IDLE);
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Post event) {
        var player = event.getEntity();
        if (player.tickCount % 20 != 0) return; // Only tick once per second

        if (Config.COMMON.playerTimeUntilIdle.get() == 0) {
            // Idling disabled in config
            player.setData(PsAttachmentTypes.IDLE, false);
        }

        var lastPos = player.getData(PsAttachmentTypes.LAST_POS);
        var idleTime = player.getData(PsAttachmentTypes.IDLE_TIME);
        var idle = player.getData(PsAttachmentTypes.IDLE);

        if (player.blockPosition().equals(lastPos)) {
            // Player not moving, maybe idle?
            var newIdleTime = idleTime + 1;
            player.setData(PsAttachmentTypes.IDLE_TIME, newIdleTime);

            if (!idle && newIdleTime >= Config.COMMON.playerTimeUntilIdle.get()) {
                // Player is now idle
                player.setData(PsAttachmentTypes.IDLE, true);
                if (!player.level().isClientSide && Config.COMMON.sendIdleNotification.get()) {
                    player.sendSystemMessage(Component.translatable("powerscale.notifyIdle"));
                }
            }
        } else {
            // Player moved, no longer idle
            player.setData(PsAttachmentTypes.IDLE_TIME, 0);
            player.setData(PsAttachmentTypes.IDLE, false);
        }

        player.setData(PsAttachmentTypes.LAST_POS, player.blockPosition());
    }
}
