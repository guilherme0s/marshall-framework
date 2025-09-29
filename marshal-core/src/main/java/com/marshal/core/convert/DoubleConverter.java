package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;

public final class DoubleConverter implements ArgumentConverter<Object, Double> {

    @Override
    public Double convert(final CommandContext<Object> context, final String input) throws ArgumentConversionException {
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }
}
