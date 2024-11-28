package net.silentchaos512.powerscale.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootTable;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.PowerScale;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum EntityGroups implements Predicate<Entity> {
    NONE(
            e -> false,
            () -> 0.0,
            () -> 0.0
    ),
    PLAYER(
            EntityGroups::isPlayer,
            Config.COMMON.difficultyPlayerMin,
            Config.COMMON.difficultyPlayerMax
    ),
    BOSS(
            EntityGroups::isBoss,
            Config.COMMON.difficultyBossMin,
            Config.COMMON.difficultyBossMax
    ),
    HOSTILE(
            EntityGroups::isHostile,
            Config.COMMON.difficultyHostileMin,
            Config.COMMON.difficultyHostileMax
    ),
    PEACEFUL(
            EntityGroups::isPeacefulMob,
            Config.COMMON.difficultyPeacefulMin,
            Config.COMMON.difficultyPeacefulMax
    );

    private final Predicate<Entity> predicate;
    private final Supplier<Double> minDifficulty;
    private final Supplier<Double> maxDifficulty;
    private final ResourceKey<LootTable> lootTable;

    EntityGroups(
            Predicate<Entity> predicate,
            Supplier<Double> minDifficulty,
            Supplier<Double> maxDifficulty
    ) {
        this.predicate = predicate;
        this.minDifficulty = minDifficulty;
        this.maxDifficulty = maxDifficulty;
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
        return entity.isAlive() && !entity.canUsePortal(true);
    }

    private static boolean isHostile(Entity entity) {
        return entity instanceof Enemy;
    }

    private static boolean isPeacefulMob(Entity e) {
        return !(isPlayer(e) || isBoss(e) || isHostile(e));
    }
}
