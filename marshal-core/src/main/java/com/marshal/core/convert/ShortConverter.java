package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;

public final class ShortConverter implements ArgumentConverter<Object, Short> {

    @Override
    public Short convert(final CommandContext<Object> context, final String input) throws ArgumentConversionException {
        try {
            return Short.parseShort(input);
        } catch (NumberFormatException ignored) {
            throw new InvalidNumberFormatException();
        }
    }
}
