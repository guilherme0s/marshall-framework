package com.marshal.core.parser;

import com.marshal.core.Command;
import com.marshal.core.Argument;

/**
 * Thrown when a required command argument is not provided.
 */
public class MissingRequiredArgumentException extends ParserException {

    private final Argument<?> missingArgument;

    /**
     * Constructs a new {@code MissingRequiredArgumentException}.
     *
     * @param command The command that was being parsed.
     * @param missingArgument The argument that was required but not provided.
     */
    public MissingRequiredArgumentException(Command command, Argument<?> missingArgument) {
        super(command, "Missing required argument '" + missingArgument.getName() + "'");
        this.missingArgument = missingArgument;
    }

    /**
     * Gets the argument that was required but not provided.
     */
    public Argument<?> getMissingArgument() {
        return missingArgument;
    }
}
