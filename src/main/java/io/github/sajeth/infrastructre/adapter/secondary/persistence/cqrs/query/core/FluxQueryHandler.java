package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.query.core;

import reactor.core.publisher.Flux;

/**
 * Handler interface for processing streaming queries in a reactive manner.
 * Each FluxQuery type should have exactly one FluxQueryHandler.
 *
 * @param <Q> The type of query to handle
 * @param <R> The type of each element in the result stream
 */
public interface FluxQueryHandler<Q extends FluxQuery<R>, R> {

    /**
     * Handle the given streaming query.
     *
     * @param query The query to handle
     * @return A Flux emitting the query results with backpressure support
     */
    Flux<R> handle(Q query);

    /**
     * Get the type of query this handler supports.
     *
     * @return The query class type
     */
    Class<Q> getQueryType();
}
