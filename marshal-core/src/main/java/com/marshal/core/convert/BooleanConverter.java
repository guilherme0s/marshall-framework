package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;

public final class BooleanConverter implements ArgumentConverter<Object, Boolean> {

    @Override
    public Boolean convert(final CommandContext<Object> context, final String input)
            throws ArgumentConversionException {

        final String normalized = input.trim().toLowerCase();
        if ("true".equals(normalized) || "1".equals(normalized)) {
            return true;
        } else if ("false".equals(normalized) || "0".equals(normalized)) {
            return false;
        }
        throw new ArgumentConversionException("Invalid boolean");
    }
}
