package net.silentchaos512.powerscale.core.scalingattribute;

import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ScalingAttributeHelper {
    public static void applyBoostedAttributes(LivingEntity entity) {
        Map<DataHolder<ScalingAttribute>, Double> immutableMap = entity.getData(PsAttachmentTypes.BOOSTED_ATTRIBUTES);
        immutableMap.forEach((attribute, amount) -> applyAttributeBoost(entity, attribute, amount));
    }

    private static void applyAttributeBoost(LivingEntity entity, DataHolder<ScalingAttribute> attribute, Double amount) {
        var attributeInstance = entity.getAttribute(attribute.get().attribute());
        if (attributeInstance == null) return;

        attributeInstance.addOrReplacePermanentModifier(
                new AttributeModifier(
                        PowerScale.getId("boost"),
                        amount,
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
        if (attributeInstance.getAttribute().equals(Attributes.MAX_HEALTH)) {
            entity.heal(amount.floatValue());
        }
    }

    public static void modifyBoostsOnDeath(Player player) {
        modifyBoosts(player, sa -> sa.playerMutators().onDeath(), Config.COMMON.notifyOfAttributeChangesOnDeath.get());
    }

    public static void modifyBoostsOnSleep(Player player) {
        modifyBoosts(player, sa -> sa.playerMutators().onSleep(), Config.COMMON.notifyOfAttributeChangesOnSleep.get());
    }

    public static void modifyBoosts(Player player, Function<ScalingAttribute, ExpressionExtension<?>> mutator, boolean sendNotifications) {
        Map<DataHolder<ScalingAttribute>, Double> immutableMap = player.getData(PsAttachmentTypes.BOOSTED_ATTRIBUTES);
        Map<DataHolder<ScalingAttribute>, Double> newMap = new HashMap<>();
        immutableMap.forEach((attribute, amount) -> {
            var mutatorExpression = mutator.apply(attribute.get());
            var newAmount = mutatorExpression
                    .withPlayer(player)
                    .with("value", amount)
                    .evaluateDouble(0.0, player);
            newMap.put(attribute, newAmount);
            if (sendNotifications) {
                notifyOfAttributeChange(player, attribute, amount, newAmount);
            }
        });
        player.setData(PsAttachmentTypes.BOOSTED_ATTRIBUTES, ImmutableMap.copyOf(newMap));
        applyBoostedAttributes(player);
    }

    public static void notifyOfAttributeChange(LivingEntity entity, DataHolder<ScalingAttribute> attribute, double originalAmount, double newAmount) {
        var change = newAmount - originalAmount;
        if (MathUtils.doublesEqual(change, 0.0)) return; // No changes

        var messageColor = change < 0.0 ? ChatFormatting.RED : ChatFormatting.GREEN;
        var changeDirectionText = change < 0.0
                ? Component.translatable("powerscale.notifyAttributeChange.decreased")
                : Component.translatable("powerscale.notifyAttributeChange.increased");
        var changeAmountStr = String.format("%.5f", Math.abs(change))
                .replaceFirst("\\.?0+$", ""); // strip trailing zeros
        var message = Component.translatable(
                "powerscale.notifyAttributeChange",
                attribute.get().getName(),
                changeDirectionText,
                changeAmountStr
        );
        entity.sendSystemMessage(message.withStyle(messageColor));
    }
}
