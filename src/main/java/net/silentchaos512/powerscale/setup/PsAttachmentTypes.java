package net.silentchaos512.powerscale.setup;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;

import java.util.function.Supplier;

public class PsAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, PowerScale.MOD_ID);

    // attach to players, accumulate over time, maybe lose on death
    public static final Supplier<AttachmentType<Double>> DIFFICULTY = ATTACHMENT_TYPES.register(
            "difficulty", () -> AttachmentType.builder(Config.COMMON.difficultyPlayerInitial)
                    .serialize(Codec.DOUBLE)
                    .copyOnDeath()
                    .copyHandler((attachment, holder, provider) -> {
                        PowerScale.LOGGER.info("!copy difficulty");
                        return attachment;
                    })
                    .build()
    );

    // assign to players, calculated base on the sp value
    // maybe could assign to mobs? but difficulty is probably enough
    public static final Supplier<AttachmentType<Integer>> LEVEL = ATTACHMENT_TYPES.register(
            "level", () -> AttachmentType.builder(() -> 1)
                    .serialize(ExtraCodecs.POSITIVE_INT)
                    .copyOnDeath()
                    .copyHandler((attachment, holder, provider) -> {
                        PowerScale.LOGGER.info("!copy level");
                        return attachment;
                    })
                    .build()
    );

    // players accumulate sp by playing the game (mining, fighting, etc.)
    public static final Supplier<AttachmentType<Integer>> SP = ATTACHMENT_TYPES.register(
            "sp", () -> AttachmentType.builder(() -> 0)
                    .serialize(Codec.INT)
                    .copyOnDeath()
                    .copyHandler((attachment, holder, provider) -> {
                        PowerScale.LOGGER.info("!copy sp");
                        return attachment;
                    })
                    .build()
    );
}
