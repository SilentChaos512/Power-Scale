package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.parser.Token;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class HorizontalDistanceFunction extends AbstractFunction {
    public abstract BlockPos getOrigin(Level level);

    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        var level = (Level) expression.getDataAccessor().getData("level").getValue();
        var pos = (BlockPos) expression.getDataAccessor().getData("pos").getValue();
        var origin = getOrigin(level);

        double dx = pos.getX() - origin.getX();
        double dz = pos.getZ() - origin.getZ();

        double result = Math.sqrt(dx * dx + dz * dz);
        return EvaluationValue.of(result, ExpressionConfiguration.defaultConfiguration());
    }

    public static final class FromOrigin extends HorizontalDistanceFunction {
        @Override
        public BlockPos getOrigin(Level level) {
            return BlockPos.ZERO;
        }
    }

    public static final class FromSpawn extends HorizontalDistanceFunction {
        @Override
        public BlockPos getOrigin(Level level) {
            return level.getSharedSpawnPos();
        }
    }
}
