package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

@FunctionParameter(name = "radius")
public class WeightedPlayerDifficultyFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... evaluationValues) throws EvaluationException {
        var level = (Level) expression.getDataAccessor().getData("level").getValue();
        var pos = (BlockPos) expression.getDataAccessor().getData("pos").getValue();
        int radius = evaluationValues[0].getNumberValue().intValue();
        int radiusSquared = radius * radius;

        var totalDifficulty = 0.0;
        var totalWeight = 0.0;

        for (Player player : level.players()) {
            var horizontalDistanceSqr = DifficultyUtil.horizontalDistanceSqr(pos, player);
            if (horizontalDistanceSqr <= radiusSquared) {
                double weight = 1.0 - horizontalDistanceSqr / radiusSquared;
                totalDifficulty += weight * player.getData(PsAttachmentTypes.DIFFICULTY);
                totalWeight += weight;
            }
        }

        var result = totalWeight <= 0 ? 0.0 : totalDifficulty / totalWeight;
        return EvaluationValue.of(result, ExpressionConfiguration.defaultConfiguration());
    }
}
