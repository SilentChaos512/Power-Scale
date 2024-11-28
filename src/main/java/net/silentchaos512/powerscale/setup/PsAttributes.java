package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;

public class PsAttributes {
    static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, PowerScale.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> ARROW_DAMAGE = ATTRIBUTES.register(
            "arrow_damage",
            () -> new RangedAttribute(
                    "arrow_damage",
                    0.0,
                    0.0,
                    1024
            ).setSyncable(true)
    );

    @EventBusSubscriber(modid = PowerScale.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static final class Events {
        @SubscribeEvent
        public static void addEntityAttributes(EntityAttributeModificationEvent event) {
            for (EntityType<? extends LivingEntity> type : event.getTypes()) {
                event.add(type, PsAttributes.ARROW_DAMAGE);
            }
        }
    }
}
