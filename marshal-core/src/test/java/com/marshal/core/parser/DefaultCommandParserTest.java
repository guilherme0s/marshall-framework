package com.marshal.core.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.marshal.core.Argument;
import com.marshal.core.Command;
import com.marshal.core.context.CommandContext;
import com.marshal.core.convert.ArgumentConverterRegistry;

@ExtendWith(MockitoExtension.class)
class DefaultCommandParserTest {

    @Mock
    private ArgumentConverterRegistry<Object> converterRegistry;

    @InjectMocks
    private DefaultCommandParser<Object> parser;

    // A generic context for tests that don't depend on a specific sender type.
    private final CommandContext<Object> testContext = new CommandContext<>(new Object());

    @Test
    void shouldParseCommandWithNoArguments() {
        Command cmd = Command.newBuilder("help").build();
        CommandParseResult result = parser.parse(testContext, cmd, new String[]{});

        assertThat(result).isNotNull();
        assertThat(result.selectedCommand()).isSameAs(cmd);
        assertThat(result.path()).containsExactly(cmd);
        assertThat(result.parameters().isEmpty()).isTrue();
    }

    @Test
    void shouldParseSubcommandPathAndConvertArgument() {
        when(converterRegistry.find(Integer.class)).thenReturn((ctx, input) -> Integer.valueOf(input));

        Argument<Integer> arg = Argument.newBuilder("id", Integer.class).build();
        Command sub = Command.newBuilder("sub").addArgument(arg).build();
        Command root = Command.newBuilder("root").addSubCommand(sub).build();

        CommandParseResult result = parser.parse(testContext, root, new String[]{"sub", "42"});

        assertThat(result.selectedCommand()).isSameAs(sub);
        assertThat(result.path()).containsExactly(root, sub);
        assertThat(result.parameters().get("id").asInteger()).isEqualTo(42);
    }

    @Test
    void shouldUseDefaultValueWhenArgumentMissingAndNotRequired() {
        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class)
                        .defaultValue("John")
                        .build())
                .build();

        CommandParseResult result = parser.parse(testContext, root, new String[]{});

        assertThat(result.parameters().get("name").asString()).isEqualTo("John");
    }

    @Test
    void shouldThrowWhenMissingRequiredArgument() {
        // @see Argument#isRequired
        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(testContext, root, new String[0]))
                .isInstanceOf(MissingRequiredArgumentException.class)
                .hasFieldOrPropertyWithValue("command", root)
                .hasFieldOrPropertyWithValue("missingArgument", root.getArguments().getFirst());
    }

    @Test
    void shouldThrowWhenTooManyArgumentsProvided() {
        when(converterRegistry.find(String.class)).thenReturn((ctx, input) -> input);

        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("a", String.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(testContext, root, new String[]{"one", "two"}))
                .isInstanceOf(TooManyArgumentsProvidedException.class)
                .hasFieldOrPropertyWithValue("command", root);
    }

    @Test
    void shouldParseCommandWithMultipleArgumentsRequiredAndOptional() {
        when(converterRegistry.find(String.class)).thenReturn((ctx, input) -> input);
        when(converterRegistry.find(Integer.class)).thenReturn((ctx, input) -> Integer.valueOf(input));

        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class).build())
                .addArgument(Argument.newBuilder("age", Integer.class).build())
                .addArgument(Argument.newBuilder("city", String.class)
                        .defaultValue("Unknown")
                        .build())
                .build();

        CommandParseResult result1 = parser.parse(testContext, root, new String[]{"Alice", "25", "NewYork"});
        assertThat(result1.parameters().get("name").asString()).isEqualTo("Alice");
        assertThat(result1.parameters().get("age").asInteger()).isEqualTo(25);
        assertThat(result1.parameters().get("city").asString()).isEqualTo("NewYork");

        CommandParseResult result2 = parser.parse(testContext, root, new String[]{"Bob", "30"});
        assertThat(result2.parameters().get("name").asString()).isEqualTo("Bob");
        assertThat(result2.parameters().get("age").asInteger()).isEqualTo(30);
        assertThat(result2.parameters().get("city").asString()).isEqualTo("Unknown");
    }

    @Test
    void shouldThrowWhenNoConverterRegisteredForNonStringType() {
        Command command = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", Integer.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(testContext, command, new String[]{"7"}))
                .isInstanceOf(IllegalStateException.class);
    }
}
