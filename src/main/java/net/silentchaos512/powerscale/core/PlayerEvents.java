package net.silentchaos512.powerscale.core;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerWakeUpEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttributeHelper;
import net.silentchaos512.powerscale.network.payload.ClientDataUpdatePayload;
import net.silentchaos512.powerscale.setup.PsRegistries;

import java.util.Objects;

@EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        if (!player.level().isClientSide()) {
            // Set up initial scaling attribute values
            for (ScalingAttribute scalingAttribute : PsRegistries.SCALING_ATTRIBUTE) {
                tryToApplyAttributeStartingValue(scalingAttribute, player);
            }
        }
    }

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

    private static void tryToApplyAttributeStartingValue(ScalingAttribute scalingAttribute, Player player) {
        double delta;
        if (isMaxHealthAndUsingPlayerOverrideConfig(scalingAttribute)) {
            // Max Health has a unique override config
            var baseValue = player.getAttributeBaseValue(Attributes.MAX_HEALTH);
            delta = Config.COMMON.playerStartingHealthOverride.get() - baseValue;
        } else if (player.getAttributes().hasAttribute(scalingAttribute.attribute()) && scalingAttribute.playerBonusSettings().isPresent()) {
            // All other attributes that apply to players (plus Max Health if override config is not set)
            var playerBonusSettings = scalingAttribute.playerBonusSettings().get();
            var baseValue = player.getAttributeBaseValue(scalingAttribute.attribute());
            delta = playerBonusSettings.startingValue() - baseValue;
        } else {
            // The player does not have this attribute
            delta = 0.0;
        }

        if (!MathUtils.doublesEqual(delta, 0.0)) {
            var attributeInstance = player.getAttribute(scalingAttribute.attribute());
            if (attributeInstance == null) return;
            attributeInstance.addOrReplacePermanentModifier(
                    new AttributeModifier(
                            PowerScale.getId("starting_value_delta"),
                            delta,
                            AttributeModifier.Operation.ADD_VALUE
                    )
            );
        }
    }

    private static boolean isMaxHealthAndUsingPlayerOverrideConfig(ScalingAttribute scalingAttribute) {
        return Config.COMMON.playerStartingHealthOverride.get() > 0
                && Attributes.MAX_HEALTH.isBound()
                && scalingAttribute.attribute().is(Objects.requireNonNull(Attributes.MAX_HEALTH.getKey()));
    }
}
