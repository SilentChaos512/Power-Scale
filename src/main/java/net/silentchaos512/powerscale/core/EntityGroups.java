package net.silentchaos512.powerscale.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.Tags;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.config.ConfiguredExpression;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum EntityGroups implements Predicate<Entity> {
    NONE(
            e -> false,
            () -> 0.0,
            () -> 0.0,
            null
    ),
    PLAYER(
            EntityGroups::isPlayer,
            Config.COMMON.difficultyPlayerMin,
            Config.COMMON.difficultyPlayerMax,
            null
    ),
    BOSS(
            EntityGroups::isBoss,
            Config.COMMON.difficultyBossMin,
            Config.COMMON.difficultyBossMax,
            Config.COMMON.blightSpawnChanceBoss
    ),
    HOSTILE(
            EntityGroups::isHostile,
            Config.COMMON.difficultyHostileMin,
            Config.COMMON.difficultyHostileMax,
            Config.COMMON.blightSpawnChanceHostile
    ),
    PEACEFUL(
            EntityGroups::isPeacefulMob,
            Config.COMMON.difficultyPeacefulMin,
            Config.COMMON.difficultyPeacefulMax,
            Config.COMMON.blightSpawnChancePeaceful
    );

    private final Predicate<Entity> predicate;
    private final Supplier<Double> minDifficulty;
    private final Supplier<Double> maxDifficulty;
    @Nullable
    private final ConfiguredExpression blightSpawnChance;
    private final ResourceKey<LootTable> lootTable;

    EntityGroups(
            Predicate<Entity> predicate,
            Supplier<Double> minDifficulty,
            Supplier<Double> maxDifficulty,
            @Nullable ConfiguredExpression blightSpawnChance
    ) {
        this.predicate = predicate;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
        this.blightSpawnChance = blightSpawnChance;
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, PowerScale.getId("bonus_drops/" + this.getName()));
    }

    public static EntityGroups from(Entity entity) {
        for (EntityGroups group : values()) {
            if (group.test(entity)) {
                return group;
            }
        }
        // This should never happen
        return NONE;
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public double minDifficulty() {
        return this.minDifficulty.get();
    }

    public double maxDifficulty() {
        return this.maxDifficulty.get();
    }

    public double getBlightSpawnChance(Entity entity, double difficulty, double level) {
        if (this.blightSpawnChance == null) return 0.0;

        return this.blightSpawnChance
                .with("difficulty", difficulty)
                .with("level", level)
                .evaluateDouble(0.0, null);
    }

    public ResourceKey<LootTable> getLootTable() {
        return lootTable;
    }

    @Override
    public boolean test(Entity entity) {
        return this.predicate.test(entity);
    }

    private static boolean isPlayer(Entity entity) {
        return entity instanceof Player;
    }

    private static boolean isBoss(Entity entity) {
        return entity.getType().is(Tags.EntityTypes.BOSSES);
    }

    private static boolean isHostile(Entity entity) {
        return entity instanceof Enemy;
    }

    private static boolean isPeacefulMob(Entity e) {
        return !(isPlayer(e) || isBoss(e) || isHostile(e));
    }
}
