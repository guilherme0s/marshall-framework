package com.marshal.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringReaderTest {

    @Test
    void shouldCreateReaderWithString() {
        StringReader reader = new StringReader("test");

        assertThat(reader.getString()).isEqualTo("test");
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldCreateReaderFromCopy() {
        StringReader original = new StringReader("test");
        original.setCursor(2);

        StringReader copy = new StringReader(original);

        assertThat(copy.getString()).isEqualTo("test");
        assertThat(copy.getCursor()).isEqualTo(2);
    }

    @Test
    void shouldSetValidCursor() {
        StringReader reader = new StringReader("test");
        reader.setCursor(2);
        assertThat(reader.getCursor()).isEqualTo(2);
    }

    @Test
    void shouldSetCursorToZero() {
        StringReader reader = new StringReader("test");
        reader.setCursor(2);
        reader.setCursor(0);
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldSetCursorToEndOfString() {
        StringReader reader = new StringReader("test");

        reader.setCursor(4);
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldThrowExceptionWhenCursorIsNegative() {
        StringReader reader = new StringReader("test");
        assertThatThrownBy(() -> reader.setCursor(-1)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void shouldThrowExceptionWhenCursorExceedsLength() {
        StringReader reader = new StringReader("test");
        assertThatThrownBy(() -> reader.setCursor(5)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void shouldGetRemainingLength() {
        StringReader reader = new StringReader("test");
        reader.setCursor(2);

        assertThat(reader.getRemainingLength()).isEqualTo(2);
    }

    @Test
    void shouldGetRemainingLengthWhenAtEnd() {
        StringReader reader = new StringReader("test");
        reader.setCursor(4);

        assertThat(reader.getRemainingLength()).isZero();
    }

    @Test
    void shouldGetTotalLength() {
        StringReader reader = new StringReader("test");

        assertThat(reader.getTotalLength()).isEqualTo(4);
    }

    @Test
    void shouldReturnTrueWhenCanRead() {
        StringReader reader = new StringReader("test");

        assertThat(reader.canRead()).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCannotRead() {
        StringReader reader = new StringReader("test");
        reader.setCursor(4);

        assertThat(reader.canRead()).isFalse();
    }

    @Test
    void shouldReturnTrueWhenCanReadLength() {
        StringReader reader = new StringReader("test");

        assertThat(reader.canRead(3)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenCannotReadLength() {
        StringReader reader = new StringReader("test");

        assertThat(reader.canRead(5)).isFalse();
    }

    @Test
    void shouldReturnTrueWhenCanReadExactRemainingLength() {
        StringReader reader = new StringReader("test");

        assertThat(reader.canRead(4)).isTrue();
    }

    @Test
    void shouldPeekCurrentCharacter() {
        StringReader reader = new StringReader("test");

        assertThat(reader.peek()).isEqualTo('t');
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldPeekWithOffset() {
        StringReader reader = new StringReader("test");

        assertThat(reader.peek(2)).isEqualTo('s');
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadAndAdvanceCursor() {
        StringReader reader = new StringReader("test");

        char result = reader.read();

        assertThat(result).isEqualTo('t');
        assertThat(reader.getCursor()).isEqualTo(1);
    }

    @Test
    void shouldReadMultipleCharacters() {
        StringReader reader = new StringReader("test");

        reader.read();
        reader.read();

        assertThat(reader.getCursor()).isEqualTo(2);
        assertThat(reader.peek()).isEqualTo('s');
    }

    @Test
    void shouldSkip() {
        StringReader reader = new StringReader("test");

        reader.skip();

        assertThat(reader.getCursor()).isEqualTo(1);
    }

    @Test
    void shouldSkipWhitespace() {
        StringReader reader = new StringReader("  \t\n\rtest");

        reader.skipWhitespace();

        assertThat(reader.getCursor()).isEqualTo(5);
        assertThat(reader.peek()).isEqualTo('t');
    }

    @Test
    void shouldNotSkipWhenNoWhitespace() {
        StringReader reader = new StringReader("test");

        reader.skipWhitespace();

        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldSkipWhitespaceUntilEnd() {
        StringReader reader = new StringReader("   ");

        reader.skipWhitespace();

        assertThat(reader.getCursor()).isEqualTo(3);
    }

    @Test
    void shouldReadPositiveInt() throws CommandSyntaxException {
        StringReader reader = new StringReader("123");
        assertThat(reader.readInt()).isEqualTo(123);
        assertThat(reader.getCursor()).isEqualTo(3);
    }

    @Test
    void shouldReadNegativeInt() throws CommandSyntaxException {
        StringReader reader = new StringReader("-456");
        assertThat(reader.readInt()).isEqualTo(-456);
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadZeroInt() throws CommandSyntaxException {
        StringReader reader = new StringReader("0");
        assertThat(reader.readInt()).isZero();
        assertThat(reader.getCursor()).isEqualTo(1);
    }

    @Test
    void shouldReadIntAndStopAtNonDigit() throws CommandSyntaxException {
        StringReader reader = new StringReader("123abc");
        assertThat(reader.readInt()).isEqualTo(123);
        assertThat(reader.getCursor()).isEqualTo(3);
        assertThat(reader.peek()).isEqualTo('a');
    }

    @Test
    void shouldThrowExceptionWhenReadingIntFromEmpty() {
        StringReader reader = new StringReader("");
        assertThatThrownBy(reader::readInt).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionWhenReadingIntFromNonDigit() {
        StringReader reader = new StringReader("abc");
        assertThatThrownBy(reader::readInt).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionWhenReadingIntWithOnlyMinus() {
        StringReader reader = new StringReader("-");
        assertThatThrownBy(reader::readInt).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionAndResetCursorWhenIntOverflows() {
        StringReader reader = new StringReader("99999999999999999999");

        assertThatThrownBy(reader::readInt).isInstanceOf(CommandSyntaxException.class);
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadPositiveLong() throws CommandSyntaxException {
        StringReader reader = new StringReader("123456789");
        assertThat(reader.readLong()).isEqualTo(123456789);
        assertThat(reader.getCursor()).isEqualTo(9);
    }

    @Test
    void shouldReadNegativeLong() throws CommandSyntaxException {
        StringReader reader = new StringReader("-987654321");
        assertThat(reader.readLong()).isEqualTo(-987654321L);
        assertThat(reader.getCursor()).isEqualTo(10);
    }

    @Test
    void shouldThrowExceptionWhenReadingLongFromEmpty() {
        StringReader reader = new StringReader("");
        assertThatThrownBy(reader::readLong).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionAndResetCursorWhenLongOverflows() {
        StringReader reader = new StringReader("99999999999999999999999999");
        assertThatThrownBy(reader::readLong).isInstanceOf(CommandSyntaxException.class);
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadPositiveFloat() throws CommandSyntaxException {
        StringReader reader = new StringReader("123.45");
        assertThat(reader.readFloat()).isEqualTo(123.45f);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadNegativeFloat() throws CommandSyntaxException {
        StringReader reader = new StringReader("-67.89");
        assertThat(reader.readFloat()).isEqualTo(-67.89f);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadFloatWithoutDecimal() throws CommandSyntaxException {
        StringReader reader = new StringReader("123");
        assertThat(reader.readFloat()).isEqualTo(123f);
        assertThat(reader.getCursor()).isEqualTo(3);
    }

    @Test
    void shouldReadFloatWithExponentLowerCase() throws CommandSyntaxException {
        StringReader reader = new StringReader("1.5e10");
        assertThat(reader.readFloat()).isEqualTo(1.5e10f);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadFloatWithExponentUpperCase() throws CommandSyntaxException {
        StringReader reader = new StringReader("2.5E-3");
        assertThat(reader.readFloat()).isEqualTo(2.5E-3f);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadFloatWithPositiveExponent() throws CommandSyntaxException {
        StringReader reader = new StringReader("1e+5");
        assertThat(reader.readFloat()).isEqualTo(1e+5f);
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadFloatWithTrailingDecimal() throws CommandSyntaxException {
        StringReader reader = new StringReader("123.");
        assertThat(reader.readFloat()).isEqualTo(123.0f);
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    void shouldThrowExceptionWhenReadingFloatFromEmpty() {
        StringReader reader = new StringReader("");
        assertThatThrownBy(reader::readFloat).isInstanceOf(CommandSyntaxException.class);
    }
    @Test
    void shouldThrowExceptionWhenReadingFloatFromNonDigit() {
        StringReader reader = new StringReader("abc");
        assertThatThrownBy(reader::readFloat).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionAndResetCursorWhenFloatIsInvalid() {
        StringReader reader = new StringReader("1.2.3");
        reader.readString(); // This will read "1.2.3" but it's invalid as float
        reader.setCursor(0);

        // Reading as float should fail on malformed input
        StringReader reader2 = new StringReader("1e");

        assertThatThrownBy(reader2::readFloat).isInstanceOf(CommandSyntaxException.class);
        assertThat(reader2.getCursor()).isZero();
    }

    @Test
    void shouldReadPositiveDouble() throws CommandSyntaxException {
        StringReader reader = new StringReader("123.456789");
        assertThat(reader.readDouble()).isEqualTo(123.456789);
        assertThat(reader.getCursor()).isEqualTo(10);
    }

    @Test
    void shouldReadNegativeDouble() throws CommandSyntaxException {
        StringReader reader = new StringReader("-987.654321");
        assertThat(reader.readDouble()).isEqualTo(-987.654321);
        assertThat(reader.getCursor()).isEqualTo(11);
    }

    @Test
    void shouldReadDoubleWithoutDecimal() throws CommandSyntaxException {
        StringReader reader = new StringReader("123");
        assertThat(reader.readDouble()).isEqualTo(123.0);
        assertThat(reader.getCursor()).isEqualTo(3);
    }

    @Test
    void shouldReadDoubleWithExponentLowerCase() throws CommandSyntaxException {
        StringReader reader = new StringReader("1.5e10");
        assertThat(reader.readDouble()).isEqualTo(1.5e10);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadDoubleWithExponentUpperCase() throws CommandSyntaxException {
        StringReader reader = new StringReader("2.5E-3");
        assertThat(reader.readDouble()).isEqualTo(2.5E-3);
        assertThat(reader.getCursor()).isEqualTo(6);
    }

    @Test
    void shouldReadDoubleWithPositiveExponent() throws CommandSyntaxException {
        StringReader reader = new StringReader("1e+5");
        assertThat(reader.readDouble()).isEqualTo(1e+5);
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldThrowExceptionWhenReadingDoubleFromEmpty() {
        StringReader reader = new StringReader("");
        assertThatThrownBy(reader::readDouble).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionWhenReadingDoubleFromNonDigit() {
        StringReader reader = new StringReader("xyz");
        assertThatThrownBy(reader::readDouble).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionAndResetCursorWhenDoubleIsInvalid() {
        StringReader reader = new StringReader("1e");

        assertThatThrownBy(reader::readDouble).isInstanceOf(CommandSyntaxException.class);
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadSimpleString() {
        StringReader reader = new StringReader("test");
        assertThat(reader.readString()).isEqualTo("test");
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadStringWithDigits() {
        StringReader reader = new StringReader("test123");
        assertThat(reader.readString()).isEqualTo("test123");
        assertThat(reader.getCursor()).isEqualTo(7);
    }

    @Test
    void shouldReadStringWithUnderscores() {
        StringReader reader = new StringReader("test_name");
        assertThat(reader.readString()).isEqualTo("test_name");
        assertThat(reader.getCursor()).isEqualTo(9);
    }

    @Test
    void shouldReadStringWithHyphens() {
        StringReader reader = new StringReader("test-name");
        assertThat(reader.readString()).isEqualTo("test-name");
        assertThat(reader.getCursor()).isEqualTo(9);
    }

    @Test
    void shouldReadStringWithDots() {
        StringReader reader = new StringReader("test.name");
        assertThat(reader.readString()).isEqualTo("test.name");
        assertThat(reader.getCursor()).isEqualTo(9);
    }

    @Test
    void shouldReadStringWithPlus() {
        StringReader reader = new StringReader("test+name");
        assertThat(reader.readString()).isEqualTo("test+name");
        assertThat(reader.getCursor()).isEqualTo(9);
    }

    @Test
    void shouldReadStringAndStopAtWhitespace() {
        StringReader reader = new StringReader("test name");
        assertThat(reader.readString()).isEqualTo("test");
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadStringAndStopAtDisallowedCharacter() {
        StringReader reader = new StringReader("test@name");
        assertThat(reader.readString()).isEqualTo("test");
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadEmptyStringFromEmpty() {
        StringReader reader = new StringReader("");
        assertThat(reader.readString()).isEmpty();
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadEmptyStringWhenStartingWithDisallowedChar() {
        StringReader reader = new StringReader("@test");
        assertThat(reader.readString()).isEmpty();
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldReadTrueBoolean() throws CommandSyntaxException {
        StringReader reader = new StringReader("true");
        assertThat(reader.readBoolean()).isTrue();
        assertThat(reader.getCursor()).isEqualTo(4);
    }

    @Test
    void shouldReadFalseBoolean() throws CommandSyntaxException {
        StringReader reader = new StringReader("false");
        assertThat(reader.readBoolean()).isFalse();
        assertThat(reader.getCursor()).isEqualTo(5);
    }

    @Test
    void shouldThrowExceptionWhenReadingBooleanFromEmpty() {
        StringReader reader = new StringReader("");
        assertThatThrownBy(reader::readBoolean).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionWhenReadingBooleanFromInvalidString() {
        StringReader reader = new StringReader("yes");
        assertThatThrownBy(reader::readBoolean).isInstanceOf(CommandSyntaxException.class);
    }

    @Test
    void shouldThrowExceptionAndResetCursorWhenBooleanIsInvalid() {
        StringReader reader = new StringReader("invalid");
        assertThatThrownBy(reader::readBoolean).isInstanceOf(CommandSyntaxException.class);
        assertThat(reader.getCursor()).isZero();
    }

    @Test
    void shouldGetReadPortion() {
        StringReader reader = new StringReader("test");
        reader.setCursor(2);
        assertThat(reader.getRead()).isEqualTo("te");
    }

    @Test
    void shouldGetReadWhenAtStart() {
        StringReader reader = new StringReader("test");
        assertThat(reader.getRead()).isEmpty();
    }

    @Test
    void shouldGetReadWhenAtEnd() {
        StringReader reader = new StringReader("test");
        reader.setCursor(4);
        assertThat(reader.getRead()).isEqualTo("test");
    }

    @Test
    void shouldGetRemainingPortion() {
        StringReader reader = new StringReader("test");
        reader.setCursor(2);
        assertThat(reader.getRemaining()).isEqualTo("st");
    }

    @Test
    void shouldGetRemainingWhenAtStart() {
        StringReader reader = new StringReader("test");
        assertThat(reader.getRemaining()).isEqualTo("test");
    }

    @Test
    void shouldGetRemainingWhenAtEnd() {
        StringReader reader = new StringReader("test");
        reader.setCursor(4);
        assertThat(reader.getRemaining()).isEmpty();
    }

    @Test
    void shouldIdentifyDigitCharacters() {
        assertThat(StringReader.isAllowedInUnquotedString('0')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('5')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('9')).isTrue();
    }

    @Test
    void shouldIdentifyUppercaseLetters() {
        assertThat(StringReader.isAllowedInUnquotedString('A')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('M')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('Z')).isTrue();
    }

    @Test
    void shouldIdentifyLowercaseLetters() {
        assertThat(StringReader.isAllowedInUnquotedString('a')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('m')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('z')).isTrue();
    }

    @Test
    void shouldIdentifyAllowedSpecialCharacters() {
        assertThat(StringReader.isAllowedInUnquotedString('_')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('-')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('.')).isTrue();
        assertThat(StringReader.isAllowedInUnquotedString('+')).isTrue();
    }

    @Test
    void shouldRejectDisallowedCharacters() {
        assertThat(StringReader.isAllowedInUnquotedString(' ')).isFalse();
        assertThat(StringReader.isAllowedInUnquotedString('@')).isFalse();
        assertThat(StringReader.isAllowedInUnquotedString('!')).isFalse();
        assertThat(StringReader.isAllowedInUnquotedString('/')).isFalse();
    }

    @Test
    void shouldIdentifySpaceAsWhitespace() {
        assertThat(StringReader.isWhitespace(' ')).isTrue();
    }

    @Test
    void shouldIdentifyTabAsWhitespace() {
        assertThat(StringReader.isWhitespace('\t')).isTrue();
    }

    @Test
    void shouldIdentifyNewlineAsWhitespace() {
        assertThat(StringReader.isWhitespace('\n')).isTrue();
    }

    @Test
    void shouldIdentifyCarriageReturnAsWhitespace() {
        assertThat(StringReader.isWhitespace('\r')).isTrue();
    }

    @Test
    void shouldRejectNonWhitespaceCharacters() {
        assertThat(StringReader.isWhitespace('a')).isFalse();
        assertThat(StringReader.isWhitespace('1')).isFalse();
        assertThat(StringReader.isWhitespace('_')).isFalse();
    }

    @Test
    void shouldReadIntMinValue() throws CommandSyntaxException {
        StringReader reader = new StringReader("-2147483648");
        assertThat(reader.readInt()).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    void shouldReadIntMaxValue() throws CommandSyntaxException {
        StringReader reader = new StringReader("2147483647");
        assertThat(reader.readInt()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void shouldReadLongMinValue() throws CommandSyntaxException {
        StringReader reader = new StringReader("-9223372036854775808");
        assertThat(reader.readLong()).isEqualTo(Long.MIN_VALUE);
    }
    
    @Test
    void shouldHandleConsecutiveReads() throws CommandSyntaxException {
        StringReader reader = new StringReader("123 456");

        int first = reader.readInt();
        reader.skipWhitespace();
        int second = reader.readInt();

        assertThat(first).isEqualTo(123);
        assertThat(second).isEqualTo(456);
    }

    @Test
    void shouldHandleMixedTypeReads() throws CommandSyntaxException {
        StringReader reader = new StringReader("true 123 test");

        boolean bool = reader.readBoolean();
        reader.skipWhitespace();
        int num = reader.readInt();
        reader.skipWhitespace();
        String str = reader.readString();

        assertThat(bool).isTrue();
        assertThat(num).isEqualTo(123);
        assertThat(str).isEqualTo("test");
    }

    @Test
    void shouldReadStringWithAllAllowedCharacters() {
        StringReader reader = new StringReader("aZ09_-.+");
        assertThat(reader.readString()).isEqualTo("aZ09_-.+");
        assertThat(reader.getCursor()).isEqualTo(8);
    }

    @Test
    void shouldReadFloatWithZeroBeforeDecimal() throws CommandSyntaxException {
        StringReader reader = new StringReader("0.5");
        assertThat(reader.readFloat()).isEqualTo(0.5f);
        assertThat(reader.getCursor()).isEqualTo(3);
    }

    @Test
    void shouldReadDoubleWithZeroBeforeDecimal() throws CommandSyntaxException {
        StringReader reader = new StringReader("0.123456789");
        assertThat(reader.readDouble()).isEqualTo(0.123456789);
        assertThat(reader.getCursor()).isEqualTo(11);
    }
}