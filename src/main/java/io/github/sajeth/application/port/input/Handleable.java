package io.github.sajeth.application.port.input;

/**
 * Represents a self-contained operation that produces a result when handled.
 *
 * @param <R> the result type
 */
@FunctionalInterface
public interface Handleable<R> {
    R handle();
}
