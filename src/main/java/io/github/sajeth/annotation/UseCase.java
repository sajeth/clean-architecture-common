package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * Marks a class as a use-case interactor in the application layer.
 *
 * <p>Combines {@link Service} (Spring component detection) with optional
 * {@link Transactional} behaviour. Set {@code transactional = false} when the
 * use case coordinates purely reactive pipelines that manage their own
 * transaction boundaries.
 *
 * <p>Usage:
 * <pre>{@code
 * @UseCase
 * public class CreateOrderUseCase implements CreateOrderInputPort { ... }
 *
 * @UseCase(transactional = false)
 * public class StreamEventsUseCase implements StreamEventsInputPort { ... }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface UseCase {

    /** Spring bean name — forwarded to {@link Service#value()}. */
    @AliasFor(annotation = Service.class)
    String value() default "";

    /**
     * Whether to wrap the use-case bean in a Spring-managed transaction.
     * Defaults to {@code true}. Set to {@code false} for reactive use cases
     * that manage their own transaction boundaries via R2DBC operators.
     */
    boolean transactional() default true;
}
