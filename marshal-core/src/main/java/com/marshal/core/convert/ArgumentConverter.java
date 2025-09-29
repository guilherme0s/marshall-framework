package com.marshal.core.convert;

import com.marshal.core.context.CommandContext;
import com.marshal.core.parser.CommandParser;
import com.marshal.core.CommandExecutor;

/**
 * A strategy interface for converting a raw string argument into a specific object type.
 *
 * <p>This interface is a cornerstone of the framework's type-safe argument system.
 * It defines a contract for transforming the string-based input provided by a user into a strongly-typed Java object
 * that can be used within a {@link CommandExecutor}.
 *
 * <p>Implementations of {@code ArgumentConverter} are registered with an {@link ArgumentConverterRegistry}, which the
 * {@link CommandParser} uses to find the appropriate converter for each command argument based on its declared type.
 *
 * <p>The conversion process is context-aware, receiving a {@link CommandContext}. This allows
 * for sophisticated conversions that depend on the execution environment. For example, a converter can be constrained
 * to only work when the command sender is of a specific type (e.g., a Player), enabling context-sensitive logic like
 * finding a target player relative to the sender's location.
 *
 * <h3>Usage Example:</h3>
 * <pre>{@code
 * public class PlayerConverter implements ArgumentConverter<CommandSender, Player> {
 *
 *     @Override
 *     public Player convert(CommandContext<CommandSender> context, String input)
 *             throws ArgumentConversionException {
 *
 *         Player player = Bukkit.getPlayer(input);
 *         if (player == null) {
 *             throw new ArgumentConversionException("Player '" + input + "' not found or is not online.");
 *         } else if (context.getSender() instanceof Player sender && !sender.canSee(player)) {
 *             throw new ArgumentConversionException("You cannot see the specified player.");
 *         }
 *         return player;
 *     }
 * }
 *
 * // Registration
 * ArgumentConverterRegistry registry = new DefaultArgumentConverterRegistry();
 * registry.register(Player.class, new PlayerConverter());
 * }</pre>
 *
 * @param <C> The required type of the command sender for this converter. Use {@link Object} or a common
 *         supertype if the converter is sender-agnostic.
 * @param <T> The target type to which the string input will be converted.
 * @see ArgumentConverterRegistry
 * @see CommandParser
 * @see CommandContext
 */
@FunctionalInterface
public interface ArgumentConverter<C, T> {

    /**
     * Converts the given string input into an object of type {@code T}, using the provided command context if
     * necessary.
     *
     * @param context The execution context of the command, providing access to the sender and other
     *         environmental information. This can be used for context-dependent conversions (e.g., looking up a player
     *         by name).
     * @param input The raw string token provided by the user for this argument. This value is guaranteed to be
     *         non-null.
     * @return The converted object of type {@code T}.
     * @throws ArgumentConversionException if the string input cannot be successfully converted to the target
     *         type {@code T}. The exception message should be user-friendly, as it may be displayed directly to the
     *         command sender.
     */
    T convert(CommandContext<C> context, String input) throws ArgumentConversionException;
}
