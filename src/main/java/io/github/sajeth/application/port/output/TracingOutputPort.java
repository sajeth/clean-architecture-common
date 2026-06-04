package io.github.sajeth.application.port.output;

import java.util.Map;
import reactor.core.publisher.Mono;

/**
 * Output port for distributed tracing of reactive operations.
 */
public interface TracingOutputPort {
    <T> Mono<T> trace(String spanName, Mono<T> operation);

    <T> Mono<T> trace(String spanName, Map<String, String> attributes, Mono<T> operation);
}
