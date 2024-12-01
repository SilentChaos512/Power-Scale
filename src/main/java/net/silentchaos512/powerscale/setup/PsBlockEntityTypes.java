package net.silentchaos512.powerscale.setup;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetBlockEntity;

public class PsBlockEntityTypes {
    static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PowerScale.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AlchemySetBlockEntity>> ALCHEMY_SET = BLOCK_ENTITY_TYPES.register(
            "alchemy_set",
            () -> BlockEntityType.Builder.of(
                    AlchemySetBlockEntity::new,
                    PsBlocks.ALCHEMY_SET.get()
            ).build(null)
    );
}
