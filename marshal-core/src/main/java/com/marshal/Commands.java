package com.marshal;

import com.marshal.parser.ArgumentParser;
import com.marshal.tree.ArgumentNodeBuilder;
import com.marshal.tree.LiteralNodeBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Commands {

    private Commands() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <S> LiteralNodeBuilder<S> literal(String name) {
        return new LiteralNodeBuilder<>(name);
    }

    public static <S, T> ArgumentNodeBuilder<S, T> argument(String name, ArgumentParser<T> parser) {
        return new ArgumentNodeBuilder<>(name, parser);
    }
}
