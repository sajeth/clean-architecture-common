package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a class as a domain event handler.
 *
 * <p>The {@link #event()} attribute explicitly declares which domain event type
 * this handler processes, serving as the single source of truth for event routing.
 *
 * <p>Usage:
 * <pre>{@code
 * @HandlesEvent(OrderCreatedEvent.class)
 * public class SendOrderConfirmationHandler implements DomainEventHandler<OrderCreatedEvent> {
 *     @Override
 *     public Mono<Void> handle(OrderCreatedEvent event) { ... }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HandlesEvent {

    /** Spring bean name — forwarded to {@link Component#value()}. */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /** The domain event type this handler processes. */
    Class<?> event();
}
