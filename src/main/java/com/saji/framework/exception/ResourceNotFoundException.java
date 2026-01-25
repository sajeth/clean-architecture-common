package com.saji.framework.exception;

import lombok.Getter;

/**
 * Exception thrown when a requested resource is not found
 *
 * @author sajethperli
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceType;
    private final String resourceId;

    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(String.format("%s not found with id: %s", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String resourceType, String resourceId, Throwable cause) {
        super(String.format("%s not found with id: %s", resourceType, resourceId), cause);
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceType = "Resource";
        this.resourceId = "unknown";
    }
}
