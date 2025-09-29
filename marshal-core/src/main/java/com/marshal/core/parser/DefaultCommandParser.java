package com.marshal.core.parser;

import com.marshal.core.Command;
import com.marshal.core.Argument;
import com.marshal.core.convert.ArgumentConverter;
import com.marshal.core.convert.ArgumentConverterRegistry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The default implementation of the {@link CommandParser} interface.
 *
 * <p>This parser resolves subcommands greedily and then parses the remaining tokens as arguments
 * for the final command.
 */
public class DefaultCommandParser implements CommandParser {

    private final ArgumentConverterRegistry converterRegistry;

    public DefaultCommandParser(ArgumentConverterRegistry converterRegistry) {
        this.converterRegistry = converterRegistry;
    }

    @Override
    public CommandParseResult parse(Command rootCommand, String[] args) {
        Deque<String> tokens = new ArrayDeque<>(Arrays.asList(args));

        List<Command> path = resolveCommandPath(rootCommand, tokens);
        Command target = path.getLast();

        Map<String, Object> parsedArguments = parseArguments(target, tokens);

        return new CommandParseResult(path, parsedArguments);
    }

    /**
     * Resolves the command path by traversing subcommands.
     * Uses greedy matching: consumes as many subcommands as possible.
     */
    private List<Command> resolveCommandPath(Command root, Deque<String> inputs) {
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

    private Map<String, Object> parseArguments(Command command, Deque<String> tokens) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Argument<?> argument : command.getArguments()) {
            if (tokens.isEmpty()) {
                if (argument.isRequired()) {
                    throw new MissingRequiredArgumentException(command, argument);
                }
                if (argument.getDefaultValue() != null) {
                    result.put(argument.getName(), argument.getDefaultValue());
                }
            } else {
                String token = tokens.poll();
                Object value = convertArgument(token, argument);
                result.put(argument.getName(), value);
            }
        }

        if (!tokens.isEmpty()) {
            throw new TooManyArgumentsProvidedException(command);
        }

        return result;
    }

    private <T> T convertArgument(String input, Argument<T> argument) {
        ArgumentConverter<T> converter = converterRegistry.find(argument.getType());

        if (converter == null) {
            throw new IllegalStateException("No converter registered for type: " + argument.getType().getSimpleName());
        }

        return converter.convert(input);
    }
}
