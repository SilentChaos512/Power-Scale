package net.silentchaos512.powerscale.client;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsTags;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

@EventBusSubscriber(value = Dist.CLIENT)
public class PowerLevelDisplayHandler {
    public enum ConfigType {
        NEVER,
        USING_ITEM,
        HOLDING_ITEM,
        ALWAYS
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        var player = Minecraft.getInstance().player;
        if (player == null || !canDetectPowerLevels(player)) return;

        var viewEntity = Minecraft.getInstance().getCameraEntity();
        if (viewEntity == null) return;

        var lookedAtEntity = rayTrace(viewEntity, 100);
        if (lookedAtEntity == null) return;

        var powerLevel = lookedAtEntity.getData(PsAttachmentTypes.LEVEL);
        if (powerLevel < 1) return;

        var text = Component.translatable("powerscale.name_and_level", lookedAtEntity.getDisplayName(), powerLevel);
        player.displayClientMessage(text.withStyle(ChatFormatting.GOLD), true);
    }

    private static boolean canDetectPowerLevels(LocalPlayer player) {
        var config = Config.SERVER.powerLevelDisplayRestriction.get();
        if (config == ConfigType.NEVER) return false;
        if (config == ConfigType.ALWAYS) return true;

        var usingRequired = config == ConfigType.USING_ITEM;
        return canDetectPowerLevelsWithHeldItem(player, InteractionHand.MAIN_HAND, usingRequired)
                || canDetectPowerLevelsWithHeldItem(player, InteractionHand.OFF_HAND, usingRequired);
    }

    private static boolean canDetectPowerLevelsWithHeldItem(LocalPlayer player, InteractionHand hand, boolean usingRequired) {
        return isPowerLevelDetector(player.getItemInHand(hand)) && (!usingRequired || isUsingItem(player, hand));
    }

    private static boolean isPowerLevelDetector(ItemStack stack) {
        return stack.is(PsTags.Items.POWER_LEVEL_DETECTORS);
    }

    private static boolean isUsingItem(LocalPlayer player, InteractionHand hand) {
        return player.isUsingItem() && player.getUsedItemHand() == hand;
    }

    private static boolean canBeTarget(Entity target, Entity viewEntity) {
        if (target.isRemoved()) return false;
        if (target.isSpectator()) return false;
        if (target == viewEntity.getVehicle()) return false;
        if (viewEntity instanceof Player player) {
            if (target.isInvisibleTo(player)) return false;
        } else {
            if (target.isInvisible()) return false;
        }

        return target instanceof Mob;
    }

    @Nullable
    public static Entity rayTrace(Entity entity, double reach) {
        var mc = Minecraft.getInstance();
        float partialTick = mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
        Vec3 eyePosition = entity.getEyePosition(partialTick);

        Vec3 traceEnd;
        Vec3 lookVector;
        if (mc.hitResult == null) {
            lookVector = entity.getViewVector(partialTick);
            traceEnd = eyePosition.add(lookVector.scale(reach));
        } else {
            traceEnd = mc.hitResult.getLocation().subtract(eyePosition);
            lookVector = entity.getViewVector(partialTick);
            // when it comes to a block hit, we only need to find entities that closer than the block
            if (mc.hitResult.getType() == HitResult.Type.BLOCK && traceEnd.lengthSqr() < reach * reach) {
                traceEnd = eyePosition.add(lookVector.scale(traceEnd.length() + 1e-5));
            } else {
                traceEnd = eyePosition.add(lookVector.scale(reach));
            }
        }

        Level world = entity.level();
        AABB bound = new AABB(eyePosition, traceEnd);
        Predicate<Entity> predicate = e -> canBeTarget(e, entity);
        EntityHitResult entityResult = getEntityHitResult(world, entity, eyePosition, traceEnd, bound, predicate);

        return entityResult != null ? entityResult.getEntity() : null;

    }

    @Nullable
    public static EntityHitResult getEntityHitResult(
            Level worldIn,
            Entity projectile,
            Vec3 startVec,
            Vec3 endVec,
            AABB boundingBox,
            Predicate<Entity> filter
    ) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;

        for (Entity entity1 : worldIn.getEntities(projectile, boundingBox, filter)) {
            AABB axisalignedbb = entity1.getBoundingBox();
            if (axisalignedbb.getSize() < 0.3) {
                axisalignedbb = axisalignedbb.inflate(0.3);
            }
            if (axisalignedbb.contains(startVec)) {
                entity = entity1;
                break;
            }
            Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
            if (optional.isPresent()) {
                double d1 = startVec.distanceToSqr(optional.get());
                if (d1 < d0) {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity);
    }
}
