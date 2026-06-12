package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a class as a CQRS command handler.
 *
 * <p>The {@link #command()} attribute explicitly declares which command type this
 * handler processes, eliminating the need for a separate {@code getCommandType()}
 * method and preventing dual-source-of-truth bugs where the annotation and the
 * method disagree.
 *
 * <p>Usage:
 * <pre>{@code
 * @HandlesCommand(CreateOrderCommand.class)
 * public class CreateOrderHandler implements CommandHandler<CreateOrderCommand, OrderId> {
 *     @Override
 *     public Mono<OrderId> handle(CreateOrderCommand command) { ... }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HandlesCommand {

    /** Spring bean name — forwarded to {@link Component#value()}. */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /** The command type this handler processes. */
    Class<?> command();
}
