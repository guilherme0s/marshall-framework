package com.marshal.core.parser;

import com.marshal.core.Command;

/**
 * Thrown when more arguments are provided than the command can accept.
 */
public class TooManyArgumentsProvidedException extends ParserException {

    /**
     * Constructs a new {@code TooManyArgumentsException}.
     *
     * @param command The command that was being parsed.
     */
    public TooManyArgumentsProvidedException(Command command) {
        super(command, "Too many arguments provided");
    }
}