package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.query.core;

/**
 * Marker interface for streaming queries in the CQRS pattern.
 * FluxQueries return multiple results as a reactive stream,
 * preserving backpressure for large collections and SSE endpoints.
 *
 * @param <R> The type of each element emitted by the query result stream
 */
public interface FluxQuery<R> {
}
