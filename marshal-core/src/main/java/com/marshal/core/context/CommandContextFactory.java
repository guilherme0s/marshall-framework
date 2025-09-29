package com.marshal.core.context;

/**
 * @param <T> the type of the command sender
 */
public interface CommandContextFactory<T> {

    /**
     * Creates a new {@link CommandContext} for the given sender.
     *
     * @param sender the command sender instance.
     * @return a new, fully-initialized {@link CommandContext} for the sender.
     */
    CommandContext<T> create(T sender);
}
