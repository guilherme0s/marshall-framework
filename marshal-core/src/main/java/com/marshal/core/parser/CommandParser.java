package com.marshal.core.parser;

import com.marshal.core.Command;
import com.marshal.core.context.CommandContext;

/**
 * Parses raw string arguments into a structured command execution path and typed arguments.
 */
public interface CommandParser<C> {

    /**
     * Parses the given string arguments against a root command definition.
     *
     * @param rootCommand The root command to start parsing from.
     * @param args The raw string arguments to parse.
     * @return A {@link CommandParseResult} containing the resolved command path and parsed arguments.
     */
    CommandParseResult parse(CommandContext<C> context, Command rootCommand, String[] args);
}
