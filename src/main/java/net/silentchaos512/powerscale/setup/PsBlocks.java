package net.silentchaos512.powerscale.setup;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetBlock;

public class PsBlocks {
    static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PowerScale.MOD_ID);

    public static final DeferredBlock<AlchemySetBlock> ALCHEMY_SET = BLOCKS.registerBlock(
            "alchemy_set",
            AlchemySetBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(5, 30)
    );
}
