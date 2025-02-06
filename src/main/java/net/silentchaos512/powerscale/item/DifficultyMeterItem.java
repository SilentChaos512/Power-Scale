package net.silentchaos512.powerscale.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.silentchaos512.lib.util.EnumUtils;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.client.ClientData;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import net.silentchaos512.powerscale.setup.PsDataComponents;
import net.silentchaos512.powerscale.setup.PsItems;

import javax.annotation.Nullable;
import java.util.List;

public class DifficultyMeterItem extends Item {
    public DifficultyMeterItem(Properties pProperties) {
        super(pProperties);
    }

    public static float getDifficultyScaleForModel(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        if (entity != null) {
            if (stack.get(PsDataComponents.DIFFICULTY_METER_MODE) == Mode.PLAYER) {
                double maxDifficulty = Config.COMMON.difficultyPlayerMax.get();
                double difficulty = MathUtils.clamp(ClientData.get().playerDifficulty(), 0.0, maxDifficulty);
                return (float) (difficulty / maxDifficulty);
            } else {
                double maxDifficulty = Config.COMMON.difficultyLocalMax.get();
                double difficulty = MathUtils.clamp(ClientData.get().localDifficulty(), 0.0, maxDifficulty);
                return (float) (difficulty / maxDifficulty);
            }
        }
        return 0f;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        // TODO: display current mode
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        Mode currentMode = stack.getOrDefault(PsDataComponents.DIFFICULTY_METER_MODE, Mode.LOCAL);
        Mode nextMode = currentMode.cycleNext();
        stack.set(PsDataComponents.DIFFICULTY_METER_MODE, nextMode);
        player.displayClientMessage(Component.translatable("powerscale.mode", nextMode.name()), true);
        return InteractionResultHolder.consume(stack);
    }

    public enum Mode {
        LOCAL,
        PLAYER;

        public static final Codec<Mode> CODEC = Codec.STRING.comapFlatMap(
                str -> {
                    for (Mode mode : values()) {
                        if (mode.name().equalsIgnoreCase(str)) {
                            return DataResult.success(mode);
                        }
                    }
                    return DataResult.error(() -> "Unknown difficulty meter mode: " + str);
                },
                Enum::name
        );

        public static final StreamCodec<FriendlyByteBuf, Mode> STREAM_CODEC = StreamCodec.of(
                (buf, val) -> buf.writeByte(val.ordinal()),
                buf -> {
                    var index = buf.readByte();
                    return values()[MathUtils.clamp(index, 0, values().length - 1)];
                }
        );

        public Mode cycleNext() {
            var nextIndex = ordinal() + 1;
            if (nextIndex >= values().length) {
                nextIndex = 0;
            }
            return values()[nextIndex];
        }
    }
}
