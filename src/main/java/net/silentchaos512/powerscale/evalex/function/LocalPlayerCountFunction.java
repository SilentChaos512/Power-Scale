package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.silentchaos512.powerscale.core.DifficultyUtil;

@FunctionParameter(name = "radius")
public class LocalPlayerCountFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        var level = (Level) expression.getDataAccessor().getData("level").getValue();
        var pos = (BlockPos) expression.getDataAccessor().getData("pos").getValue();
        int radius = evaluationValues[0].getNumberValue().intValue();

        int result = DifficultyUtil.localPlayerCount(level, pos, radius);
        return EvaluationValue.of(result, ExpressionConfiguration.defaultConfiguration());
    }
}
