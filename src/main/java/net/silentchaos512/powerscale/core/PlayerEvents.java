package net.silentchaos512.powerscale.core;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttributeHelper;
import net.silentchaos512.powerscale.network.payload.ClientDataUpdatePayload;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = PowerScale.MOD_ID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ScalingAttributeHelper.modifyBoostsOnDeath(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerWakeUpEvent event) {
        ScalingAttributeHelper.modifyBoostsOnSleep(event.getEntity());
    }

    @SubscribeEvent
    public static void onMobKilled(LivingDeathEvent event) {
        var killer = event.getSource().getEntity();
        if (killer instanceof Player player) {
            var killedEntity = event.getEntity();
            var isBoss = EntityGroups.BOSS.test(killedEntity);
            if (isBoss) {
                ScalingAttributeHelper.modifyBoosts(
                        player,
                        sa -> sa.playerMutators().onBossKill(),
                        Config.COMMON.notifyOfAttributeChangesOnBossKill.get()
                );
            } else {
                ScalingAttributeHelper.modifyBoosts(
                        player,
                        sa -> sa.playerMutators().onMobKill(),
                        Config.COMMON.notifyOfAttributeChangesOnMobKill.get()
                );
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        // Only send client updates every few seconds
        if (event.getEntity().tickCount % 200 != 0) return;
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

        sendClientUpdate(serverPlayer);
    }

    public static void sendClientUpdate(ServerPlayer serverPlayer) {
        var payload = new ClientDataUpdatePayload(
                (float) DifficultyUtil.getModifiedPlayerDifficulty(serverPlayer),
                (float) DifficultyUtil.getLocalDifficulty(serverPlayer.level(), serverPlayer.position())
        );
        PacketDistributor.sendToPlayer(serverPlayer, payload);
    }
}
