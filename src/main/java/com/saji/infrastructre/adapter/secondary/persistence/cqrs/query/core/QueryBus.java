package com.saji.infrastructre.adapter.secondary.persistence.cqrs.query.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query Bus - dispatches queries to their appropriate handlers.
 * Implements the mediator pattern for query processing.
 */
@Component
public class QueryBus {

    private static final Logger log = LoggerFactory.getLogger(QueryBus.class);
    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public QueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        queryHandlers.forEach(handler -> {
            handlers.put(handler.getQueryType(), handler);
            log.debug("Registered query handler: {} for {}",
                handler.getClass().getSimpleName(),
                handler.getQueryType().getSimpleName());
        });
        log.info("QueryBus initialized with {} handlers", handlers.size());
    }

    /**
     * Dispatch a query to its handler.
     *
     * @param query The query to dispatch
     * @param <R> The result type
     * @return A Mono containing the query result
     */
    @SuppressWarnings("unchecked")
    public <R> Mono<R> dispatch(Query<R> query) {
        QueryHandler<Query<R>, R> handler =
            (QueryHandler<Query<R>, R>) handlers.get(query.getClass());

        if (handler == null) {
            return Mono.error(new IllegalArgumentException(
                "No handler registered for query: " + query.getClass().getSimpleName()));
        }

        log.debug("Dispatching query: {}", query.getClass().getSimpleName());
        return handler.handle(query)
            .doOnSuccess(result -> log.debug("Query {} completed successfully",
                query.getClass().getSimpleName()))
            .doOnError(error -> log.error("Query {} failed: {}",
                query.getClass().getSimpleName(), error.getMessage()));
    }
}
