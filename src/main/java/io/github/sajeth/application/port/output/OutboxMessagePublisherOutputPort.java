package io.github.sajeth.application.port.output;

import io.github.sajeth.framework.event.OutboxEvent;
import reactor.core.publisher.Mono;

/**
 * Output port for publishing outbox events to an external message broker.
 */
public interface OutboxMessagePublisherOutputPort {
    Mono<Void> publish(OutboxEvent event);
}
