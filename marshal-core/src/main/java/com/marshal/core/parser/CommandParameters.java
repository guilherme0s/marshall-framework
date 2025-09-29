package com.marshal.core.parser;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
public final class CommandParameters {

    private final Map<String, Object> internalMap = new LinkedHashMap<>();

    public <T> void put(final String key, final T value) {
        internalMap.put(key, value);
    }

    public boolean has(final String key) {
        return internalMap.containsKey(key);
    }

    public ParameterValue get(final String key) {
        return new ParameterValue(internalMap.get(key));
    }

    public boolean isEmpty() {
        return internalMap.isEmpty();
    }

    public Map<String, Object> toMap() {
        return Collections.unmodifiableMap(internalMap);
    }

    public static final class ParameterValue {

        private final @Nullable Object value;

        private ParameterValue(@Nullable final Object value) {
            this.value = value;
        }

        public String asString() {
            return as(String.class);
        }

        public Integer asInteger() {
            return as(Integer.class);
        }

        public Double asDouble() {
            return as(Double.class);
        }

        public Boolean asBoolean() {
            return as(Boolean.class);
        }

        public <T> T as(final Class<T> type) {
            Objects.requireNonNull(value,
                    "Cannot convert a null parameter. Check for presence first or provide a default.");
            return type.cast(value);
        }
    }
}
