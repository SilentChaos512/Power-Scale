package net.silentchaos512.powerscale.core;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.silentchaos512.lib.util.MathUtils;
import net.silentchaos512.powerscale.Config;
import net.silentchaos512.powerscale.config.ConfiguredExpression;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

public class DifficultyUtil {
    public static double getLocalDifficulty(Level level, Vec3 pos) {
        return getLocalDifficulty(level, new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
    }

    public static double getLocalDifficulty(Level level, BlockPos pos) {
        if (Config.COMMON.localDifficultyUseOverride.get()) {
            return getLocalDifficultyFromOverride(level, pos);
        } else {
            return getLocalDifficultyFromParts(level, pos);
        }
    }

    private static double getLocalDifficultyFromParts(Level level, BlockPos pos) {
        double fromPlayers = evaluate(Config.COMMON.localDifficultyFromPlayers, level, pos);
        double fromLocation = evaluate(Config.COMMON.localDifficultyFromLocation, level, pos);
        double fromMoonPhase = evaluate(Config.COMMON.localDifficultyLunarMultipliers, level, pos);
        double groupBonus = evaluate(Config.COMMON.localDifficultyGroupBonus, level, pos);

        double value = (fromPlayers + fromLocation) * fromMoonPhase * groupBonus;
        return MathUtils.clamp(value, Config.COMMON.difficultyLocalMin.get(), Config.COMMON.difficultyLocalMax.get());
    }

    private static double getLocalDifficultyFromOverride(Level level, BlockPos pos) {
        double value = evaluate(Config.COMMON.localDifficultyOverride, level, pos);
        return MathUtils.clamp(value, Config.COMMON.difficultyLocalMin.get(), Config.COMMON.difficultyLocalMax.get());
    }

    private static double evaluate(ConfiguredExpression expression, Level level, BlockPos pos) {
        return expression.with("level", level).with("pos", pos).evaluateDouble(0.0, null);
    }

    public static double getDifficulty(Entity entity) {
        return entity.getData(PsAttachmentTypes.DIFFICULTY);
    }

    static void setDifficulty(Entity entity, double value) {
        entity.setData(PsAttachmentTypes.DIFFICULTY, value);
    }

    public static double setDifficultyClamped(Entity entity, double value) {
        var entityGroup = EntityGroups.from(entity);
        double clampedValue = MathUtils.clamp(value, entityGroup.minDifficulty(), entityGroup.maxDifficulty());
        setDifficulty(entity, clampedValue);
        return clampedValue;
    }

    public static void resetDifficulty(Entity entity) {
        if (entity instanceof Player) {
            setDifficulty(entity, Config.COMMON.difficultyPlayerInitial.get());
        } else if (entity instanceof Mob mob) {
            var localDifficulty = getLocalDifficulty(mob.level(), mob.getOnPos());
            MobDifficulty.setDifficultyAndAttributes(mob, localDifficulty);
        }
    }

    public static double horizontalDistanceSqr(BlockPos center, Entity entity) {
        var dx = center.getX() + 0.5 - entity.getX();
        var dz = center.getZ() + 0.5 - entity.getZ();
        return dx * dx + dz * dz;
    }

    public static int depthBelowSurface(int surfaceLevel, BlockPos pos) {
        var y = pos.getY();
        if (y > surfaceLevel) {
            return 0;
        }
        return surfaceLevel - y;
    }

    public static int localPlayerCount(Level level, BlockPos pos, int radius) {
        var radiusSquared = radius * radius;
        int total = 0;
        for (Player player : level.players()) {
            if (horizontalDistanceSqr(pos, player) <= radiusSquared) {
                ++total;
            }
        }
        return total;
    }
}
