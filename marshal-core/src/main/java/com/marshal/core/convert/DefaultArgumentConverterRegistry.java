package com.marshal.core.convert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.Nullable;

/**
 * A thread-safe, high-performance implementation of {@link ArgumentConverterRegistry}.
 */
public class DefaultArgumentConverterRegistry<C> implements ArgumentConverterRegistry<C> {

    private final Map<Class<?>, ArgumentConverter<?, ?>> converters = new ConcurrentHashMap<>();

    @Override
    public <T> void register(final Class<T> type, final ArgumentConverter<C, T> converter) {
        converters.put(type, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable ArgumentConverter<C, T> find(final Class<T> type) {
        return (ArgumentConverter<C, T>) converters.get(type);
    }
}
