package com.marshal.core.convert;

import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe, high-performance implementation of {@link ArgumentConverterRegistry}.
 */
public class DefaultArgumentConverterRegistry implements ArgumentConverterRegistry {

    private final Map<Class<?>, ArgumentConverter<?>> converters = new ConcurrentHashMap<>();

    @Override
    public <T> void register(Class<T> type, ArgumentConverter<T> converter) {
        converters.put(type, converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable ArgumentConverter<T> find(Class<T> type) {
        return (ArgumentConverter<T>) converters.get(type);
    }
}
