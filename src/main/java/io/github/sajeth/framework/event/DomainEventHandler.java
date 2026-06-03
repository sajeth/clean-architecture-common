package io.github.sajeth.framework.event;

import reactor.core.publisher.Mono;

public interface DomainEventHandler<E extends DomainEvent> {
    Mono<Void> handle(E event);
    Class<E> getEventType();
}
