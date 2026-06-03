package io.github.sajeth.infrastructre.adapter.secondary.event;

import io.github.sajeth.application.port.output.DomainEventPublisherOutputPort;
import io.github.sajeth.framework.event.DomainEvent;
import io.github.sajeth.framework.event.DomainEventHandler;
import io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryDomainEventBus extends LoggerAdapter implements DomainEventPublisherOutputPort<DomainEvent> {

    private final Map<Class<?>, List<DomainEventHandler<?>>> handlers = new HashMap<>();

    public InMemoryDomainEventBus(List<DomainEventHandler<?>> eventHandlers) {
        super(InMemoryDomainEventBus.class);
        eventHandlers.forEach(handler ->
                handlers.computeIfAbsent(handler.getEventType(), k -> new java.util.ArrayList<>()).add(handler));
        info(MessageFormat.format("InMemoryDomainEventBus initialized with {0} handler(s)", eventHandlers.size()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Void> publish(DomainEvent event) {
        List<DomainEventHandler<?>> eventHandlers = handlers.getOrDefault(event.getClass(), List.of());
        debug(MessageFormat.format("Publishing event {0} to {1} handler(s)", event.getClass().getSimpleName(), eventHandlers.size()));
        return Flux.fromIterable(eventHandlers)
                .flatMap(handler -> ((DomainEventHandler<DomainEvent>) handler).handle(event))
                .then();
    }
}
