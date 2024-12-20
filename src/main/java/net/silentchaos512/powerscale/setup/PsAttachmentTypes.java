package net.silentchaos512.powerscale.setup;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.core.resources.DataHolder;
import net.silentchaos512.powerscale.core.resources.ScalingAttributeManager;
import net.silentchaos512.powerscale.core.scalingattribute.ScalingAttribute;

import java.util.Map;
import java.util.function.Supplier;

public class PsAttachmentTypes {
    static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PowerScale.MOD_ID);

    // attach to players, accumulate over time, maybe lose on death
    public static final Supplier<AttachmentType<Double>> DIFFICULTY = ATTACHMENT_TYPES.register(
            "difficulty", () -> AttachmentType.builder(Config.COMMON.difficultyPlayerInitial)
                    .serialize(Codec.DOUBLE)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<Integer>> BLIGHT_TIER = ATTACHMENT_TYPES.register(
            "blight_tier", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .build()
    );

    // assign to players, calculated base on the sp value
    // assign to mobs, calculated based on difficulty
    public static final Supplier<AttachmentType<Integer>> LEVEL = ATTACHMENT_TYPES.register(
            "level", () -> AttachmentType.builder(() -> 1)
                    .serialize(ExtraCodecs.POSITIVE_INT)
                    .copyOnDeath()
                    .build()
    );

    // players accumulate sp by playing the game (mining, fighting, etc.)
    public static final Supplier<AttachmentType<Integer>> SP = ATTACHMENT_TYPES.register(
            "sp", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<Map<DataHolder<ScalingAttribute>, Double>>> BOOSTED_ATTRIBUTES = ATTACHMENT_TYPES.register(
            "boosted_attributes", () -> AttachmentType.<Map<DataHolder<ScalingAttribute>, Double>>builder(ImmutableMap::of)
                    .serialize(
                            Codec.unboundedMap(
                                    ScalingAttributeManager.HOLDER_CODEC,
                                    Codec.DOUBLE
                            )
                    )
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<BlockPos>> LAST_POS = ATTACHMENT_TYPES.register(
            "last_pos", () -> AttachmentType.builder(() -> BlockPos.ZERO)
                    .serialize(BlockPos.CODEC)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<Integer>> IDLE_TIME = ATTACHMENT_TYPES.register(
            "idle_time", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .build()
    );

    public static final Supplier<AttachmentType<Boolean>> IDLE = ATTACHMENT_TYPES.register(
            "idle", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL)
                    .copyOnDeath()
                    .build()
    );
}
