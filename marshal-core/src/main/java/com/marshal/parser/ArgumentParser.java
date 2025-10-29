package com.marshal.parser;

public interface ArgumentParser<T> {

    T parse(StringReader input) throws CommandSyntaxException;
}
