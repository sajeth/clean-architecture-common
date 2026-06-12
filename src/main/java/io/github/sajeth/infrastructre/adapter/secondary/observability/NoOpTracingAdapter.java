package io.github.sajeth.infrastructre.adapter.secondary.observability;

import io.github.sajeth.application.port.output.TracingOutputPort;
import reactor.core.publisher.Mono;

import java.util.Map;

public class NoOpTracingAdapter implements TracingOutputPort {

    @Override
    public <T> Mono<T> trace(String spanName, Mono<T> operation) {
        return operation;
    }

    @Override
    public <T> Mono<T> trace(String spanName, Map<String, String> attributes, Mono<T> operation) {
        return operation;
    }
}
