package net.silentchaos512.powerscale.setup;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.component.AttributeMutator;

import java.util.function.Supplier;

public class PsDataComponents {
    static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(PowerScale.MOD_ID);

    public static final Supplier<DataComponentType<AttributeMutator>> ATTRIBUTE_MUTATOR = DATA_COMPONENTS.registerComponentType(
            "attribute_mutator",
            builder -> builder
                    .persistent(AttributeMutator.CODEC)
                    .networkSynchronized(AttributeMutator.STREAM_CODEC)
    );

    public static final Supplier<DataComponentType<Double>> DIFFICULTY_MUTATOR = DATA_COMPONENTS.registerComponentType(
            "difficulty_mutator",
            builder -> builder
                    .persistent(Codec.DOUBLE)
                    .networkSynchronized(ByteBufCodecs.DOUBLE)
    );
}
