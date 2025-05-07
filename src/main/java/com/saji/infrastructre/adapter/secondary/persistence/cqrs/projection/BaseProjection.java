package com.saji.infrastructre.adapter.secondary.persistence.cqrs.projection;

import com.saji.infrastructre.adapter.secondary.persistence.cqrs.query.TypeQuery;

import java.util.Set;
import java.util.function.Supplier;

public class BaseProjection<T,T1> {

    public Set<T> handle(TypeQuery query, Supplier<Set<T>> supplier) {
        return supplier.get();
    }
}
