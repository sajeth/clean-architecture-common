package com.saji.infrastructre.adapter.secondary.persistence.cqrs.query.core;
import reactor.core.publisher.Mono;

/**
 * @author sajethperli
 * Handler interface for processing queries in a reactive manner.
 * Each query type should have exactly one handler.
 *
 * @param <Q> The type of query to handle
 * @param <R> The type of result returned
 */
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * Handle the given query.
     *
     * @param query The query to handle
     * @return A Mono containing the query result
     */
    Mono<R> handle(Q query);

    /**
     * Get the type of query this handler supports.
     *
     * @return The query class type
     */
    Class<Q> getQueryType();
}

