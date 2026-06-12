package io.github.sajeth.framework.event;

import java.time.Instant;

import lombok.Getter;

/**
 * Represents a domain event persisted in the transactional outbox.
 */
@Getter
public class OutboxEvent {
    private final String id;
    private final String aggregateId;
    private final String eventType;
    private final String payload;
    private final Instant createdAt;
    private OutboxStatus status;
    private int retryCount;

    public OutboxEvent(String id, String aggregateId, String eventType, String payload) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = Instant.now();
        this.status = OutboxStatus.PENDING;
        this.retryCount = 0;
    }

    public void markSent() {
        this.status = OutboxStatus.SENT;
    }

    public void markFailed() {
        this.status = OutboxStatus.FAILED;
        this.retryCount++;
    }
}
