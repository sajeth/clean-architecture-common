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

    /**
     * Constructs an AuthorizationException with a default error code and the given message.
     *
     * @param message human-readable description of the authorization failure
     */
    public AuthorizationException(String message) {
        super(message);
        this.errorCode = "ACCESS_DENIED";
        this.requiredPermission = null;
    }

    /**
     * Constructs an AuthorizationException with the given error code and message.
     *
     * @param errorCode application-specific error code
     * @param message human-readable description of the authorization failure
     */
    public AuthorizationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.requiredPermission = null;
    }

    /**
     * Constructs an AuthorizationException with an error code, message, and required permission.
     *
     * @param errorCode application-specific error code
     * @param message human-readable description of the authorization failure
     * @param requiredPermission the permission that was required but not held
     */
    public AuthorizationException(String errorCode, String message, String requiredPermission) {
        super(message);
        this.errorCode = errorCode;
        this.requiredPermission = requiredPermission;
    }
}
