package net.silentchaos512.powerscale.evalex;

import com.ezylang.evalex.BaseException;
import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.silentchaos512.powerscale.PowerScale;
import net.silentchaos512.powerscale.config.ConfiguredExpression;
import net.silentchaos512.powerscale.config.ConfiguredExpressionData;
import net.silentchaos512.powerscale.setup.PsAttachmentTypes;

import javax.annotation.Nullable;

public interface ExpressionExtension<T extends ExpressionExtension<T>> {
    Codec<ConfiguredExpression> CONFIGURED_EXPRESSION_CODEC = Codec.STRING
            .comapFlatMap(
                    str -> {
                        if (!str.startsWith("$")) return DataResult.error(() -> "Missing leading \"$\" on ConfiguredExpression config path");
                        var configuredExpression = ConfiguredExpression.getFromConfigPath(str.replaceFirst("\\$", ""));
                        return configuredExpression != null
                                ? DataResult.success(configuredExpression)
                                : DataResult.error(() -> "Unknown expression config: " + str);
                    },
                    exp -> "$" + exp.getDescription()
            );
    Codec<ExpressionExtension<?>> CODEC = Codec.either(CONFIGURED_EXPRESSION_CODEC, ExpressionWrapper.CODEC)
            .xmap(
                    either -> either.map(configuredExpression -> configuredExpression, expressionWrapper -> expressionWrapper),
                    exp -> {
                        if (exp instanceof ConfiguredExpression) {
                            return Either.left((ConfiguredExpression) exp);
                        }
                        return Either.right((ExpressionWrapper) exp);
                    }
            );

    StreamCodec<FriendlyByteBuf, ExpressionExtension<?>> STREAM_CODEC = StreamCodec.of(
            (buf, exp) -> {
                if (exp instanceof ConfiguredExpression) {
                    buf.writeUtf("$" + exp.getDescription());
                }
                buf.writeUtf(exp.expression().getExpressionString());
            },
            buf -> {
                var str = buf.readUtf();
                if (str.startsWith("$")) {
                    var configuredExpression = ConfiguredExpression.getFromConfigPath(str.replaceFirst("\\$", ""));
                    if (configuredExpression == null) {
                        throw new IllegalStateException("Unknown expression config: " + str);
                    }
                    return configuredExpression;
                }
                return new ExpressionWrapper(new Expression(str));
            }
    );

    String getDescription();

    Expression expression();

    default EvaluationValue evaluate() throws EvaluationException, ParseException {
        checkExpressionNotNull();
        return expression().evaluate();
    }

    default int evaluateInt(int fallback, @Nullable CommandSource player) {
        try {
            return evaluate().getNumberValue().intValue();
        } catch (EvaluationException | ParseException e) {
            logExpressionError(player, e);
            return fallback;
        }
    }

    default double evaluateDouble(double fallback, @Nullable CommandSource player) {
        try {
            return evaluate().getNumberValue().doubleValue();
        } catch (EvaluationException | ParseException e) {
            logExpressionError(player, e);
            return fallback;
        }
    }

    default void checkExpressionNotNull() {}

    default void logExpressionError(@Nullable CommandSource player, BaseException e) {
        var message = "Error while evaluating expression \"" + getDescription() + "\" (" + expression().getExpressionString() + "). Check your log file.";
        if (player != null) {
            player.sendSystemMessage(Component.literal(message).withStyle(ChatFormatting.RED));
        } else {
            PowerScale.LOGGER.error(message);
        }
        PowerScale.LOGGER.catching(e);
    }

    default T with(String variable, Object value) {
        expression().with(variable, value);
        //noinspection unchecked
        return (T) this;
    }

    default T with(ConfiguredExpressionData data) {
        data.setVariables(expression());
        //noinspection unchecked
        return (T) this;
    }

    default T withPlayer(Player player) {
        return with("difficulty", player.getData(PsAttachmentTypes.DIFFICULTY))
                .with("level", player.getData(PsAttachmentTypes.LEVEL))
                .with("sp", player.getData(PsAttachmentTypes.SP))
                .with("player", player);
    }
}
