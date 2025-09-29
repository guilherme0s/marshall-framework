package com.marshal.core.parser;

import com.marshal.core.Command;
import com.marshal.core.Argument;
import com.marshal.core.convert.ArgumentConverterRegistry;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCommandParserTest {

    @Mock
    private ArgumentConverterRegistry converterRegistry;

    @InjectMocks
    private DefaultCommandParser parser;

    @Test
    void shouldParseCommandWithNoArguments() {
        Command cmd = Command.newBuilder("help").build();
        CommandParseResult result = parser.parse(cmd, new String[]{});

        assertThat(result).isNotNull();
        assertThat(result.selectedCommand()).isSameAs(cmd);
        assertThat(result.path()).containsExactly(cmd);
        assertThat(result.arguments()).isEmpty();
    }

    @Test
    void shouldParseSubcommandPathAndConvertArgument() {
        when(converterRegistry.find(Integer.class)).thenReturn(Integer::valueOf);

        Argument<@NonNull Integer> arg = Argument.newBuilder("id", Integer.class).build();
        Command sub = Command.newBuilder("sub").addArgument(arg).build();
        Command root = Command.newBuilder("root").addSubCommand(sub).build();

        CommandParseResult result = parser.parse(root, new String[]{"sub", "42"});

        assertThat(result.selectedCommand()).isSameAs(sub);
        assertThat(result.path()).containsExactly(root, sub);
        assertThat(result.arguments()).containsEntry("id", 42);
    }

    @Test
    void shouldUseDefaultValueWhenArgumentMissingAndNotRequired() {
        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class)
                        .defaultValue("John")
                        .build())
                .build();

        CommandParseResult result = parser.parse(root, new String[]{});

        assertThat(result.arguments()).containsEntry("name", "John");
    }

    @Test
    void shouldThrowWhenMissingRequiredArgument() {
        // @see Argument#isRequired
        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(root, new String[0]))
                .isInstanceOf(MissingRequiredArgumentException.class)
                .hasFieldOrPropertyWithValue("command", root)
                .hasFieldOrPropertyWithValue("missingArgument", root.getArguments().getFirst());
    }

    @Test
    void shouldThrowWhenTooManyArgumentsProvided() {
        when(converterRegistry.find(String.class)).thenReturn(input -> input);

        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("a", String.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(root, new String[]{"one", "two"}))
                .isInstanceOf(TooManyArgumentsProvidedException.class)
                .hasFieldOrPropertyWithValue("command", root);
    }

    @Test
    void shouldParseCommandWithMultipleArgumentsRequiredAndOptional() {
        when(converterRegistry.find(String.class)).thenReturn(input -> input);
        when(converterRegistry.find(Integer.class)).thenReturn(Integer::valueOf);

        Command root = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", String.class).build())
                .addArgument(Argument.newBuilder("age", Integer.class).build())
                .addArgument(Argument.newBuilder("city", String.class)
                        .defaultValue("Unknown")
                        .build())
                .build();

        CommandParseResult result1 = parser.parse(root, new String[]{"Alice", "25", "NewYork"});
        assertThat(result1.arguments())
                .containsEntry("name", "Alice")
                .containsEntry("age", 25)
                .containsEntry("city", "NewYork");

        CommandParseResult result2 = parser.parse(root, new String[]{"Bob", "30"});
        assertThat(result2.arguments())
                .containsEntry("name", "Bob")
                .containsEntry("age", 30)
                .containsEntry("city", "Unknown");
    }

    @Test
    void shouldThrowWhenNoConverterRegisteredForNonStringType() {
        Command command = Command.newBuilder("root")
                .addArgument(Argument.newBuilder("name", Integer.class).build())
                .build();

        assertThatThrownBy(() -> parser.parse(command, new String[]{"7"}))
                .isInstanceOf(IllegalStateException.class);
    }
}
