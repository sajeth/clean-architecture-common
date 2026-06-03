package io.github.sajeth.framework.exception;

import lombok.Getter;

/**
 * Exception thrown when authorization fails (HTTP 403 Forbidden).
 * Distinguishes from AuthenticationException (HTTP 401) — the caller is
 * authenticated but lacks the required permission to access the resource.
 *
 * @author sajethperli
 */
@Getter
public class AuthorizationException extends RuntimeException {
    private final String errorCode;
    private final String requiredPermission;

    public AuthorizationException(String message) {
        super(message);
        this.errorCode = "ACCESS_DENIED";
        this.requiredPermission = null;
    }

    public AuthorizationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.requiredPermission = null;
    }

    public AuthorizationException(String errorCode, String message, String requiredPermission) {
        super(message);
        this.errorCode = errorCode;
        this.requiredPermission = requiredPermission;
    }
}
