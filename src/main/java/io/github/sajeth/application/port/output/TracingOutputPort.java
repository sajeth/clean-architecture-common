package io.github.sajeth.application.port.output;

import reactor.core.publisher.Mono;

import java.util.Map;

public interface TracingOutputPort {
    <T> Mono<T> trace(String spanName, Mono<T> operation);

    <T> Mono<T> trace(String spanName, Map<String, String> attributes, Mono<T> operation);
}
