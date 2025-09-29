package com.marshal.core;

import org.jspecify.annotations.Nullable;

/**
 * Represents a typed argument for a command.
 *
 * @param <T> the type of the argument's value
 */
public interface Argument<T> {

    /**
     * Gets the name of the argument.
     *
     * @return the argument's name
     */
    String getName();

    /**
     * Gets the expected type of the argument's value.
     *
     * @return the class representing the argument's type
     */
    Class<T> getType();

    /**
     * Gets the default value for this argument.
     *
     * @return the default value, or {@code null} if not set
     */
    @Nullable
    T getDefaultValue();

    /**
     * Checks if the argument is required.
     * <p>
     * An argument is considered required if it does not have a default value.
     *
     * @return {@code true} if the argument is required, {@code false} otherwise.
     */
    default boolean isRequired() {
        return getDefaultValue() == null;
    }

    /**
     * Creates a new builder for constructing an {@link Argument}.
     *
     * @param name the name of the argument, used for identification
     * @param type the expected class of the argument's value
     * @param <T> the type of the argument
     * @return a new {@link Builder} instance
     */
    static <T> Argument.Builder<T> newBuilder(final String name, final Class<T> type) {
        return new Argument.Builder<>(name, type);
    }

    /**
     * A default, immutable implementation of the {@link Argument} interface.
     *
     * @param <T> the type of the argument's value
     */
    final class DefaultArgument<T> implements Argument<T> {

        private final String name;
        private final Class<T> type;
        private final @Nullable T defaultValue;

        private DefaultArgument(final Argument.Builder<T> builder) {
            this.name = builder.name;
            this.type = builder.type;
            this.defaultValue = builder.defaultValue;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<T> getType() {
            return type;
        }

        @Override
        public @Nullable T getDefaultValue() {
            return defaultValue;
        }
    }

    /**
     * A builder for creating immutable {@link Argument} instances.
     *
     * @param <T> the type of the argument's value
     */
    final class Builder<T> {

        private final String name;
        private final Class<T> type;
        private @Nullable T defaultValue;

        /**
         * Constructs a new builder.
         *
         * @param name the name of the argument
         * @param type the expected class of the argument's value
         */
        private Builder(final String name, final Class<T> type) {
            this.name = name;
            this.type = type;
        }

        /**
         * Sets the default value for the argument.
         * <p>
         * Providing a default value makes the argument optional.
         *
         * @param defaultValue the value to use when the user does not provide the argument
         * @return this builder instance for chaining
         */
        public Builder<T> defaultValue(final T defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        /**
         * Builds an immutable {@link Argument} instance from this builder.
         *
         * @return a new {@link Argument} instance
         */
        public Argument<T> build() {
            return new DefaultArgument<>(this);
        }
    }
}
