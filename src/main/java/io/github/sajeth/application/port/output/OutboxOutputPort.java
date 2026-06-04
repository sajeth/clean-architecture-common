package io.github.sajeth.application.port.output;

import io.github.sajeth.framework.event.OutboxEvent;
import io.github.sajeth.framework.event.OutboxStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Output port for persisting and querying transactional outbox events.
 */
public interface OutboxOutputPort {

    Mono<Void> save(OutboxEvent event);

    Flux<OutboxEvent> findByStatus(OutboxStatus status);

    Mono<Void> updateStatus(String eventId, OutboxStatus status);
}
