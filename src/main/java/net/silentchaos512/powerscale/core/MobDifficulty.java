package net.silentchaos512.powerscale.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.network.payload.MobDataPayload;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsRegistries;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class MobDifficulty {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (!Config.SERVER.quickToggleDifficulty.get()) return;

        final var mob = event.getEntity();

        if (mob.hasData(PsAttachmentTypes.LEVEL)) {
            return;
        }

        final var localDifficulty = DifficultyUtil.getLocalDifficulty(mob.level(), mob.getOnPos());
        setDifficultyAndAttributes(mob, localDifficulty);
    }

    public static void setDifficultyAndAttributes(Mob mob, double difficulty) {
        final var mobDifficulty = DifficultyUtil.setDifficultyClamped(mob, difficulty);
        var level = (int) mobDifficulty + 1;
        mob.setData(PsAttachmentTypes.LEVEL, level);
        // TEST
//        mob.setData(PsAttachmentTypes.BLIGHT_TIER, 1);

        if (PowerScale.detailedLogging()) {
            PowerScale.LOGGER.info("Setting {} to difficulty {} and level {}", mob.getName().getString(), mobDifficulty, level);
        }

        handleAttributeBoosts(mob, level);

        PacketDistributor.sendToAllPlayers(new MobDataPayload(mob));
    }

    private static void handleAttributeBoosts(Mob mob, int level) {
        if (!Config.SERVER.quickToggleScalingAttributes.get()) return;

        for (ScalingAttribute scalingAttribute : PsRegistries.SCALING_ATTRIBUTE) {
            boostAttribute(mob, level, scalingAttribute);
        }
    }

    private static void boostAttribute(Mob mob, int level, ScalingAttribute scalingAttribute) {
        var attributeInstance = mob.getAttribute(scalingAttribute.attribute());
        if (attributeInstance == null) return;

        double oldValue = attributeInstance.getValue();
        double boostAmount = scalingAttribute.getMobBoost(mob, level);
        ResourceLocation id = PsRegistries.SCALING_ATTRIBUTE.getKey(scalingAttribute);
        var modifier = new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + ".level_boost"),
                boostAmount,
                AttributeModifier.Operation.ADD_VALUE
        );
        attributeInstance.addOrReplacePermanentModifier(modifier);
        double newValue = attributeInstance.getValue();

        if (scalingAttribute.attribute().equals(Attributes.MAX_HEALTH)) {
            if (newValue > oldValue) {
                // Heal entity when increasing max health
                float healAmount = (float) (newValue - oldValue);
                mob.heal(healAmount);
            } else if (mob.getHealth() > newValue) {
                // Reduce health to new max
                mob.setHealth((float) newValue);
            }
        }
    }

    public static void onClientSync(Entity entity, MobDataPayload data) {
        entity.setData(PsAttachmentTypes.DIFFICULTY, (double) data.difficulty());
        entity.setData(PsAttachmentTypes.LEVEL, data.level());
        entity.setData(PsAttachmentTypes.BLIGHT_TIER, data.blightTier());
    }
}
