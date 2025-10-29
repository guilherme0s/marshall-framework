package com.marshal.tree;

import com.marshal.CommandContext;
import com.marshal.CommandExecutor;
import com.marshal.parser.ArgumentParser;
import com.marshal.parser.CommandSyntaxException;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class ArgumentCommandNode<S, T> implements CommandNode<S> {

    private final String name;
    private final ArgumentParser<T> parser;
    private @Nullable Predicate<S> requirement;
    private @Nullable CommandExecutor<S> executor;
    private @Nullable Map<String, CommandNode<S>> children;

    public ArgumentCommandNode(String name, ArgumentParser<T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<CommandNode<S>> getChildren() {
        return children != null
                ? Collections.unmodifiableCollection(children.values())
                : Collections.emptyList();
    }

    @Override
    public void addChild(CommandNode<S> child) {
        if (children == null) {
            children = new HashMap<>();
        }

        CommandNode<S> existing = children.get(child.getName());
        if (existing != null) {
            for (CommandNode<S> grandchild : child.getChildren()) {
                existing.addChild(grandchild);
            }
        } else {
            children.put(child.getName(), child);
        }
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

    public void setRequirement(@Nullable Predicate<S> requirement) {
        this.requirement = requirement;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArgumentCommandNode<?, ?> other)) {
            return false;
        }

        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
