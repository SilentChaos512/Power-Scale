package net.silentchaos512.powerscale.evalex;

import com.ezylang.evalex.Expression;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record ExpressionWrapper(Expression expression) implements ExpressionExtension<ExpressionWrapper> {
    public static final Codec<ExpressionWrapper> CODEC = Codec.STRING
            .xmap(
                    str -> new ExpressionWrapper(new Expression(str)),
                    wrapper -> wrapper.expression().getExpressionString()
            );

    public static final StreamCodec<FriendlyByteBuf, ExpressionWrapper> STREAM_CODEC = StreamCodec.of(
            (buf, val) -> buf.writeUtf(val.expression.getExpressionString()),
            buf -> new ExpressionWrapper(new Expression(buf.readUtf()))
    );

    @Override
    public String getDescription() {
        return "unnamed wrapper";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof ExpressionWrapper other)) return false;

        return this.expression.getExpressionString().equals(other.expression.getExpressionString());
    }

    @Override
    public int hashCode() {
        return this.expression.getExpressionString().hashCode();
    }
}
