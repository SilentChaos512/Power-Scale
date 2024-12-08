package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import net.minecraft.world.entity.player.Player;
import net.silentchaos512.powerscale.core.IdleTracker;

@FunctionParameter(name = "multiplier")
public class IdleMultiplierFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        var player = (Player) expression.getDataAccessor().getData("player").getValue();
        var multiplier = evaluationValues[0].getNumberValue().doubleValue();
        double result = IdleTracker.isIdle(player) ? multiplier : 1.0;
        return EvaluationValue.of(result, ExpressionConfiguration.defaultConfiguration());
    }
}
