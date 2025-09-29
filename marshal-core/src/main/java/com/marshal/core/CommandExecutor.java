package com.marshal.core;

import com.marshal.core.parser.CommandParameters;

/**
 * Represents an action to be executed when a command is run.
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executes the command logic.
     *
     * @param sender the entity that executed the command
     * @param parameters the arguments provided for the command
     */
    void execute(Object sender, CommandParameters parameters);
}
