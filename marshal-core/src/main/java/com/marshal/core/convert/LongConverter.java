package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;

public final class LongConverter implements ArgumentConverter<Object, Long> {

    @Override
    public Long convert(final CommandContext<Object> context, final String input) throws ArgumentConversionException {
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }
}
