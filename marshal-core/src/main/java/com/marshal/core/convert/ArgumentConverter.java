package com.marshal.core.convert;

/**
 * Converts a string argument to an object of a specific type.
 *
 * @param <T> The type to convert to.
 */
@FunctionalInterface
public interface ArgumentConverter<T> {

    /**
     * Converts the given string input into an object of type {@code T}.
     *
     * @param input The string input to convert.
     * @return The converted object.
     * @throws ArgumentConversionException if the conversion fails.
     */
    T convert(String input) throws ArgumentConversionException;
}
