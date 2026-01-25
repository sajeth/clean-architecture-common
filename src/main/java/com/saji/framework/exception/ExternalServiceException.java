package com.saji.framework.exception;

import lombok.Getter;

/**
 * Exception thrown when external service call fails
 *
 * @author sajethperli
 */
@Getter
public class ExternalServiceException extends RuntimeException {
    private final String serviceName;
    private final String errorCode;
    private final boolean retryable;

    public ExternalServiceException(String serviceName, String message) {
        super(message);
        this.serviceName = serviceName;
        this.errorCode = "SERVICE_ERROR";
        this.retryable = false;
    }

    public ExternalServiceException(String serviceName, String errorCode, String message, boolean retryable) {
        super(message);
        this.serviceName = serviceName;
        this.errorCode = errorCode;
        this.retryable = retryable;
    }

    public ExternalServiceException(String serviceName, String errorCode, String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.errorCode = errorCode;
        this.retryable = retryable;
    }
}
