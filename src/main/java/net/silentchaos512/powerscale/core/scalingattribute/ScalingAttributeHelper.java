package net.silentchaos512.powerscale.core.scalingattribute;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

import java.util.Map;

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
}
