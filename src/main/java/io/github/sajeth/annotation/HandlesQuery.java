package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a class as a CQRS query handler.
 *
 * <p>The {@link #query()} attribute explicitly declares which query type this
 * handler processes, serving as the single source of truth for handler-to-query
 * wiring.
 *
 * <p>Usage:
 * <pre>{@code
 * @HandlesQuery(FindOrderQuery.class)
 * public class FindOrderHandler implements QueryHandler<FindOrderQuery, OrderDto> {
 *     @Override
 *     public Mono<OrderDto> handle(FindOrderQuery query) { ... }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HandlesQuery {

    /** Spring bean name — forwarded to {@link Component#value()}. */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /** The query type this handler processes. */
    Class<?> query();
}
