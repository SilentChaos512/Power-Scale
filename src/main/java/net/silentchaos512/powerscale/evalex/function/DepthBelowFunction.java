package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import net.minecraft.core.BlockPos;
import net.silentchaos512.powerscale.core.DifficultyUtil;

@FunctionParameter(name = "surface_level")
public class DepthBelowFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        var pos = (BlockPos) expression.getDataAccessor().getData("pos").getValue();
        int surfaceLevel = evaluationValues[0].getNumberValue().intValue();

        int result = DifficultyUtil.depthBelowSurface(surfaceLevel, pos);
        return EvaluationValue.of(result, ExpressionConfiguration.defaultConfiguration());
    }
}
