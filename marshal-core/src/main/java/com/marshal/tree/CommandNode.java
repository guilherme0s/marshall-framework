package com.marshal.tree;

import com.marshal.CommandContext;
import com.marshal.parser.CommandSyntaxException;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public interface CommandNode<S> {

    String getName();

    Collection<CommandNode<S>> getChildren();

    void addChild(CommandNode<S> child);

    @Nullable CommandNode<S> getChild(String name);

    boolean canUse(S source);

    @Nullable Predicate<S> getRequirement();

    int execute(CommandContext<S> context) throws CommandSyntaxException;

    boolean isExecutable();
}
