package com.marshal.core;

/**
 * Represents an action to be executed when a command is run.
 */
@FunctionalInterface
public interface CommandExecutor {

    /**
     * Executes the command logic.
     *
     * @param sender the entity that executed the command
     * @param args the arguments provided for the command
     */
    void execute(Object sender, String[] args);
}
