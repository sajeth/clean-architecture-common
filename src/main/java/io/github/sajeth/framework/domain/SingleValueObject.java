package io.github.sajeth.framework.domain;

import java.util.List;

public abstract class SingleValueObject<T> extends ValueObject {
    private final T value;

    protected SingleValueObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    protected List<Object> getEqualityComponents() {
        return List.of(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
