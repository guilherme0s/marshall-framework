package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;

public final class IntegerConverter implements ArgumentConverter<Object, Integer> {

    @Override
    public Integer convert(final CommandContext<Object> context, final String input)
            throws ArgumentConversionException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }
}
