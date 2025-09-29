package com.marshal.core.convert;

import org.jspecify.annotations.Nullable;

/**
 * A registry for managing {@link ArgumentConverter} instances.
 *
 * <p>This allows for registering and retrieving converters for different types.
 */
public interface ArgumentConverterRegistry {

    /**
     * Registers an {@link ArgumentConverter} for a specific type.
     *
     * @param <T> The type the converter is for.
     * @param type The class of the type.
     * @param converter The converter instance.
     */
    <T> void register(Class<T> type, ArgumentConverter<?, T> converter);

    /**
     * Retrieves the {@link ArgumentConverter} for a given type.
     *
     * @param <T> The type of the converter to retrieve.
     * @param type The class of the type.
     * @return The registered converter for the given type, or {@code null} if none is found.
     */
    @Nullable
    <T> ArgumentConverter<?, T> find(Class<T> type);
}
