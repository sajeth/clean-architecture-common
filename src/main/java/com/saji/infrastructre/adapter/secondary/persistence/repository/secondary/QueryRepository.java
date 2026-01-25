package com.saji.infrastructre.adapter.secondary.persistence.repository.secondary;


import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

/**
 * @author sajethperli
 */
public interface QueryRepository<T, ID> extends ReactiveSortingRepository<T,ID>,ReactiveCrudRepository<T, ID>, ReactiveQueryByExampleExecutor<T> {
}

