package com.marshal.core.parser;

import com.marshal.core.Command;

/**
 * Parses raw string arguments into a structured command execution path and typed arguments.
 */
public interface CommandParser {

    /**
     * Parses the given string arguments against a root command definition.
     *
     * @param rootCommand The root command to start parsing from.
     * @param args The raw string arguments to parse.
     * @return A {@link CommandParseResult} containing the resolved command path and parsed arguments.
     */
    CommandParseResult parse(Command rootCommand, String[] args);
}
