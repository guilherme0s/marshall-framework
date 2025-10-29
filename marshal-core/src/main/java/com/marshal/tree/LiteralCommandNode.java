package com.marshal.tree;

import com.marshal.CommandContext;
import com.marshal.CommandExecutor;
import com.marshal.parser.CommandSyntaxException;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class LiteralCommandNode<S> implements CommandNode<S> {

    private final String literal;
    private @Nullable Predicate<S> requirement;
    private @Nullable CommandExecutor<S> executor;
    private @Nullable Map<String, CommandNode<S>> children;
    private boolean caseSensitive = true;

    public LiteralCommandNode(String literal) {
        this.literal = literal;
    }

    @Override
    public String getName() {
        return this.literal;
    }

    @Override
    public Collection<CommandNode<S>> getChildren() {
        return children == null
                ? Collections.emptyList()
                : Collections.unmodifiableCollection(children.values());
    }

    @Override
    public void addChild(CommandNode<S> child) {
        if (children == null) {
            children = new HashMap<>();
        }
        children.put(child.getName(), child);
    }

    @Override
    public @Nullable CommandNode<S> getChild(String name) {
        return children != null ? children.get(name) : null;
    }

    @Override
    public boolean canUse(S source) {
        if (requirement != null) {
            return requirement.test(source);
        }
        return true;
    }

    @Override
    public @Nullable Predicate<S> getRequirement() {
        return requirement;
    }

    @Override
    public int execute(CommandContext<S> context) throws CommandSyntaxException {
        if (executor == null) {
            throw new IllegalArgumentException("Command is not executable");
        }
        return executor.execute(context);
    }

    @Override
    public boolean isExecutable() {
        return executor != null;
    }

    public void setExecutor(@Nullable CommandExecutor<S> executor) {
        this.executor = executor;
    }

    public void setRequirement(@Nullable Predicate<S> requirement) {
        this.requirement = requirement;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LiteralCommandNode<?> other)) {
            return false;
        }

        return literal.equals(other.literal);
    }

    @Override
    public int hashCode() {
        return literal.hashCode();
    }
}
