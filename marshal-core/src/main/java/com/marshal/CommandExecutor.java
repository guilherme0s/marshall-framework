package com.marshal;

import com.marshal.parser.CommandSyntaxException;

public interface CommandExecutor<S> {

    int execute(CommandContext<S> context) throws CommandSyntaxException;
}
