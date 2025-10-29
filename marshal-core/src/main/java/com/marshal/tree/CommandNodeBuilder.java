package com.marshal.tree;

import com.marshal.CommandExecutor;

import java.util.function.Predicate;

public interface CommandNodeBuilder<S, T extends CommandNode<S>> {

    CommandNodeBuilder<S, T> then(CommandNode<S> child);

    CommandNodeBuilder<S, T> then(CommandNodeBuilder<S, ?> child);

    CommandNodeBuilder<S, T> requires(Predicate<S> requirement);

    CommandNodeBuilder<S, T> executes(CommandExecutor<S> executor);

    T build();
}
