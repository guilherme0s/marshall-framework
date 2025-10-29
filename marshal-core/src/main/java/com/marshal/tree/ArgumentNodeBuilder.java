package com.marshal.tree;

import com.marshal.CommandExecutor;
import com.marshal.parser.ArgumentParser;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class ArgumentNodeBuilder<S, T> implements CommandNodeBuilder<S, ArgumentCommandNode<S, T>> {

    private final String name;
    private final ArgumentParser<T> parser;

    private @Nullable Predicate<S> requirement;
    private @Nullable CommandExecutor<S> executor;
    private @Nullable List<CommandNode<S>> children;

    public ArgumentNodeBuilder(String name, ArgumentParser<T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @Override
    public CommandNodeBuilder<S, ArgumentCommandNode<S, T>> then(CommandNode<S> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        return this;
    }

    @Override
    public CommandNodeBuilder<S, ArgumentCommandNode<S, T>> then(CommandNodeBuilder<S, ?> child) {
        return then(child.build());
    }

    @Override
    public CommandNodeBuilder<S, ArgumentCommandNode<S, T>> requires(Predicate<S> requirement) {
        this.requirement = requirement;
        return this;
    }

    @Override
    public CommandNodeBuilder<S, ArgumentCommandNode<S, T>> executes(CommandExecutor<S> executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public ArgumentCommandNode<S, T> build() {
        ArgumentCommandNode<S, T> node = new ArgumentCommandNode<>(name, parser);
        node.setRequirement(requirement);
        node.setExecutor(executor);

        if (children != null) {
            for (CommandNode<S> child : children) {
                node.addChild(child);
            }
        }
        return node;
    }
}
