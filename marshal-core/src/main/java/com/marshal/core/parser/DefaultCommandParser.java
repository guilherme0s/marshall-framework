package com.marshal.core.parser;

import com.marshal.core.Command;
import com.marshal.core.Argument;
import com.marshal.core.convert.ArgumentConverter;
import com.marshal.core.context.CommandContext;
import com.marshal.core.convert.ArgumentConverterRegistry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * The default implementation of the {@link CommandParser} interface.
 *
 * <p>This parser resolves subcommands greedily and then parses the remaining tokens as arguments
 * for the final command.
 */
public class DefaultCommandParser<C> implements CommandParser<C> {

    private final ArgumentConverterRegistry<C> converterRegistry;

    public DefaultCommandParser(final ArgumentConverterRegistry<C> converterRegistry) {
        this.converterRegistry = converterRegistry;
    }

    @Override
    public CommandParseResult parse(final CommandContext<C> context, final Command rootCommand, final String[] args) {
        Deque<String> tokens = new ArrayDeque<>(Arrays.asList(args));

        List<Command> path = resolveCommandPath(rootCommand, tokens);
        Command target = path.getLast();

        CommandParameters parameters = parseArguments(context, target, tokens);

        return new CommandParseResult(path, parameters);
    }

    /**
     * Resolves the command path by traversing subcommands. Uses greedy matching: consumes as many subcommands as
     * possible.
     */
    private List<Command> resolveCommandPath(final Command root, final Deque<String> inputs) {
        List<Command> path = new ArrayList<>();
        Command current = root;
        path.add(current);

        while (!inputs.isEmpty()) {
            String next = inputs.peek();
            Command sub = current.getSubCommand(next);

            if (sub == null) {
                break; // No more subcommands, remaining tokens are arguments
            }

            inputs.poll(); // Consume the subcommand token
            current = sub;
            path.add(current);
        }

        return path;
    }

    private CommandParameters parseArguments(final CommandContext<C> context, final Command command,
            final Deque<String> tokens) {

        CommandParameters parameters = new CommandParameters();

        for (Argument<?> argument : command.getArguments()) {
            if (tokens.isEmpty()) {
                if (argument.isRequired()) {
                    throw new MissingRequiredArgumentException(command, argument);
                }
                if (argument.getDefaultValue() != null) {
                    parameters.put(argument.getName(), argument.getDefaultValue());
                }
            } else {
                String token = tokens.poll();
                Object value = convertArgument(context, token, argument);
                parameters.put(argument.getName(), value);
            }
        }

        if (!tokens.isEmpty()) {
            throw new TooManyArgumentsProvidedException(command);
        }

        return parameters;
    }

    private <T> T convertArgument(final CommandContext<C> context, final String input, final Argument<T> argument) {
        ArgumentConverter<C, T> converter = converterRegistry.find(argument.getType());

        if (converter == null) {
            throw new IllegalStateException("No converter registered for type: " + argument.getType().getSimpleName());
        }

        return converter.convert(context, input);
    }
}
