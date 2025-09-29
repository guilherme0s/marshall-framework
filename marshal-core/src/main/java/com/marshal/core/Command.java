package com.marshal.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jspecify.annotations.Nullable;

/**
 * Represents a command that can be executed.
 * <p>
 * A command has a unique name, an optional description, a list of aliases, a sequence of arguments, and a map of
 * subcommands.
 * <p>
 * Instances of this interface are immutable and should be created using the {@link Builder}.
 */
public interface Command {

    /**
     * Gets the primary name of the command.
     *
     * @return the command's name
     */
    String getName();

    /**
     * Gets the description of what the command does.
     *
     * @return the command's description, or {@code null} if not set
     */
    @Nullable
    String getDescription();

    /**
     * Gets the executor for this command.
     *
     * @return the command's executor, or {@code null} if this command is not directly executable
     */
    @Nullable
    CommandExecutor getExecutor();

    /**
     * Gets a list of alternative names for the command.
     *
     * @return an immutable list of aliases
     */
    List<String> getAliases();

    /**
     * Gets the list of arguments that this command accepts.
     *
     * @return an immutable list of arguments
     */
    List<Argument<?>> getArguments();

    /**
     * Gets a map of subcommands, keyed by their names and aliases.
     *
     * @return an immutable map of subcommands
     */
    Map<String, Command> getSubCommands();

    /**
     * Gets a subcommand by its name or alias.
     *
     * @param name the name or alias of the subcommand
     * @return the subcommand, or {@code null} if not found
     */
    @Nullable
    default Command getSubCommand(final String name) {
        return getSubCommands().get(name.toLowerCase());
    }

    /**
     * Creates a new builder for constructing a {@link Command}.
     *
     * @param name the primary name of the command
     * @return a new {@link Builder} instance
     */
    static Command.Builder newBuilder(final String name) {
        return new Command.Builder(name);
    }

    /**
     * A default, immutable implementation of the {@link Command} interface.
     */
    final class DefaultCommand implements Command {

        private final String name;
        private final @Nullable String description;
        private final @Nullable CommandExecutor executor;
        private final List<String> aliases;
        private final List<Argument<?>> arguments;
        private final Map<String, Command> subCommands;

        private DefaultCommand(final Command.Builder builder) {
            this.name = builder.name;
            this.description = builder.description;
            this.executor = builder.executor;
            this.aliases = builder.aliases;
            this.arguments = builder.arguments;
            this.subCommands = builder.subCommands;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public @Nullable String getDescription() {
            return description;
        }

        @Override
        public @Nullable CommandExecutor getExecutor() {
            return executor;
        }

        @Override
        public List<String> getAliases() {
            return aliases;
        }

        @Override
        public List<Argument<?>> getArguments() {
            return arguments;
        }

        @Override
        public Map<String, Command> getSubCommands() {
            return subCommands;
        }
    }

    /**
     * A builder for creating immutable {@link Command} instances.
     */
    final class Builder {

        private final String name;
        private @Nullable String description;
        private @Nullable CommandExecutor executor;
        private final List<String> aliases = new ArrayList<>();
        private final List<Argument<?>> arguments = new ArrayList<>();
        private final Map<String, Command> subCommands = new HashMap<>();

        private Builder(final String name) {
            this.name = name;
        }

        /**
         * Sets the description for the command.
         *
         * @param description a brief explanation of the command's purpose
         * @return this builder instance for chaining
         */
        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        /**
         * Adds aliases for the command.
         *
         * @param aliases alternative names for the command
         * @return this builder instance for chaining
         */
        public Builder aliases(final String... aliases) {
            this.aliases.addAll(List.of(aliases));
            return this;
        }

        /**
         * Sets the {@link CommandExecutor} for the command.
         *
         * @param executor the action to be performed when the command is executed
         * @return this builder instance for chaining
         */
        public Builder executes(final CommandExecutor executor) {
            this.executor = executor;
            return this;
        }

        /**
         * Adds an argument to the command's signature.
         * <p>
         * Arguments must be added in order. All required arguments must be added before any optional arguments.
         *
         * @param argument the argument to add
         * @param <T> the type of the argument's value
         * @return this builder instance for chaining
         * @throws IllegalStateException if a required argument is added after an optional one.
         */
        public <T> Builder addArgument(final Argument<T> argument) {
            if (argument.isRequired() && !arguments.isEmpty()) {
                Argument<?> lastArgument = arguments.getLast();
                if (!lastArgument.isRequired()) {
                    throw new IllegalStateException(
                            "Cannot add a required argument '" + argument.getName()
                                    + "' after an optional argument '" + lastArgument.getName() + "'.");
                }
            }
            this.arguments.add(argument);
            return this;
        }

        /**
         * Adds a subcommand.
         * <p>
         * The subcommand will be registered under its primary name and all of its aliases.
         *
         * @param subCommand the subcommand to add
         * @return this builder instance for chaining
         * @throws IllegalArgumentException if the subcommand's name or any of its aliases conflict with an
         *         already registered subcommand.
         */
        public Builder addSubCommand(final Command subCommand) {
            final List<String> allNames = new ArrayList<>();
            allNames.add(subCommand.getName().toLowerCase());
            for (String alias : subCommand.getAliases()) {
                allNames.add(alias.toLowerCase());
            }

            for (String name : allNames) {
                if (subCommands.containsKey(name)) {
                    throw new IllegalArgumentException(
                            "Subcommand name or alias '" + name + "' conflicts with an existing command."
                    );
                }
            }

            for (String name : allNames) {
                subCommands.put(name, subCommand);
            }
            return this;
        }

        /**
         * Builds an immutable {@link Command} instance from this builder.
         *
         * @return a new {@link Command} instance
         */
        public Command build() {
            return new DefaultCommand(this);
        }
    }
}
