package io.github.sajeth.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Marks a class as a hexagonal-architecture adapter.
 *
 * <p>Replaces the need for two separate annotations ({@code @PrimaryAdapter} /
 * {@code @SecondaryAdapter}) by encoding direction in the {@link #type()} attribute.
 *
 * <p>The annotation also carries {@code port()} to document which output- or
 * input-port interface this adapter implements, enabling tooling and ArchUnit rules
 * to verify adapter-to-port wiring statically.
 *
 * <p>Usage:
 * <pre>{@code
 * // Inbound REST adapter
 * @Adapter(type = AdapterType.PRIMARY, port = CreateOrderInputPort.class)
 * public class CreateOrderController { ... }
 *
 * // Outbound persistence adapter
 * @Adapter(type = AdapterType.SECONDARY, port = OrderRepositoryOutputPort.class)
 * public class OrderR2dbcAdapter implements OrderRepositoryOutputPort { ... }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Adapter {

    /** Spring bean name — forwarded to {@link Component#value()}. */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * Whether this adapter is primary (inbound / drives the application) or
     * secondary (outbound / driven by the application).
     */
    AdapterType type();

    /**
     * The port interface this adapter implements or invokes.
     * Used as documentation and can be validated by ArchUnit rules.
     * Defaults to {@link Void} (unspecified).
     */
    Class<?> port() default Void.class;
}
