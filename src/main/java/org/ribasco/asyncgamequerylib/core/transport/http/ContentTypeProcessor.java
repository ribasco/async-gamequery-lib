package org.ribasco.asyncgamequerylib.core.transport.http;

import java.util.function.Function;

abstract public class ContentTypeProcessor<T> implements Function<String, T> {
    protected abstract T processContent(String body);

    @Override
    public final T apply(String s) {
        return processContent(s);
    }
}
