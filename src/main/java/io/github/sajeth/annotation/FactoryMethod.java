package io.github.sajeth.annotation;

import java.lang.annotation.*;

/**
 * Documents a static method as a Factory Method in the GoF creational pattern sense.
 *
 * <p>This is a pure documentation annotation ({@link RetentionPolicy#SOURCE} — erased
 * at compile time). It communicates intent to readers and tooling without adding any
 * runtime overhead or framework coupling. Consumers who want ArchUnit enforcement can
 * detect this annotation at the source level.
 *
 * <p>The {@link #produces()} attribute names the type the factory method creates,
 * which is especially useful when the return type is a supertype or interface.
 *
 * <p>Usage:
 * <pre>{@code
 * public class OrderId {
 *
 *     @FactoryMethod(produces = OrderId.class)
 *     public static OrderId of(String value) {
 *         return new OrderId(value);
 *     }
 *
 *     @FactoryMethod(produces = OrderId.class)
 *     public static OrderId newId() {
 *         return new OrderId(UUID.randomUUID().toString());
 *     }
 * }
 * }</pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface FactoryMethod {

    /**
     * The concrete type this factory method produces.
     * Defaults to {@link Void} (unspecified).
     */
    Class<?> produces() default Void.class;
}
