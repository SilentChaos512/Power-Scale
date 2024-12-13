package net.silentchaos512.powerscale.setup;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.block.AlchemySetBlock;

import java.util.stream.Collectors;

public class PsBlocks {
    static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PowerScale.MOD_ID);

    public static final DeferredBlock<AlchemySetBlock> ALCHEMY_SET = BLOCKS.registerBlock(
            "alchemy_set",
            AlchemySetBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .requiresCorrectToolForDrops()
                    .strength(0.5f)
                    .noOcclusion()
    );

    public static Iterable<Block> getBlocks() {
        return BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .collect(Collectors.toSet());
    }
}
