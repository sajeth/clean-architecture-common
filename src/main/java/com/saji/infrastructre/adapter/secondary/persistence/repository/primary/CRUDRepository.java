package com.saji.infrastructre.adapter.secondary.persistence.repository.primary;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * @author sajethperli
 */
public interface CRUDRepository<T, ID> extends ReactiveCrudRepository<T, ID> {
}
