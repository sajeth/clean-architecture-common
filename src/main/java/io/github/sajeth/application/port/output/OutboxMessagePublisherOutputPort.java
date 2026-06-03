package io.github.sajeth.application.port.output;

import io.github.sajeth.framework.event.OutboxEvent;
import reactor.core.publisher.Mono;

public interface OutboxMessagePublisherOutputPort {
    Mono<Void> publish(OutboxEvent event);
}
