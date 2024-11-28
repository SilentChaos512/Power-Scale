package net.silentchaos512.powerscale.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.silentchaos512.powerscale.core.EntityGroups;
import net.silentchaos512.powerscale.loot.condition.MobProperties;
import net.silentchaos512.powerscale.setup.PsItems;

import java.util.Optional;
import java.util.function.BiConsumer;

public class EntityLootTables implements LootTableSubProvider {
    private final HolderLookup.Provider registries;

    public EntityLootTables(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(
                EntityGroups.HOSTILE.getLootTable(),
                LootTable.lootTable()
                        .withPool(
                                // Always with random chance
                                LootPool.lootPool()
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.06f, 0.005f))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .add(
                                                LootItem.lootTableItem(PsItems.HEART_CRYSTAL)
                                                        .setWeight(12)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.POWER_CRYSTAL)
                                                        .setWeight(4)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.ARCHER_CRYSTAL)
                                                        .setWeight(3)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.WING_CRYSTAL)
                                                        .setWeight(1)
                                        )
                        )
                        .withPool(
                                // High difficulty level with random chance
                                LootPool.lootPool()
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.06f, 0.005f))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(() -> new MobProperties(
                                                Optional.of(IntRange.lowerBound(70)),
                                                Optional.empty(),
                                                Optional.empty()
                                        ))
                                        .add(
                                                LootItem.lootTableItem(PsItems.ENCHANTED_HEART)
                                                        .setWeight(3)
                                        )
                        )
                        .withPool(
                                // Blights only
                                LootPool.lootPool()
                                        .setRolls(UniformGenerator.between(2, 4))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .when(() -> new MobProperties(
                                                Optional.empty(),
                                                Optional.empty(),
                                                Optional.of(IntRange.lowerBound(1))
                                        ))
                                        .add(
                                                LootItem.lootTableItem(PsItems.HEART_CRYSTAL)
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.POWER_CRYSTAL)
                                                        .setWeight(5)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.ARCHER_CRYSTAL)
                                                        .setWeight(4)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.WING_CRYSTAL)
                                                        .setWeight(2)
                                        )
                                        .add(
                                                EmptyLootItem.emptyItem()
                                                        .setWeight(3)
                                        )
                        )
        );
        output.accept(
                EntityGroups.PEACEFUL.getLootTable(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(registries, 0.06f, 0.005f))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .add(
                                                LootItem.lootTableItem(PsItems.CURSED_HEART)
                                        )
                        )
        );
        output.accept(
                EntityGroups.BOSS.getLootTable(),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(UniformGenerator.between(4, 6))
                                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                                        .add(
                                                LootItem.lootTableItem(PsItems.HEART_CRYSTAL)
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.POWER_CRYSTAL)
                                                        .setWeight(5)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.ARCHER_CRYSTAL)
                                                        .setWeight(4)
                                        )
                                        .add(
                                                LootItem.lootTableItem(PsItems.WING_CRYSTAL)
                                                        .setWeight(2)
                                        )
                        )
        );
    }
}
