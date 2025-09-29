package com.marshal.core.parser;

import com.marshal.core.Command;

import java.util.List;
import java.util.Map;

/**
 * Represents the successful result of a command parsing operation.
 *
 * @param path The sequence of commands from the root to the selected subcommand.
 * @param arguments A map of parsed argument names to their converted values.
 */
public record CommandParseResult(List<Command> path, Map<String, Object> arguments) {

    /**
     * Gets the final, most specific command resolved from the input.
     */
    public Command selectedCommand() {
        return path.getLast();
    }
}
