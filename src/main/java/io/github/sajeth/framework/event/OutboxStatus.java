package io.github.sajeth.framework.event;

/**
 * Lifecycle states for an outbox event.
 */
public enum OutboxStatus {
    PENDING,
    SENT,
    FAILED
}
