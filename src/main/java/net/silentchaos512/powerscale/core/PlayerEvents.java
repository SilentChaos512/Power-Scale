package net.silentchaos512.powerscale.core;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttributeHelper;

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
                ScalingAttributeHelper.modifyBoosts(player, sa -> sa.playerMutators().onBossKill());
            } else {
                ScalingAttributeHelper.modifyBoosts(player, sa -> sa.playerMutators().onMobKill());
            }
        }
    }
}
