package net.silentchaos512.powerscale.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Zombie;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.network.payload.MobDataPayload;
import net.silentchaos512.powerscale.network.payload.RequestMobDataPayload;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsRegistries;
import net.silentchaos512.powerscale.setup.PsTags;

@EventBusSubscriber(modid = PowerScale.MOD_ID)
public class MobDifficulty {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        if (!Config.SERVER.quickToggleDifficulty.get()) return;

        final var mob = event.getEntity();

        if (mob.hasData(PsAttachmentTypes.LEVEL) || mob.getType().is(PsTags.EntityTypes.DIFFICULTY_EXEMPT)) {
            return;
        }

        final var localDifficulty = DifficultyUtil.getLocalDifficulty(mob.level(), mob.getOnPos());
        setDifficultyAndAttributes(mob, localDifficulty);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityTick(EntityTickEvent.Post event) {
        // On the client, request mob data from the server
        // TODO: This is only really necessary for blights, as the level and difficulty are invisible without Jade...
        //  But the blight attachment must be on the client for the fire effect to render. Maybe this could be improved
        //  somehow?
        if (!event.getEntity().hasData(PsAttachmentTypes.LEVEL) && event.getEntity() instanceof Mob mob && mob.level().isClientSide) {
            if (PowerScale.detailedLogging()) {
                PowerScale.LOGGER.debug("Requesting missing data from {} {}", mob.getId(), mob);
            }
            PacketDistributor.sendToServer(new RequestMobDataPayload(mob));
        }
    }

    public static void setDifficultyAndAttributes(Mob mob, double difficulty) {
        final var mobDifficulty = DifficultyUtil.setDifficultyClamped(mob, difficulty);
        var level = (int) mobDifficulty + 1;
        mob.setData(PsAttachmentTypes.LEVEL, level);

        trySetBlight(mob, difficulty, level);

        if (PowerScale.detailedLogging()) {
            PowerScale.LOGGER.info("Setting {} to difficulty {} and level {}", mob.getName().getString(), mobDifficulty, level);
        }

        handleAttributeBoosts(mob, level);
    }

    private static void trySetBlight(Mob mob, double difficulty, int level) {
        if (mob.getType().is(PsTags.EntityTypes.BLIGHT_EXEMPT)) return;

        double chance = EntityGroups.from(mob).getBlightSpawnChance(mob, difficulty, level);
        if (mob.getRandom().nextDouble() < chance) {
            // Mob becomes a blight!
            if (PowerScale.detailedLogging()) {
                PowerScale.LOGGER.debug("Setting mob as blight: {}", mob);
            }
            mob.setData(PsAttachmentTypes.IS_BLIGHT, true);
            applySpecialBlightBonuses(mob, difficulty, level);
        }
    }

    private static void applySpecialBlightBonuses(Mob mob, double difficulty, int powerLevel) {
        if (!(mob.level() instanceof ServerLevel serverLevel)) return;

        // Supercharge creepers or strike with lightning
        if ((Config.COMMON.blightsStrikeWithLightning.get() && mob.getType().is(PsTags.EntityTypes.BLIGHTS_STRIKE_WITH_LIGHTNING))
                || (Config.COMMON.blightsSuperchargeCreepers.get() && mob instanceof Creeper)) {
            var currentHealth = mob.getHealth();
            mob.thunderHit(serverLevel, new LightningBolt(EntityType.LIGHTNING_BOLT, serverLevel));
            mob.setRemainingFireTicks(0);
            mob.setHealth(currentHealth);
        }
        // Giant mobs
        if (Config.COMMON.blightsMakeGiants.get() && mob.getType().is(PsTags.EntityTypes.BLIGHTS_MAKE_GIANT)) {
            var attributeInstance = mob.getAttribute(Attributes.SCALE);
            if (attributeInstance != null) {
                attributeInstance.addPermanentModifier(
                        new AttributeModifier(
                                PowerScale.getId("giant_blight"),
                                1.0,
                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        )
                );
            }
        }
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
        entity.setData(PsAttachmentTypes.IS_BLIGHT, data.isBlight());
    }
}
