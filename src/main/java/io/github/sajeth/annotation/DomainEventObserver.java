package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a method or class as an observer of domain events in the application layer.
 *
 * <p>This annotation is intentionally distinct from Spring's {@code @EventListener}
 * to avoid confusion: {@code @EventListener} is a Spring infrastructure annotation for
 * any ApplicationEvent; {@code @DomainEventObserver} is a domain-layer concept that
 * documents reactive observation of bounded-context domain events.
 *
 * <p>When applied to a <em>class</em>, it declares that the class observes a specific
 * domain event type via its {@link #event()} attribute. When applied to a <em>method</em>,
 * it documents that the method is the observation handler.
 *
 * <p>Usage:
 * <pre>{@code
 * @DomainEventObserver(event = OrderCreatedEvent.class, async = true)
 * public class NotificationService {
 *
 *     @DomainEventObserver(event = OrderCreatedEvent.class)
 *     public Mono<Void> onOrderCreated(OrderCreatedEvent event) { ... }
 * }
 * }</pre>
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface DomainEventObserver {

    /** Spring bean name when applied to a class — forwarded to {@link Component#value()}. */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * The domain event type being observed.
     * Defaults to {@link Void} (unspecified / documentation-only when on a method).
     */
    Class<?> event() default Void.class;

    /**
     * Whether observation is asynchronous (non-blocking, fire-and-forget).
     * Defaults to {@code false} (synchronous / in-transaction observation).
     */
    boolean async() default false;

    /**
     * Ordering hint when multiple observers handle the same event type.
     * Lower values run first. Defaults to {@link Integer#MAX_VALUE} (last / no preference).
     */
    int order() default Integer.MAX_VALUE;
}
