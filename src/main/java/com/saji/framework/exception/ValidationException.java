package com.saji.framework.exception;

import lombok.Getter;

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
        this.errors = errors;
    }

    public ValidationException(String field, String error) {
        super("Validation failed for field: " + field);
        this.errors = new HashMap<>();
        this.errors.put(field, error);
    }

    public void addError(String field, String error) {
        this.errors.put(field, error);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
