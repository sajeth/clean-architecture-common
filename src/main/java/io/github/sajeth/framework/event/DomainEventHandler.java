package io.github.sajeth.framework.event;

import reactor.core.publisher.Mono;

/**
 * Handler for a specific domain event type.
 *
 * @param <E> the event type
 */
public interface DomainEventHandler<E extends DomainEvent> {
    Mono<Void> handle(E event);

    Class<E> getEventType();
}
