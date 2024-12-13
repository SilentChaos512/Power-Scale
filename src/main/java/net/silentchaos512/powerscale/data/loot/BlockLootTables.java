package net.silentchaos512.powerscale.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.silentchaos512.powerscale.setup.PsBlocks;

import java.util.Collections;

public class BlockLootTables extends BlockLootSubProvider {
    protected BlockLootTables(HolderLookup.Provider pRegistries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), pRegistries);
    }

    @Override
    protected void generate() {
        dropSelf(PsBlocks.ALCHEMY_SET.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return PsBlocks.getBlocks();
    }
}
