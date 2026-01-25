package com.saji.framework.exception;

import lombok.Getter;

/**
 * Exception thrown when authentication fails
 *
 * @author sajethperli
 */
@Getter
public class AuthenticationException extends RuntimeException {
    private final String errorCode;

    public AuthenticationException(String message) {
        super(message);
        this.errorCode = "AUTH_ERROR";
    }

    public AuthenticationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthenticationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
