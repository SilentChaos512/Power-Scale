package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "unscaled_value")
@FunctionParameter(name = "reduction_ratio")
@FunctionParameter(name = "high_value")
public class ReducedScalingFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        double unscaledValue = evaluationValues[0].getNumberValue().doubleValue();
        double reductionRatio = evaluationValues[1].getNumberValue().doubleValue();
        double highValue = evaluationValues[2].getNumberValue().doubleValue();
        double baseValue = expression.getDataAccessor().getData("base_value").getNumberValue().doubleValue();

        var scale = reductionRatio * Math.max(0, baseValue - highValue);
        return EvaluationValue.of(unscaledValue * baseValue / (highValue + scale), ExpressionConfiguration.defaultConfiguration());
    }
}
