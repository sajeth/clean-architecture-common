package io.github.sajeth.framework.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception thrown when validation fails
 *
 * @author sajethperli
 */
@Getter
public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = new HashMap<>(errors);
    }

    /**
     * Returns an unmodifiable view of the errors map to prevent external mutation of exception state.
     */
    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    public ValidationException(String field, String error) {
        super("Validation failed for field: " + field);
        this.errors = new HashMap<>();
        this.errors.put(field, error);
    }

    /**
     * Adds a field-level validation error to this exception.
     * Should only be called before the exception is thrown; mutating a propagating
     * exception in a reactive pipeline is unsafe.
     */
    public void addError(String field, String error) {
        this.errors.put(field, error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
