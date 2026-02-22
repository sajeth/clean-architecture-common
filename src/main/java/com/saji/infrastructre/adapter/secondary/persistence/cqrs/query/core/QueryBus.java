package com.saji.infrastructre.adapter.secondary.persistence.cqrs.query.core;


import com.saji.infrastructre.adapter.secondary.logging.LoggerAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Query Bus - dispatches queries to their appropriate handlers.
 * Implements the mediator pattern for query processing.
 */
@Component
public class QueryBus extends LoggerAdapter {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public QueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        super(QueryBus.class);
        queryHandlers.forEach(handler -> {
            handlers.put(handler.getQueryType(), handler);
            debug(MessageFormat.format("Registered query handler: {0} for {1}",
                    handler.getClass().getSimpleName(),
                    handler.getQueryType().getSimpleName()));
        });
        info(MessageFormat.format("QueryBus initialized with {0} handlers", handlers.size()));
    }

    /**
     * Dispatch a query to its handler.
     *
     * @param query The query to dispatch
     * @param <R>   The result type
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

        debug(MessageFormat.format("Dispatching query: {0}", query.getClass().getSimpleName()));
        return handler.handle(query)
                .doOnSuccess(result -> debug(MessageFormat.format("Query {0} completed successfully",
                        query.getClass().getSimpleName())))
                .doOnError(error -> error(MessageFormat.format("Query {0} failed: {1}",
                        query.getClass().getSimpleName(), error.getMessage())));
    }
}
