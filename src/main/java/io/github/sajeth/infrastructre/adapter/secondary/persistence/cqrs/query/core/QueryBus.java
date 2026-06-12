package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.query.core;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Query Bus - dispatches queries to their appropriate handlers.
 * Implements the mediator pattern for query processing.
 * Supports both single-result (Mono) and streaming (Flux) queries.
 */
@Component
public class QueryBus extends LoggerAdapter {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();
    private final Map<Class<?>, FluxQueryHandler<?, ?>> fluxHandlers = new HashMap<>();

    /**
     * Constructs a QueryBus with only single-result query handlers.
     *
     * @param queryHandlers the list of query handlers to register
     */
    public QueryBus(List<QueryHandler<?, ?>> queryHandlers) {
        this(queryHandlers, List.of());
    }

    /**
     * Constructs a QueryBus with both single-result and streaming query handlers.
     *
     * @param queryHandlers     the list of single-result query handlers to register
     * @param fluxQueryHandlers the list of streaming query handlers to register
     */
    public QueryBus(
            List<QueryHandler<?, ?>> queryHandlers,
            List<FluxQueryHandler<?, ?>> fluxQueryHandlers) {
        super(QueryBus.class);
        queryHandlers.forEach(handler -> {
            Class<?> queryType = handler.getQueryType();
            if (handlers.containsKey(queryType)) {
                throw new IllegalStateException(
                    "Duplicate QueryHandler registered for type: " + queryType.getSimpleName() +
                    ". Existing: " + handlers.get(queryType).getClass().getSimpleName() +
                    ", Duplicate: " + handler.getClass().getSimpleName());
            }
            handlers.put(queryType, handler);
            debug(MessageFormat.format("Registered query handler: {0} for {1}",
                    handler.getClass().getSimpleName(),
                    queryType.getSimpleName()));
        });
        fluxQueryHandlers.forEach(handler -> {
            fluxHandlers.put(handler.getQueryType(), handler);
            debug(MessageFormat.format("Registered flux query handler: {0} for {1}",
                    handler.getClass().getSimpleName(),
                    handler.getQueryType().getSimpleName()));
        });
        info(MessageFormat.format("QueryBus initialized with {0} handlers and {1} flux handlers",
                handlers.size(), fluxHandlers.size()));
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
                .doOnSuccess(result -> debug(
                        MessageFormat.format("Query {0} completed successfully",
                                query.getClass().getSimpleName())))
                .doOnError(error -> error(MessageFormat.format("Query {0} failed: {1}",
                        query.getClass().getSimpleName(), error.getMessage())));
    }

    /**
     * Dispatch a streaming query to its handler.
     *
     * @param query The streaming query to dispatch
     * @param <R>   The type of each element in the result stream
     * @return A Flux emitting the query results with backpressure support
     */
    @SuppressWarnings("unchecked")
    public <R> Flux<R> dispatchFlux(FluxQuery<R> query) {
        FluxQueryHandler<FluxQuery<R>, R> handler =
                (FluxQueryHandler<FluxQuery<R>, R>) fluxHandlers.get(query.getClass());
        if (handler == null) {
            return Flux.error(new IllegalArgumentException(
                    "No flux handler registered for query: " + query.getClass().getSimpleName()));
        }
        debug(MessageFormat.format(
                "Dispatching flux query: {0}", query.getClass().getSimpleName()));
        return handler.handle(query)
                .doOnComplete(() -> debug(MessageFormat.format("Flux query {0} completed",
                        query.getClass().getSimpleName())))
                .doOnError(error -> error(MessageFormat.format("Flux query {0} failed: {1}",
                        query.getClass().getSimpleName(), error.getMessage())));
    }
}
