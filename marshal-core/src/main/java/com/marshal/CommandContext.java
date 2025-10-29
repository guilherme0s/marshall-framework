package com.marshal;

public final class CommandContext<S> {

    private final S source;

    public CommandContext(S source) {
        this.source = source;
    }

    public S getSource() {
        return source;
    }
}
