package net.silentchaos512.powerscale.item;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.registries.DeferredItem;
import net.silentchaos512.powerscale.setup.PsItems;

public class FlaskItem extends Item {
    public FlaskItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (blockhitresult.getType() != HitResult.Type.MISS && blockhitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!level.mayInteract(player, blockpos)) {
                return InteractionResultHolder.pass(stack);
            }

            if (level.getFluidState(blockpos).is(FluidTags.WATER)) {
                level.playSound(
                        player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F
                );
                level.gameEvent(player, GameEvent.FLUID_PICKUP, blockpos);
                return InteractionResultHolder.sidedSuccess(
                        turnFlaskIntoItem(stack, player, PsItems.WATER_FLASK),
                        level.isClientSide()
                );
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    private ItemStack turnFlaskIntoItem(ItemStack flaskStack, Player player, DeferredItem<Item> filledItem) {
        player.awardStat(Stats.ITEM_USED.get(this));
        return ItemUtils.createFilledResult(flaskStack, player, filledItem.toStack());
    }
}
