package com.marshal.parser;

public class CommandSyntaxException extends Exception {

    private final StringReader reader;

    public CommandSyntaxException(String message, StringReader reader) {
        super(message, null, true, false);
        this.reader = reader;
    }

    public StringReader getReader() {
        return reader;
    }
}
