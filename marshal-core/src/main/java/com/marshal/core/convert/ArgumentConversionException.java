package com.marshal.core.convert;

/**
 * Thrown to indicate that a string argument could not be converted to the target type.
 */
public class ArgumentConversionException extends RuntimeException {

    /**
     * Constructs a new {@code ArgumentConversionException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public ArgumentConversionException(String message) {
        super(message);
    }
}
