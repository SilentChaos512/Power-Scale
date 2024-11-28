package net.silentchaos512.powerscale.core;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.setup.PsAttributes;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class ScalingEvents {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // Add extra damage to arrows (player and mobs)
        if (event.getEntity() instanceof AbstractArrow arrow) {
            var owner = arrow.getOwner();
            if (owner instanceof LivingEntity livingEntity && livingEntity.getAttributes().hasAttribute(PsAttributes.ARROW_DAMAGE)) {
                var extraDamage = livingEntity.getAttributeValue(PsAttributes.ARROW_DAMAGE);
                arrow.setBaseDamage(arrow.getBaseDamage() + extraDamage);
                PowerScale.LOGGER.info("set arrow damage to {}", arrow.getBaseDamage());
            }
        }
    }
}
