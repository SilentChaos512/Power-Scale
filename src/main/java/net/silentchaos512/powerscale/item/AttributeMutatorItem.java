package net.silentchaos512.powerscale.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
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
import java.util.Map;
import java.util.function.Consumer;
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

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        var mutator = getAttributeMutator(stack);
        if (mutator != null) {
            var attributeName = ScalingAttribute.getNameSafely(mutator.attribute());
            var amountText = Component.literal((mutator.amount() > 0 ? "+" : "") + mutator.amount());
            tooltipAdder.accept(Component.translatable("item.powerscale.mutator.desc", attributeName, amountText));
        }
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack pStack) {
        if (this.quickUse) {
            return super.getUseAnimation(pStack);
        }
        return ItemUseAnimation.DRINK;
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
            boostAttribute(level, entity, mutator);
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
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        var stack = player.getItemInHand(usedHand);
        if (getAttributeMutator(stack) == null) {
            return InteractionResult.PASS;
        }
        if (this.quickUse) {
            finishUsingItem(stack, level, player);
            return InteractionResult.CONSUME;
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

    private static void boostAttribute(Level level, LivingEntity entity, AttributeMutator mutator) {
        Map<DataHolder<ScalingAttribute>, Double> immutableMap = entity.getData(PsAttachmentTypes.BOOSTED_ATTRIBUTES);
        var copiedMap = new HashMap<>(immutableMap);
        double currentBoost = copiedMap.getOrDefault(mutator.attribute(), 0.0);
        double newBoost = currentBoost + mutator.amount();
        copiedMap.put(mutator.attribute(), newBoost);
        entity.setData(PsAttachmentTypes.BOOSTED_ATTRIBUTES, copiedMap);
        ScalingAttributeHelper.applyBoostedAttributes(entity);
        if (!level.isClientSide()) {
            ScalingAttributeHelper.notifyOfAttributeChange(entity, mutator.attribute(), currentBoost, newBoost);
        }
    }
}
