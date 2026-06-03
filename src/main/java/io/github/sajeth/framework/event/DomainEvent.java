package io.github.sajeth.framework.event;

import java.time.Instant;
import java.util.UUID;

import lombok.Getter;

/**
 * Abstract base for all domain events.
 */
@Getter
public abstract class DomainEvent {
    private final String eventId;
    private final Instant occurredOn;
    private final String aggregateId;

    protected DomainEvent(String aggregateId) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.aggregateId = aggregateId;
    }
}
