package com.marshal.parser;

public class StringReader {

    private final String string;
    private int cursor;

    public StringReader(String string) {
        this.string = string;
        this.cursor = 0;
    }

    public StringReader(StringReader other) {
        this.string = other.string;
        this.cursor = other.cursor;
    }

    public String getString() {
        return string;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        if (cursor < 0 || cursor > string.length()) {
            throw new IndexOutOfBoundsException("Cursor out of range: " + cursor);
        }
        this.cursor = cursor;
    }

    public int getRemainingLength() {
        return string.length() - cursor;
    }

    public int getTotalLength() {
        return string.length();
    }

    public boolean canRead() {
        return canRead(1);
    }

    public boolean canRead(final int length) {
        return cursor + length <= string.length();
    }

    public char peek() {
        return string.charAt(cursor);
    }

    public char peek(int offset) {
        return string.charAt(cursor + offset);
    }

    public char read() {
        return string.charAt(cursor++);
    }

    public void skip() {
        cursor++;
    }

    public void skipWhitespace() {
        while (canRead() && Character.isWhitespace(peek())) {
            skip();
        }
    }

    public int readInt() throws CommandSyntaxException {
        int start = cursor;
        skipOptionalSign();

        if (!canRead() || !isDigit(peek())) {
            throw new CommandSyntaxException("Expected int", this);
        }

        skipDigits();
        String number = string.substring(start, cursor);

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandSyntaxException("Invalid int", this);
        }
    }

    public long readLong() throws CommandSyntaxException {
        int start = cursor;
        skipOptionalSign();

        if (!canRead() || !isDigit(peek())) {
            throw new CommandSyntaxException("Expected long", this);
        }

        skipDigits();
        String number = string.substring(start, cursor);

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandSyntaxException("Invalid long", this);
        }
    }

    public float readFloat() throws CommandSyntaxException {
        int start = cursor;
        skipOptionalSign();

        if (!canRead() || !isDigit(peek())) {
            throw new CommandSyntaxException("Expected float", this);
        }

        skipNumericPart();
        String number = string.substring(start, cursor);

        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandSyntaxException("Invalid float", this);
        }
    }

    public double readDouble() throws CommandSyntaxException {
        int start = cursor;
        skipOptionalSign();

        if (!canRead() || !isDigit(peek())) {
            throw new CommandSyntaxException("Expected double", this);
        }

        skipNumericPart();
        String number = string.substring(start, cursor);

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            cursor = start;
            throw new CommandSyntaxException("Invalid double", this);
        }
    }

    public String readString() {
        if (!canRead()) {
            return "";
        }

        int start = cursor;
        while (canRead() && isAllowedInUnquotedString(peek())) {
            skip();
        }
        return string.substring(start, cursor);
    }

    @SuppressWarnings("IfCanBeSwitch")
    public Boolean readBoolean() throws CommandSyntaxException {
        int start = cursor;
        String value = readString();

        if (value.isEmpty()) {
            throw new CommandSyntaxException("Expected boolean", this);
        }

        if ("true".equals(value)) {
            return true;
        } else if ("false".equals(value)) {
            return false;
        }

        cursor = start;
        throw new CommandSyntaxException("Expected boolean", this);
    }

    public String getRead() {
        return string.substring(0, cursor);
    }

    public String getRemaining() {
        return string.substring(cursor);
    }

    private void skipOptionalSign() {
        if (canRead() && peek() == '-') {
            skip();
        }
    }

    private void skipDigits() {
        while (canRead() && isDigit(peek())) {
            skip();
        }
    }

    private void skipNumericPart() {
        skipDigits();

        if (canRead() && peek() == '.') {
            skip();
            skipDigits();
        }

        if (canRead() && (peek() == 'e' || peek() == 'E')) {
            skip();
            if (canRead() && (peek() == '+' || peek() == '-')) {
                skip();
            }
            skipDigits();
        }
    }

    public static boolean isAllowedInUnquotedString(char c) {
        return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
    }

    public static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
