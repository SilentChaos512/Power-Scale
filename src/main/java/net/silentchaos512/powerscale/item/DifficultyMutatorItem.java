package net.silentchaos512.powerscale.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;
import net.silentchaos512.powerscale.setup.PsDataComponents;
import net.silentchaos512.powerscale.setup.PsItems;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DifficultyMutatorItem extends Item {
    private final boolean quickUse;
    private final Supplier<Optional<Double>> defaultMutator;

    public DifficultyMutatorItem(boolean quickUse, Supplier<Optional<Double>> defaultMutator, Properties pProperties) {
        super(pProperties);
        this.quickUse = quickUse;
        this.defaultMutator = defaultMutator;
    }

    public Optional<Double> getDifficultyMutator(ItemStack stack) {
        var data = stack.get(PsDataComponents.DIFFICULTY_MUTATOR);
        if (data != null) {
            return Optional.of(data);
        }
        return defaultMutator.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        var mutator = getDifficultyMutator(stack);
        if (mutator.isPresent()) {
            double amount = mutator.get();
            var amountText = Component.literal((amount > 0.0 ? "+" : "") + amount);
            tooltip.add(Component.translatable("powerscale.difficulty", amountText));
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
        var mutator = getDifficultyMutator(stack);
        if (mutator.isEmpty()) return stack;

        double amount = mutator.get();
        if (canChangeDifficulty(entity, amount)) {
            changeDifficulty(entity, amount);
            playEffects(entity, amount);
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
        if (getDifficultyMutator(stack).isEmpty()) {
            return InteractionResultHolder.pass(stack);
        }
        if (this.quickUse) {
            return InteractionResultHolder.consume(finishUsingItem(stack, level, player));
        }
        return ItemUtils.startUsingInstantly(level, player, usedHand);
    }

    private boolean canChangeDifficulty(LivingEntity entity, double amount) {
        var current = entity.getData(PsAttachmentTypes.DIFFICULTY);
        if (current <= 0.0 && amount < 0.0 || current >= Config.COMMON.difficultyPlayerMax.get() && amount > 0.0) {
            return false;
        }
        return true;
    }

    private void playEffects(LivingEntity entity, double amount) {
        // TODO
    }

    private void changeDifficulty(LivingEntity entity, double amount) {
        var current = entity.getData(PsAttachmentTypes.DIFFICULTY);
        var newAmount = current + amount;
        var clampedAmount = MathUtils.clamp(
                newAmount,
                Config.COMMON.difficultyPlayerMin.get(),
                Config.COMMON.difficultyPlayerMax.get()
        );
        entity.setData(PsAttachmentTypes.DIFFICULTY, clampedAmount);
    }
}
