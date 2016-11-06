package org.ribasco.agql.core.functions;

import java.util.Objects;

public interface TriConsumer<T, U, V> {
    void accept(T t, U u, V v);

    default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);
        return (a, b, c) -> {
            accept(a, b, c);
            after.accept(a, b, c);
        };
    }
}
