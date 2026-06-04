package io.github.sajeth.framework.domain;

import java.util.List;
import java.util.Objects;

/**
 * Base class for domain value objects, providing structural equality based on components.
 */
public abstract class ValueObject {

    protected abstract List<Object> getEqualityComponents();

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ValueObject that = (ValueObject) o;
        return Objects.equals(getEqualityComponents(), that.getEqualityComponents());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getEqualityComponents().toArray());
    }
}
