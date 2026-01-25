package com.saji.infrastructre.adapter.secondary.persistence.cqrs.query.core;

/**
 * Marker interface for all queries in the CQRS pattern.
 * Queries represent requests for data without side effects.
 *
 * @param <R> The type of result returned by the query
 */
public interface Query<R> {
}
