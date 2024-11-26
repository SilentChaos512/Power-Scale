package net.silentchaos512.powerscale.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.silentchaos512.powerscale.component.AttributeMutator;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttributeHelper;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsDataComponents;
import net.silentchaos512.powerscale.setup.PsItems;
import net.silentchaos512.powerscale.setup.PsSounds;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AttributeMutatorItem extends Item {
    private final boolean quickUse;
    private final Supplier<AttributeMutator> defaultMutator;

    public AttributeMutatorItem(boolean quickUse, Supplier<AttributeMutator> defaultMutator, Properties properties) {
        super(properties);
        this.defaultMutator = defaultMutator;
        this.quickUse = quickUse;
    }

    @Nullable
    public AttributeMutator getAttributeMutator(ItemStack stack) {
        var data = stack.get(PsDataComponents.ATTRIBUTE_MUTATOR);
        if (data != null) {
            return data;
        }
        return defaultMutator.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        var mutator = getAttributeMutator(stack);
        if (mutator != null) {
            var attributeName = mutator.attribute().get().getName();
            var amountText = Component.literal((mutator.amount() > 0 ? "+" : "") + mutator.amount());
            tooltip.add(Component.translatable("item.powerscale.mutator.desc", attributeName, amountText));
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        if (this.quickUse) {
            return super.getUseAnimation(pStack);
        }
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack pStack, LivingEntity pEntity) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        var mutator = getAttributeMutator(stack);
        if (mutator == null) return stack;

        if (canChangeAttribute(entity, mutator)) {
            boostAttribute(entity, mutator);
            playEffects(level, entity);
            if (this.quickUse) {
                stack.consume(1, entity);
                return stack;
            }
            return new ItemStack(PsItems.FLASK.get());
        }

        return stack;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (getAttributeMutator(stack) == null) {
            return InteractionResultHolder.pass(stack);
        }
        if (this.quickUse) {
            return InteractionResultHolder.consume(finishUsingItem(stack, level, player));
        }
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    protected boolean canChangeAttribute(LivingEntity entity, AttributeMutator mutator) {
        // TODO
        return true;
    }

    protected void playEffects(Level level, LivingEntity target) {
        // FIXME: Does not work at all...
        level.playSound(null, target.getX(), target.getY(), target.getZ(), PsSounds.CRYSTAL_USED.get(), SoundSource.PLAYERS, 1f, 1f);
        // TODO: Particles
    }

    private static void boostAttribute(LivingEntity entity, AttributeMutator mutator) {
        Map<DataHolder<ScalingAttribute>, Double> immutableMap = entity.getData(PsAttachmentTypes.BOOSTED_ATTRIBUTES);
        var copiedMap = new HashMap<>(immutableMap);
        var currentBoost = copiedMap.getOrDefault(mutator.attribute(), 0.0);
        var newBoost = currentBoost + mutator.amount();
        copiedMap.put(mutator.attribute(), newBoost);
        entity.setData(PsAttachmentTypes.BOOSTED_ATTRIBUTES, copiedMap);
        ScalingAttributeHelper.applyBoostedAttributes(entity);
    }
}
