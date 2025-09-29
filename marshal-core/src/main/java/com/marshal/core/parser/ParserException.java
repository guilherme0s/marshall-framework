package com.marshal.core.parser;

import com.marshal.core.Command;

/**
 * A base exception for all errors that occur during command parsing.
 */
public class ParserException extends RuntimeException {

    private final Command command;

    /**
     * Constructs a new {@code ParserException}.
     *
     * @param command The command that was being parsed when the error occurred.
     * @param message The detail message.
     */
    public ParserException(Command command, String message) {
        super(message);
        this.command = command;
    }

    /** Gets the command associated with this parsing error. */
    public Command getCommand() {
        return command;
    }
}
