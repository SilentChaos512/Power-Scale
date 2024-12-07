package net.silentchaos512.powerscale.evalex.function;

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.functions.AbstractFunction;
import com.ezylang.evalex.functions.FunctionParameter;
import com.ezylang.evalex.parser.Token;
import net.minecraft.world.level.Level;

@FunctionParameter(name = "phase1")
@FunctionParameter(name = "phase2")
@FunctionParameter(name = "phase3")
@FunctionParameter(name = "phase4")
@FunctionParameter(name = "phase5")
@FunctionParameter(name = "phase6")
@FunctionParameter(name = "phase7")
@FunctionParameter(name = "phase8")
public class LunarCycleFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... evaluationValues) throws EvaluationException {
        var level = (Level) expression.getDataAccessor().getData("level").getValue();
        int phase = level.getMoonPhase();
        return EvaluationValue.of(evaluationValues[phase], ExpressionConfiguration.defaultConfiguration());
    }
}
