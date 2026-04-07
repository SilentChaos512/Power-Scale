package net.silentchaos512.powerscale.core.resources;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

// Maybe move to Silent Lib? Mostly copied from Silent Gear, again.
public class DataHolder<T> implements Supplier<T> {
    private final Identifier objectId;
    private final Function<Identifier, T> getter;

    public DataHolder(Identifier objectId, Function<Identifier, T> getter) {
        this.objectId = objectId;
        this.getter = getter;
    }

    public static <T> DataHolder<T> empty() {
        return new DataHolder<>(Identifier.withDefaultNamespace("empty"), id -> null);
    }

    public static <T> Codec<DataHolder<T>> makeCodec(Function<Identifier, DataHolder<T>> getter) {
        return Identifier.CODEC.xmap(
                getter,
                DataHolder::getId
        );
    }

    public static <T>StreamCodec<FriendlyByteBuf, DataHolder<T>> makeStreamCodec(Function<Identifier, DataHolder<T>> getter) {
        return StreamCodec.of(
                (buf, val) -> buf.writeIdentifier(val.getId()),
                buf -> getter.apply(buf.readIdentifier())
        );
    }

    @Nullable
    public T getNullable() {
        return this.getter.apply(this.objectId);
    }

    public Optional<DataHolder<T>> toOptional() {
        if (!isPresent()) {
            return Optional.empty();
        }
        return Optional.of(this);
    }

    @Override
    public T get() {
        T ret = getNullable();
        Objects.requireNonNull(ret, () -> "Data resource not present: " + this.objectId);
        return ret;
    }

    public Identifier getId() {
        return this.objectId;
    }

    public boolean isPresent() {
        return this.getNullable() != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        T obj = getNullable();
        if (obj != null) {
            consumer.accept(obj);
        }
    }

    public Stream<T> stream() {
        return isPresent() ? Stream.of(get()) : Stream.of();
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        T obj = getNullable();
        return obj != null ? Optional.ofNullable(mapper.apply(obj)) : Optional.empty();
    }

    @Override
    public String toString() {
        return "DataHolder{" + this.objectId + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof DataHolder<?> other)) return false;

        return this.getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return this.objectId.hashCode();
    }
}
