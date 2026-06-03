package io.github.sajeth.application.port.output;

import io.github.sajeth.framework.event.DomainEvent;
import reactor.core.publisher.Mono;

public interface DomainEventPublisherOutputPort<E extends DomainEvent> {
    Mono<Void> publish(E event);
}
