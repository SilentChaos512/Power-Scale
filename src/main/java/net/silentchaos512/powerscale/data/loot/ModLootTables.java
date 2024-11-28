package net.silentchaos512.powerscale.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.VanillaLootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModLootTables extends LootTableProvider {
    public ModLootTables(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(
                pOutput,
                Collections.emptySet(),
                VanillaLootTableProvider.create(pOutput, pRegistries).getTables(),
                pRegistries
        );
    }

    @Override
    public List<SubProviderEntry> getTables() {
        return List.of(
                new SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY)
        );
    }

    @Override
    protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
        // Don't validate against built-in tables
    }
}
