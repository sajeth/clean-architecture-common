package io.github.sajeth.application.port.output;

import io.github.sajeth.framework.event.DomainEvent;
import reactor.core.publisher.Mono;

/**
 * Output port for publishing domain events.
 *
 * @param <E> the event type
 */
public interface DomainEventPublisherOutputPort<E extends DomainEvent> {
    Mono<Void> publish(E event);
}
