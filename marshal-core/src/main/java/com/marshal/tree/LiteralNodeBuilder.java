package com.marshal.tree;

import com.marshal.CommandExecutor;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class LiteralNodeBuilder<S> implements CommandNodeBuilder<S, LiteralCommandNode<S>> {

    private final String literal;
    private @Nullable Predicate<S> requirement;
    private @Nullable CommandExecutor<S> executor;
    private @Nullable List<CommandNode<S>> children;
    private boolean caseSensitive = true;

    public LiteralNodeBuilder(String literal) {
        this.literal = literal;
    }

    @Override
    public CommandNodeBuilder<S, LiteralCommandNode<S>> then(CommandNode<S> child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
        return this;
    }

    @Override
    public CommandNodeBuilder<S, LiteralCommandNode<S>> then(CommandNodeBuilder<S, ?> child) {
        return then(child.build());
    }

    @Override
    public CommandNodeBuilder<S, LiteralCommandNode<S>> requires(Predicate<S> requirement) {
        this.requirement = requirement;
        return this;
    }

    @Override
    public CommandNodeBuilder<S, LiteralCommandNode<S>> executes(CommandExecutor<S> executor) {
        this.executor = executor;
        return this;
    }

    public LiteralNodeBuilder<S> caseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    @Override
    public LiteralCommandNode<S> build() {
        LiteralCommandNode<S> node = new LiteralCommandNode<>(literal);
        node.setExecutor(executor);
        node.setCaseSensitive(caseSensitive);
        node.setRequirement(requirement);

        if (children != null) {
            for (CommandNode<S> child : children) {
                node.addChild(child);
            }
        }

        return node;
    }
}
