package com.marshal.core.context;

import com.marshal.core.CommandExecutor;

/**
 * Holds contextual information about a command's execution environment.
 *
 * <p>Encapsulates all relevant states for a single command invocation, including the identity of the command sender.
 * Provides type-safe access to the sender, abstracting the underlying platform implementation (e.g., Bukkit
 * {@code CommandSender} or Velocity {@code CommandSource}).
 *
 * <p>Instances of this class are typically created by a {@link CommandContextFactory}.
 *
 * @param <T> The specific type of the command sender.
 * @see CommandContextFactory
 * @see CommandExecutor
 */
@SuppressWarnings("unused")
public class CommandContext<T> {

    private final T sender;

    /**
     * Constructs a new {@code CommandContext} with the specified sender.
     *
     * @param sender the entity that initiated the command execution.
     */
    public CommandContext(final T sender) {
        this.sender = sender;
    }

    /**
     * Retrieves the entity that executed the command.
     *
     * @return the command sender, guaranteed to be of type {@code T}.
     */
    public T getSender() {
        return sender;
    }
}
